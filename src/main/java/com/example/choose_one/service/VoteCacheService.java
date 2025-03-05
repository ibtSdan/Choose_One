package com.example.choose_one.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VoteCacheService {
    private final StringRedisTemplate redisTemplate;

    @Value("${redis.vote.key.prefix}")
    private String voteCachePrefix;

    @Value("${redis.post.key.prefix}")
    private String postCachePrefix;

    // 캐시에서 투표 수를 가져오고 없으면 DB에서 조회
    public Long getVoteCountFromCache(Long postId, char voteOption) {
        String cacheKey = voteCachePrefix + postId + ":" + voteOption;
        String cachedVoteCount = redisTemplate.opsForValue().get(cacheKey);

        if (cachedVoteCount != null) {
            return Long.parseLong(cachedVoteCount);
        }

        // 캐시 값이 없으면 DB에서 조회
        return -1L;
    }

    public Long getVoteCountFromCache(Long postId){
        String cacheKey = postCachePrefix + postId;
        String cachedVoteCount = redisTemplate.opsForValue().get(cacheKey);

        if (cachedVoteCount != null){
            return Long.parseLong(cachedVoteCount);
        }

        return -1L;
    }

    // 캐시된 투표 수 업데이트
    public void updateVoteCountInCache(Long postId, char voteOption, Long count) {
        String cacheKey = voteCachePrefix + postId + ":" + voteOption;
        redisTemplate.opsForValue().set(cacheKey, String.valueOf(count));
    }

    public void updateVoteCountInCache(Long postId, Long count){
        String cacheKey = postCachePrefix + postId;
        System.out.println("getVote: "+cacheKey);
        redisTemplate.opsForValue().set(cacheKey, String.valueOf(count));
    }

    // Redis에서 투표 수를 증가시키는 방법
    public void incrementVoteCountInCache(Long postId, char voteOption) {
        String cacheKey = voteCachePrefix + postId + ":" + voteOption;
        redisTemplate.opsForValue().increment(cacheKey, 1);
    }
}
