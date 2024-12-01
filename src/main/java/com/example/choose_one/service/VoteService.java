package com.example.choose_one.service;

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

    public String create(VoteRequest voteRequest) {
        var user = userRepository.findById(voteRequest.getUserId())
                .orElseThrow(() -> {
                    return new RuntimeException("Not Found User");
                });
        var post = postRepository.findById(voteRequest.getPostId())
                .orElseThrow(() -> {
                    return new RuntimeException("Not Found Post");
                });

        var entity = VoteEntity.builder()
                .user(user)
                .post(post)
                .voteOption(voteRequest.getVoteOption())
                .build();
        voteRepository.save(entity);
        return "Vote successfully created";
    }
}
