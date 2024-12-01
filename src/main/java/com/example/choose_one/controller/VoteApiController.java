package com.example.choose_one.controller;

import com.example.choose_one.common.Api;
import com.example.choose_one.model.VoteRequest;
import com.example.choose_one.service.VoteService;
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
    public Api<String> create(@Valid @RequestBody VoteRequest voteRequest){
        return voteService.create(voteRequest);
    }

}
