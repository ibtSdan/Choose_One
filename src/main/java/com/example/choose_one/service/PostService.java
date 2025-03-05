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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j

public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final VoteRepository voteRepository;
    private final VoteCacheService voteCacheService;
    private final StringRedisTemplate redisTemplate;

    public Api<String> create(PostRequest postRequest) {
        var requestContext = SecurityContextHolder.getContext().getAuthentication();
        var userId = (Long) requestContext.getPrincipal();
        var user = userRepository.findById(userId)
                .orElseThrow(()-> new ApiException(UserErrorCode.USER_NOT_FOUND));

        var entity = PostEntity.builder()
                .user(user)
                .title(postRequest.getTitle())
                .contentA(postRequest.getContentA())
                .contentB(postRequest.getContentB())
                .build();
        postRepository.save(entity);
        return Api.OK("글 작성 완료: "+entity.getId());
    }

    public Api<ViewResponse> view(Long postId) {
        var entity = postRepository.findById(postId)
                .orElseThrow(() ->
                    new ApiException(PostErrorCode.POST_NOT_FOUND,"올바른 post id를 입력하십시오.")
                );

        Long countA = voteCacheService.getVoteCountFromCache(postId, 'A');
        Long countB = voteCacheService.getVoteCountFromCache(postId, 'B');

        if (countA == -1L || countB == -1L){
            log.info("상세글 디비 접근");
            countA = voteRepository.countByPostIdAndVoteOption(postId,'A');
            countB = voteRepository.countByPostIdAndVoteOption(postId,'B');

            // Redis 업데이트
            voteCacheService.updateVoteCountInCache(postId,'A',countA);
            voteCacheService.updateVoteCountInCache(postId,'B',countB);
        }

        var data = ViewResponse.builder()
                .title(entity.getTitle())
                .contentA(entity.getContentA())
                .contentB(entity.getContentB())
                .countA(countA)
                .countB(countB)
                .build();

        return Api.OK(data);
    }

    public Api<ApiPagination<List<PostAllResponse>>> all(Pageable pageable) {
        var list = postRepository.findAll(pageable);
        return getApiPaginationApi(list);
    }

    public Api<ApiPagination<List<PostAllResponse>>> userPost(Pageable pageable) {
        var requestContext = SecurityContextHolder.getContext().getAuthentication();
        var userId = (Long) requestContext.getPrincipal();
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

        var postIds = list.stream().map(PostEntity::getId).collect(Collectors.toList());
        Map<Long, Long> voteCountsMap = new HashMap<>();

        // 캐시가 존재 하는 경우
        for (Long postId : postIds) {
            Long totalVote = voteCacheService.getVoteCountFromCache(postId);
            if (totalVote != -1L){
                voteCountsMap.put(postId, totalVote);
            }
        }

        // 없는 경우
        List<Long> postIdsNotInCache = postIds.stream()
                .filter(postId -> !voteCountsMap.containsKey(postId))
                .collect(Collectors.toList());

        if (!postIdsNotInCache.isEmpty()) {
            log.info("전체 디비 접근");
            List<Object[]> voteCounts = voteRepository.countVotesByPostIds(postIdsNotInCache);

            for (Object[] result : voteCounts) {
                Long postId = (Long) result[0]; // postId
                Long count = (Long) result[1];  // vote count

                // Redis에 캐시 저장
                voteCacheService.updateVoteCountInCache(postId,count);

                voteCountsMap.put(postId, count);
            }
        }

        var body = list.toList().stream()
                .map(it -> {
                    return PostAllResponse.builder()
                            .postId(it.getId())
                            .title(it.getTitle())
                            .contentA(it.getContentA())
                            .contentB(it.getContentB())
                            .totalVotes(voteCountsMap.getOrDefault(it.getId(), 0L))
                            .build();
                }).toList();

        var response = ApiPagination.<List<PostAllResponse>>builder()
                .body(body)
                .pagination(pagination)
                .build();
        return Api.OK(response);
    }

    public Api<String> delete(Long id) {
        var entity = postRepository.findById(id)
                .orElseThrow(()-> new ApiException(PostErrorCode.POST_NOT_FOUND));
        postRepository.delete(entity);
        return Api.OK("삭제 완료");
    }
}
