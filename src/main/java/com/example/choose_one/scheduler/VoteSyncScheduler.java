package com.example.choose_one.scheduler;

import com.example.choose_one.repository.PostRepository;
import com.example.choose_one.repository.VoteRepository;
import com.example.choose_one.service.VoteCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class VoteSyncScheduler {
    private final PostRepository postRepository;
    private final VoteRepository voteRepository;
    private final VoteCacheService voteCacheService;

    // 일정 주기마다 redis 업데이트
    @Scheduled(fixedRate = 60000) // 60초
    public void syncVoteCountAllPosts() {
        List<Long> postIds = postRepository.findAllPostIds();

        for (Long postId : postIds) {
            Long countA = voteRepository.countByPostIdAndVoteOption(postId, 'A');
            Long countB = voteRepository.countByPostIdAndVoteOption(postId, 'B');
            Long total = countA + countB;

            voteCacheService.updateVoteCountInCache(postId, 'A', countA);
            voteCacheService.updateVoteCountInCache(postId, 'B', countB);
            voteCacheService.updateVoteCountInCache(postId, total);
        }
    }
}
