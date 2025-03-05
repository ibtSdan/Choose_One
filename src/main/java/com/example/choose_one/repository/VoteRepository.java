package com.example.choose_one.repository;

import com.example.choose_one.entity.VoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface VoteRepository extends JpaRepository<VoteEntity, Long> {
    Long countByPostIdAndVoteOption(Long postId, Character voteOption);
    boolean existsByUserIdAndPostId(Long userId, Long postId);
    @Query("SELECT p.id, COALESCE(COUNT(v), 0) FROM post p LEFT JOIN vote v ON p.id = v.post.id WHERE p.id IN :postIds GROUP BY p.id")
    List<Object[]> countVotesByPostIds(@Param("postIds") List<Long> postIds);
}
