package com.helene.spendbijak.service;

import com.helene.spendbijak.config.JwtUtil;
import com.helene.spendbijak.model.dto.AuthRequest;
import com.helene.spendbijak.model.dto.AuthResponse;
import com.helene.spendbijak.model.entity.User;
import com.helene.spendbijak.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthResponse register(User user) {
        // hash the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // save to database
        userRepository.save(user);

        // generate token
        String token = jwtUtil.generateToken(user.getEmail());

        // build response
        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setEmail(user.getEmail());
        return response;
    }

    public AuthResponse login(AuthRequest request) {
        // find user by email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // check password matches
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        // generate token
        String token = jwtUtil.generateToken(user.getEmail());

        // build response
        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setEmail(user.getEmail());
        return response;
    }
}
