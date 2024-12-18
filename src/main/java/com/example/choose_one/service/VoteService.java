package com.example.choose_one.service;

import com.example.choose_one.common.api.Api;
import com.example.choose_one.common.error.PostErrorCode;
import com.example.choose_one.common.error.UserErrorCode;
import com.example.choose_one.common.error.VoteErrorCode;
import com.example.choose_one.common.exception.ApiException;
import com.example.choose_one.entity.VoteEntity;
import com.example.choose_one.model.vote.VoteRequest;
import com.example.choose_one.repository.PostRepository;
import com.example.choose_one.repository.UserRepository;
import com.example.choose_one.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class VoteService {
    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public Api<String> create(VoteRequest voteRequest) {
        var requestContext = SecurityContextHolder.getContext().getAuthentication();
        var userId = (Long) requestContext.getPrincipal();
        var user = userRepository.findById(userId)
                .orElseThrow(()-> new ApiException(UserErrorCode.USER_NOT_FOUND));
        var post = postRepository.findById(voteRequest.getPostId())
                .orElseThrow(() -> {
                    return new ApiException(PostErrorCode.POST_NOT_FOUND,"올바른 post id를 입력하십시오.");
                });

        if(!(voteRequest.getVoteOption()=='A' || voteRequest.getVoteOption()=='B')){
            throw new ApiException(VoteErrorCode.INVALID_VOTE,"A or B 중 선택 가능합니다.");
        }

        var alreadyVoted = voteRepository.existsByUserIdAndPostId(userId, post.getId());
        if(alreadyVoted){
            throw new ApiException(VoteErrorCode.DUPLICATE_VOTE,"다른 글에 투표 하십시오.");
        }

        var entity = VoteEntity.builder()
                .user(user)
                .post(post)
                .voteOption(voteRequest.getVoteOption())
                .build();
        voteRepository.save(entity);

        // 실시간 투표 업데이트 및 websocket 메세지 전송
        updateVoteCount(post.getId());

        return Api.OK("투표가 완료되었습니다.");
    }

    private void updateVoteCount(Long postId) {

        // 투표 수 갱신
        Long countA = voteRepository.countByPostIdAndVoteOption(postId,'A');
        Long countB = voteRepository.countByPostIdAndVoteOption(postId, 'B');

        // 메세지 데이터 담을 Map
        var response = new HashMap<String, Long>();
        response.put("countA", countA);
        response.put("countB", countB);

        // websocket으로 메세지 전송
        simpMessagingTemplate.convertAndSend("/topic"+postId, response);
    }
}
