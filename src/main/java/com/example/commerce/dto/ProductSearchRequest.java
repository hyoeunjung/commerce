package com.example.commerce.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;


@Getter
@Setter
public class ProductSearchRequest {

    private String keyword;
    private Pageable pageable;
}
