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

    private volatile long voteCount = 0;  // volatile 필수

    @Scheduled(fixedRate = 5000) // 5초마다 업데이트
    public void updateVoteCountMetric() {
        this.voteCount = voteRepository.countByPostId(999L);
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
