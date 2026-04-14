package com.example.choose_one.component;

import com.example.choose_one.common.api.Api;
import com.example.choose_one.common.error.ErrorCode;
import com.example.choose_one.common.exception.ApiException;
import com.example.choose_one.model.vote.VoteRequest;
import com.example.choose_one.service.VoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class VoteFacade {

    private final VoteService voteService;
    private final RedissonClient redissonClient;

    public Api<String> voteWithDistributedLock(VoteRequest voteRequest) {
        String lockName = "lock:post:" + voteRequest.getPostId();
        RLock lock = redissonClient.getLock(lockName);

        try {
            boolean available = lock.tryLock(15, 1, TimeUnit.SECONDS);

            if (!available) {
                log.warn("락 획득 실패: {}", lockName);
                throw new ApiException(ErrorCode.LOCK_FAILED, "잠시 후 다시 시도해주세요.");
            }

            log.info("락 획득 성공, 로직 수행");
            return voteService.create(voteRequest);
        } catch (InterruptedException e) {
            log.info("락 에러 발생");
            Thread.currentThread().interrupt();
            throw new RuntimeException();
        } finally {
            if (lock.isHeldByCurrentThread()) {
                log.info("락 해제");
                lock.unlock();
            }
        }
    }
}
