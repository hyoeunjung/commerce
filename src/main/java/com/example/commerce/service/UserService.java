package com.example.commerce.service;

import com.example.commerce.dto.UserSignUpRequest;
import com.example.commerce.entity.User;
import com.example.commerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

        // 비밀번호 유효성
        if (userSignUpRequest.getPassword() == null || userSignUpRequest.getPassword().length() < 8) {
            throw new IllegalArgumentException("비밀번호는 영문+ 숫자 8자리 이상이어야 합니다.");
        }

        User user = new User();
        user.setEmail(userSignUpRequest.getEmail());
        user.setUsername(userSignUpRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userSignUpRequest.getPassword()));

        userRepository.save(user);

    }

}
