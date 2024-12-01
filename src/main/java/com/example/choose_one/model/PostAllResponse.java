package com.example.choose_one.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)

public class PostAllResponse {
    private Long postId;
    private String title;
    private String contentA;
    private String contentB;
    private Long totalVotes;
}
