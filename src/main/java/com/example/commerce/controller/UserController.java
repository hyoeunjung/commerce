package com.example.commerce.controller;

import com.example.commerce.dto.UserSignUpRequest;
import com.example.commerce.entity.User;
import com.example.commerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@RequestBody UserSignUpRequest userSignUpRequest) {
        userService.signUp(userSignUpRequest);
        return ResponseEntity.ok().build();
    }

}
