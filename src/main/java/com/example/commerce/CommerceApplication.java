package com.example.commerce;

import com.example.commerce.entity.User;
import com.example.commerce.entity.Role;
import com.example.commerce.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import java.util.Set;
import java.util.Optional;
import java.util.Collections;
import java.util.HashSet;

@EnableJpaAuditing
@SpringBootApplication
@RequiredArgsConstructor
public class CommerceApplication {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(CommerceApplication.class, args);
	}

	@PostConstruct
	public void createAdminAccount() {
		// Checks if an admin account already exists
		Optional<User> existingAdmin = userRepository.findByEmail("admin@example.com");

		if (existingAdmin.isEmpty()) {
			User admin = User.builder()
					.email("admin@example.com")
					.password(passwordEncoder.encode("adminpassword1"))
					.username("관리자")
					.roles(Collections.singletonList(Role.ADMIN))
					.build();
			userRepository.save(admin);
			System.out.println("관리자 계정이 성공적으로 생성되었습니다.");
		} else {
			System.out.println("관리자 계정이 이미 존재합니다.");
		}
	}
}