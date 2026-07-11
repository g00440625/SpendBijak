package com.helene.spendbijak.controller;

import com.helene.spendbijak.model.dto.AuthRequest;
import com.helene.spendbijak.model.dto.AuthResponse;
import com.helene.spendbijak.model.entity.User;
import com.helene.spendbijak.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public AuthResponse register (@RequestBody User user){
        return authService.register(user);
    }

    @PostMapping("/login")
    public AuthResponse login (@RequestBody AuthRequest request){
        return authService.login(request);
    }
}
