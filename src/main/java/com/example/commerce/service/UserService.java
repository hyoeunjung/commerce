package com.example.commerce.service;

import com.example.commerce.dto.UserSignInRequest;
import com.example.commerce.dto.UserSignUpRequest;
import com.example.commerce.entity.Role;
import com.example.commerce.entity.User;
import com.example.commerce.repository.UserRepository;
import com.example.commerce.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

        //Builder 패턴을 사용해서 User 객체 생성
        User user = User.builder()
                .email(userSignUpRequest.getEmail())
                .password(passwordEncoder.encode(userSignUpRequest.getPassword()))
                .username(userSignUpRequest.getUsername())
                .roles(Collections.singletonList(Role.USER))
                .build();




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
        return jwtTokenProvider.createToken(user.getEmail(),
                user.getRoles()
                .stream()
                .map(Enum::name)
                .collect(Collectors.toList()));
    }

    //테스트용 admin 계정
    @Transactional
    public void createAdminAccount() {
        if (userRepository.findByEmail("admin@example.com").isEmpty()) {
            User admin = User.builder()
                    .email("admin@example.com")
                    .username("정효은")
                    .password(passwordEncoder.encode("adminpassword1"))
                    .roles(List.of(Role.USER, Role.ADMIN)) //
                    .build();
            userRepository.save(admin);
        }
    }

}
