package com.example.choose_one.common.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Pagination {
    private Integer page;
    private Integer size;
    private Integer currentElements;
    private Integer totalPage;
    private Long totalElements;
}
