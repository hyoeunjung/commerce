package com.example.commerce;

import com.example.commerce.entity.User;
import com.example.commerce.entity.Role;
import com.example.commerce.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.Collections;

import org.slf4j.Logger;


@SpringBootApplication
@RequiredArgsConstructor
public class CommerceApplication {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	private static final Logger log = LoggerFactory.getLogger(CommerceApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(CommerceApplication.class, args);
	}

	@PostConstruct
	public void createAdminAccount() {

		Optional<User> existingAdmin = userRepository.findByEmail("admin@example.com");

		if (existingAdmin.isEmpty()) {
			User admin = User.builder()
					.email("admin@example.com")
					.password(passwordEncoder.encode("adminpassword1"))
					.username("관리자")
					.roles(Collections.singletonList(Role.ADMIN))
					.build();
			userRepository.save(admin);
			log.info("관리자 계정이 성공적으로 생성");
		} else {
			log.info("관리자 계정이 이미 존재함");
		}
	}
}