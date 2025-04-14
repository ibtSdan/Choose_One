package com.example.choose_one.metrics;

import com.example.choose_one.repository.VoteRepository;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

@Service
@RequiredArgsConstructor

public class VoteMetricsService {
    private final MeterRegistry meterRegistry;
    private final VoteRepository voteRepository;

    private volatile long voteCount = 0;

    @Scheduled(fixedRate = 5000) // 5초마다 업데이트
    public void updateVoteCountMetric() {
        // post가 너무 많아지면 관리 어려움 확인
        // 간단히 총 투표수 확인
        // 테스트 시 특정 postId 고정 ex.999L
        this.voteCount = voteRepository.count();
    }

    @PostConstruct
    public void init() {
        Gauge.builder("vote_persisted_count", this, VoteMetricsService::getVoteCount)
                .description("DB에 저장된 vote 수")
                .register(meterRegistry);
    }

    public double getVoteCount() {
        return voteCount;
    }
}
