package com.example.choose_one.service;

import com.example.choose_one.entity.PostEntity;
import com.example.choose_one.model.ViewResponse;
import com.example.choose_one.model.PostAllResponse;
import com.example.choose_one.model.PostRequest;
import com.example.choose_one.repository.PostRepository;
import com.example.choose_one.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public String create(PostRequest postRequest) {
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
        return "Post successfully created";
    }

    public ViewResponse view(Long postId) {
        var entity = postRepository.findById(postId)
                .orElseThrow(() -> {
                    return new RuntimeException("Not Found Post");
                });
        return ViewResponse.builder()
                .title(entity.getTitle())
                .contentA(entity.getContentA())
                .contentB(entity.getContentB())
                .build();
    }

    public List<PostAllResponse> all() {
        return postRepository.findAll().stream()
                .map(it -> {
                    var totalVotes = it.getVoteList().size();
                    return PostAllResponse.builder()
                            .postId(it.getId())
                            .title(it.getTitle())
                            .contentA(it.getContentA())
                            .contentB(it.getContentB())
                            .totalVotes(totalVotes)
                            .build();
                }).toList();
    }
}