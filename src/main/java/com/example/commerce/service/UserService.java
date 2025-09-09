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

        User user = new User();
        user.setEmail(userSignUpRequest.getEmail());
        user.setUsername(userSignUpRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userSignUpRequest.getPassword()));

        userRepository.save(user);

    }

}
