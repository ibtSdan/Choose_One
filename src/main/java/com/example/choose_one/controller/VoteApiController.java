package com.example.choose_one.controller;

import com.example.choose_one.common.Api;
import com.example.choose_one.model.VoteRequest;
import com.example.choose_one.service.VoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vote")
@RequiredArgsConstructor
@Slf4j
public class VoteApiController {
    private final VoteService voteService;

    @PostMapping("/create")
    @Operation(summary = "투표하기",description = "User가 투표할 때 사용하는 API")
    @Parameter(name = "voteOption",description = "A or B")
    public Api<String> create(@Valid @RequestBody VoteRequest voteRequest){
        return voteService.create(voteRequest);
    }

}
