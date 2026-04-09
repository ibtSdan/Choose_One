package com.example.choose_one.component;

import com.example.choose_one.common.api.Api;
import com.example.choose_one.model.vote.VoteRequest;
import com.example.choose_one.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VoteFacade {

    private final VoteService voteService;

    public Api<String> voteWithOptimisticLock(VoteRequest voteRequest) {
        while (true) {
            try {
                voteService.create(voteRequest);
                break;
            } catch (ObjectOptimisticLockingFailureException e) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        return Api.OK("투표가 완료되었습니다.");
    }
}
