package com.example.commerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductCreateRequest {
    @NotBlank(message = "상품명은 필수입력 항목입니다.")
    private String name;

    @NotNull(message = "가격은 필수입력 항목입니다.")
    @Positive(message = "가격은 양수여야 합니다.")
    private Integer price;

    @NotNull(message = "재고은 필수입력 항목입니다.")
    @Positive(message = "재고은 양수여야 합니다.")
    private Integer stock;

    @NotBlank(message = "상품 설명은 필수입력 항목입니다.")
    private String description;



}
