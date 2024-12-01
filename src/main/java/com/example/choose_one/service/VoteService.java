package com.example.choose_one.service;

import com.example.choose_one.common.Api;
import com.example.choose_one.entity.VoteEntity;
import com.example.choose_one.model.VoteRequest;
import com.example.choose_one.repository.PostRepository;
import com.example.choose_one.repository.UserRepository;
import com.example.choose_one.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
                    return new RuntimeException("Not Found User");
                });
        var post = postRepository.findById(voteRequest.getPostId())
                .orElseThrow(() -> {
                    return new RuntimeException("Not Found Post");
                });

        var alreadyVoted = voteRepository.existsByUserIdAndPostId(user.getId(), post.getId());
        if(alreadyVoted){
            throw new RuntimeException("중복 투표는 허용되지 않습니다.");
        }

        var entity = VoteEntity.builder()
                .user(user)
                .post(post)
                .voteOption(voteRequest.getVoteOption())
                .build();
        voteRepository.save(entity);
        return Api.<String>builder()
                .resultCode(String.valueOf(HttpStatus.OK.value()))
                .resultMessage(HttpStatus.OK.getReasonPhrase())
                .data("투표가 완료되었습니다.")
                .build();
    }
}
