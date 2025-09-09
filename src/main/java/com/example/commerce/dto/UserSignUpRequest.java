package com.example.commerce.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignUpRequest {

    @NotBlank(message = "이메일은 필수입력 항목입니다.")
    @Email(message = "유효한 이메일 형식을 입력해주세요")
    private String email;

    @NotBlank(message = "비밀번호는 필수입력 항목입니다.")
    @Size(min = 8, message = "비밀번호는 8자리 이상이어야 합니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
            message = "비밀번호는 8자리 이상이며, 숫자와 영문을 포함해야 합니다.")
    private String password;

    @NotBlank(message = "사용자 이름은 필수입력항목입니다.")
    private String username;
}
