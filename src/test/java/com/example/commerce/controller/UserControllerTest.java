package com.example.commerce.controller;

import com.example.commerce.dto.UserSignInRequest;
import com.example.commerce.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.commerce.dto.UserSignUpRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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

    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
        UserSignUpRequest signUpRequest = new UserSignUpRequest();
        signUpRequest.setEmail("test123@test.com");
        signUpRequest.setPassword("password123");
        signUpRequest.setUsername("테스트유저");

        try{
            userService.signUp(signUpRequest);
        }catch (IllegalArgumentException e){

        }
    }

    //회원가입테스트
    @Test
    @DisplayName("회원가입 성공: 정상적인 요청")
    void signUp_success() throws Exception {
        // Given
        UserSignUpRequest request = new UserSignUpRequest();
        request.setEmail("testuser@example.com");
        request.setPassword("Password123");
        request.setUsername("새로운유저");

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

    //로그인테스트
    @Test
    @DisplayName("로그인성공 : 유효한 요청시 ok와 토큰 반환")
    void signIn_success() throws Exception {
        //given
        UserSignInRequest request = new UserSignInRequest();
        request.setEmail("test123@test.com");
        request.setPassword("password123");

        //when then
        mockMvc.perform(post("/users/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("로그인실패 : 유효하지않은 DTO")
    void signIn_fail_invalid_input() throws Exception {
        //given
        UserSignInRequest request = new UserSignInRequest();
        request.setEmail("invalid-email");
        request.setPassword("");

        //when then
        mockMvc.perform(post("/users/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("로그인실패 :비밀번호 불일치")
    void signIn_fail_wrong_password() throws Exception {
        //given
        UserSignInRequest request = new UserSignInRequest();
        request.setEmail("test123@test.com");
        request.setPassword("wrongpassword1");

        //when then
        mockMvc.perform(post("/users/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("비밀번호가 일치하지 않습니다."));
    }


}