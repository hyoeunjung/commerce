package com.example.commerce.dto;

import com.example.commerce.entity.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderCreateRequest {

    @NotBlank(message = "수령인의 이름은 필수입니다.")
    private String recipientName;

    @NotBlank(message = "수령인의 전화번호는 필수입니다.")
    private String recipientPhone;

    @NotBlank(message = "배송주소는 필수입니다.")
    private String recipientAddress;

    @NotNull(message = "결제수단은 필수입니다.")
    private PaymentMethod paymentMethod;
}
