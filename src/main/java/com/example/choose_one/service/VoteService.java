package com.example.choose_one.service;

import com.example.choose_one.common.Api;
import com.example.choose_one.entity.VoteEntity;
import com.example.choose_one.exceptionHandler.exception.DuplicateVoteException;
import com.example.choose_one.exceptionHandler.exception.InvalidVoteException;
import com.example.choose_one.model.VoteRequest;
import com.example.choose_one.repository.PostRepository;
import com.example.choose_one.repository.UserRepository;
import com.example.choose_one.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class VoteService {
    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public Api<String> create(VoteRequest voteRequest) {
        var user = userRepository.findById(voteRequest.getUserId())
                .orElseThrow(() -> {
                    return new NoSuchElementException("해당하는 User가 존재하지 않습니다.");
                });
        var post = postRepository.findById(voteRequest.getPostId())
                .orElseThrow(() -> {
                    return new NoSuchElementException("해당하는 Post가 존재하지 않습니다.");
                });

        if(!(voteRequest.getVoteOption()=='A' || voteRequest.getVoteOption()=='B')){
            throw new InvalidVoteException("A or B 중에서 선택 가능합니다.");
        }

        var alreadyVoted = voteRepository.existsByUserIdAndPostId(user.getId(), post.getId());
        if(alreadyVoted){
            throw new DuplicateVoteException("중복 투표는 허용되지 않습니다.");
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
