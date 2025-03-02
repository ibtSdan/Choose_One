package com.example.choose_one.repository;

import com.example.choose_one.entity.VoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface VoteRepository extends JpaRepository<VoteEntity, Long> {
    Long countByPostIdAndVoteOption(Long postId, Character voteOption);
    boolean existsByUserIdAndPostId(Long userId, Long postId);
    @Query("SELECT v.post.id, COUNT(v.post.id) FROM vote v WHERE v.post.id IN :postIds GROUP BY v.post.id")
    List<Object[]> countVotesByPostIds(@Param("postIds") List<Long> postIds);
}
