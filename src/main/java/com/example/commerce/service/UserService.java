package com.example.commerce.service;

import com.example.commerce.dto.UserSignInRequest;
import com.example.commerce.dto.UserSignUpRequest;
import com.example.commerce.entity.User;
import com.example.commerce.repository.UserRepository;
import com.example.commerce.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

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

    public String signIn(UserSignInRequest userSignInRequest) {
        // 이메일로 사용자 찾기
        User user =userRepository.findByEmail(userSignInRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        //비밀번호 일치여부 확인
        if (!passwordEncoder.matches(userSignInRequest.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        //로그인 성공후 jwt 토큰 발급 및 반환
        return jwtTokenProvider.createToken(user.getEmail());
    }

}
