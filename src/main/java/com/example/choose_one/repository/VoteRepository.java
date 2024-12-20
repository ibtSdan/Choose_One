package com.example.choose_one.repository;

import com.example.choose_one.entity.VoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<VoteEntity, Long> {
    Long countByPostId(Long postId);
    Long countByPostIdAndVoteOption(Long postId, Character voteOption);

    boolean existsByUserIdAndPostId(Long userId, Long postId);
}
