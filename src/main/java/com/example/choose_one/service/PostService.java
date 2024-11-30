package com.example.choose_one.service;

import com.example.choose_one.entity.PostEntity;
import com.example.choose_one.model.PostRequest;
import com.example.choose_one.repository.PostRepository;
import com.example.choose_one.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
