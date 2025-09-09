package com.example.commerce.service;

import com.example.commerce.dto.UserSignUpRequest;
import com.example.commerce.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("회원가입 성공: 정상적인 요청")
    void signUp_success() {
        //given
        UserSignUpRequest request = new UserSignUpRequest();
        request.setEmail("servicetest@test.com");
        request.setPassword("servicetest1");
        request.setUsername("servicetest");

        //when
        userService.signUp(request);

        //then
        assertTrue(userRepository.findByEmail("servicetest@test.com").isPresent());
    }

    @Test
    @DisplayName("회원가입 실패 : 이메일 중복 시 예외 발생")
    void signUp_fail_duplicateEmail() {
        // Given
        UserSignUpRequest request1 = new UserSignUpRequest();
        request1.setEmail("duplicate@example.com");
        request1.setPassword("Password123");
        request1.setUsername("user1");
        userService.signUp(request1);

        UserSignUpRequest request2 = new UserSignUpRequest();
        request2.setEmail("duplicate@example.com");
        request2.setPassword("AnotherPassword123");
        request2.setUsername("user2");


        assertThrows(IllegalArgumentException.class, () -> userService.signUp(request2));
    }


}