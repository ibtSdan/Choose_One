package com.example.choose_one.controller;

import com.example.choose_one.common.api.Api;
import com.example.choose_one.model.VoteRequest;
import com.example.choose_one.service.VoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "성공",content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "1404",description = "User Not Found",content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "2404",description = "Post Not Found", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "3401",description = "중복투표 금지",content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "3402",description = "유효하지 않은 투표",content = @Content(mediaType = "application/json"))
    })
    @Parameter(name = "voteOption",description = "A or B")
    public Api<String> create(@Valid @RequestBody VoteRequest voteRequest){
        return voteService.create(voteRequest);
    }

}
