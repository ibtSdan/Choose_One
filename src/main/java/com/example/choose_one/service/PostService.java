package com.example.choose_one.service;

import com.example.choose_one.common.Api;
import com.example.choose_one.common.ApiPagination;
import com.example.choose_one.common.Pagination;
import com.example.choose_one.entity.PostEntity;
import com.example.choose_one.model.ViewResponse;
import com.example.choose_one.model.PostAllResponse;
import com.example.choose_one.model.PostRequest;
import com.example.choose_one.repository.PostRepository;
import com.example.choose_one.repository.UserRepository;
import com.example.choose_one.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final VoteRepository voteRepository;

    public Api<String> create(PostRequest postRequest) {
        var user = userRepository.findById(postRequest.getUserId())
                .orElseThrow(() -> {
                    return new RuntimeException("Not Found User");
                });

        var entity = PostEntity.builder()
                .user(user)
                .title(postRequest.getTitle())
                .contentA(postRequest.getContentA())
                .contentB(postRequest.getContentB())
                .build();
        postRepository.save(entity);
        return Api.<String>builder()
                .resultCode(String.valueOf(HttpStatus.OK.value()))
                .resultMessage(HttpStatus.OK.getReasonPhrase())
                .data("글 작성 완료")
                .build();
    }

    public Api<ViewResponse> view(Long postId) {
        var entity = postRepository.findById(postId)
                .orElseThrow(() -> {
                    return new RuntimeException("Not Found Post");
                });

        var data = ViewResponse.builder()
                .title(entity.getTitle())
                .contentA(entity.getContentA())
                .contentB(entity.getContentB())
                .countA(voteRepository.countByPostIdAndVoteOption(postId, 'A'))
                .countB(voteRepository.countByPostIdAndVoteOption(postId, 'B'))
                .build();

        return Api.<ViewResponse>builder()
                .resultCode(String.valueOf(HttpStatus.OK.value()))
                .resultMessage(HttpStatus.OK.getReasonPhrase())
                .data(data)
                .build();
    }

    public Api<ApiPagination<List<PostAllResponse>>> all(Pageable pageable) {
        var list = postRepository.findAll(pageable);
        return getApiPaginationApi(list);
    }

    public Api<ApiPagination<List<PostAllResponse>>> userPost(Long userId, Pageable pageable) {
        var list = postRepository.findByUserId(userId,pageable);
        return getApiPaginationApi(list);
    }

    private Api<ApiPagination<List<PostAllResponse>>> getApiPaginationApi(Page<PostEntity> list) {
        // pagination
        var pagination = Pagination.builder()
                .page(list.getNumber())
                .size(list.getSize())
                .currentElements(list.getNumberOfElements())
                .totalPage(list.getTotalPages())
                .totalElements(list.getTotalElements())
                .build();
        var body = list.toList().stream()
                .map(it -> {
                    return PostAllResponse.builder()
                            .postId(it.getId())
                            .title(it.getTitle())
                            .contentA(it.getContentA())
                            .contentB(it.getContentB())
                            .totalVotes(voteRepository.countByPostId(it.getId()))
                            .build();
                }).toList();

        return Api.<ApiPagination<List<PostAllResponse>>>builder()
                .resultCode(String.valueOf(HttpStatus.OK.value()))
                .resultMessage(HttpStatus.OK.getReasonPhrase())
                .data(ApiPagination.<List<PostAllResponse>>builder()
                        .body(body)
                        .pagination(pagination)
                        .build())
                .build();
    }
}
