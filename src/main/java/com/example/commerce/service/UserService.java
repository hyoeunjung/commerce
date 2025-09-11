package com.example.commerce.service;

import com.example.commerce.dto.UserSignUpRequest;
import com.example.commerce.entity.User;
import com.example.commerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUp(UserSignUpRequest userSignUpRequest) {
        //이메일 중복 확인
        if (userRepository.findByEmail(userSignUpRequest.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 사용중인 이메일입니다.");
        }

        //Builder 패턴을 사용해서 User 객체 생성
        User user = User.builder()
                .email(userSignUpRequest.getEmail())
                .password(passwordEncoder.encode(userSignUpRequest.getPassword()))
                .username(userSignUpRequest.getUsername())
                .roles(Collections.singletonList("USER"))
                .build();




        userRepository.save(user);

    }

}
