package com.example.commerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.commerce.dto.UserSignUpRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("회원가입 성공: 정상적인 요청")
    void signUp_success() throws Exception {
        // Given
        UserSignUpRequest request = new UserSignUpRequest();
        request.setEmail("testuser@example.com");
        request.setPassword("Password123");
        request.setUsername("테스트유저");

        // When
        mockMvc.perform(post("/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // Then
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("회원가입 실패: DTO 유효성 검사 실패")
    void signUp_fail_invalid_input() throws Exception {
        // Given (준비)
        UserSignUpRequest request = new UserSignUpRequest();
        request.setEmail("invalid-email"); // 유효하지 않은 이메일
        request.setPassword("123");         // 8자리 미만 비밀번호
        request.setUsername("");            // 공백 사용자 이름

        // When & Then
        mockMvc.perform(post("/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}