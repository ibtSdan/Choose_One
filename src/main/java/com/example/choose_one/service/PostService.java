package com.example.choose_one.service;

import com.example.choose_one.common.api.Api;
import com.example.choose_one.common.api.ApiPagination;
import com.example.choose_one.common.api.Pagination;
import com.example.choose_one.common.error.PostErrorCode;
import com.example.choose_one.common.error.UserErrorCode;
import com.example.choose_one.common.exception.ApiException;
import com.example.choose_one.entity.PostEntity;
import com.example.choose_one.model.post.ViewResponse;
import com.example.choose_one.model.post.PostAllResponse;
import com.example.choose_one.model.post.PostRequest;
import com.example.choose_one.repository.PostRepository;
import com.example.choose_one.repository.UserRepository;
import com.example.choose_one.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
                    return new ApiException(UserErrorCode.USER_NOT_FOUND, "올바른 user id를 입력하십시오.");
                });

        var entity = PostEntity.builder()
                .user(user)
                .title(postRequest.getTitle())
                .contentA(postRequest.getContentA())
                .contentB(postRequest.getContentB())
                .build();
        postRepository.save(entity);
        return Api.OK("글 작성 완료");
    }

    public Api<ViewResponse> view(Long postId) {
        var entity = postRepository.findById(postId)
                .orElseThrow(() -> {
                    return new ApiException(PostErrorCode.POST_NOT_FOUND,"올바른 post id를 입력하십시오.");
                });

        var data = ViewResponse.builder()
                .title(entity.getTitle())
                .contentA(entity.getContentA())
                .contentB(entity.getContentB())
                .countA(voteRepository.countByPostIdAndVoteOption(postId, 'A'))
                .countB(voteRepository.countByPostIdAndVoteOption(postId, 'B'))
                .build();

        return Api.OK(data);
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
        var response = ApiPagination.<List<PostAllResponse>>builder()
                .body(body)
                .pagination(pagination)
                .build();
        return Api.OK(response);
    }
}
