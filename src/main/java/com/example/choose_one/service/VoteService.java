package com.example.choose_one.service;

import com.example.choose_one.common.api.Api;
import com.example.choose_one.common.error.PostErrorCode;
import com.example.choose_one.common.error.UserErrorCode;
import com.example.choose_one.common.error.VoteErrorCode;
import com.example.choose_one.common.exception.ApiException;
import com.example.choose_one.entity.VoteEntity;
import com.example.choose_one.model.VoteRequest;
import com.example.choose_one.repository.PostRepository;
import com.example.choose_one.repository.UserRepository;
import com.example.choose_one.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VoteService {
    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public Api<String> create(VoteRequest voteRequest) {
        var user = userRepository.findById(voteRequest.getUserId())
                .orElseThrow(() -> {
                    return new ApiException(UserErrorCode.USER_NOT_FOUND, "올바른 user id를 입력하십시오.");
                });
        var post = postRepository.findById(voteRequest.getPostId())
                .orElseThrow(() -> {
                    return new ApiException(PostErrorCode.POST_NOT_FOUND,"올바른 post id를 입력하십시오.");
                });

        if(!(voteRequest.getVoteOption()=='A' || voteRequest.getVoteOption()=='B')){
            throw new ApiException(VoteErrorCode.INVALID_VOTE,"A or B 중 선택 가능합니다.");
        }

        var alreadyVoted = voteRepository.existsByUserIdAndPostId(user.getId(), post.getId());
        if(alreadyVoted){
            throw new ApiException(VoteErrorCode.DUPLICATE_VOTE,"다른 글에 투표 하십시오.");
        }

        var entity = VoteEntity.builder()
                .user(user)
                .post(post)
                .voteOption(voteRequest.getVoteOption())
                .build();
        voteRepository.save(entity);
        return Api.OK("투표가 완료되었습니다.");
    }
}
