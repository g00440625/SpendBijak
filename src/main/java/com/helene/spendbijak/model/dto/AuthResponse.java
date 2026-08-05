package com.helene.spendbijak.model.dto;

import lombok.Data;

@Data
public class AuthResponse {
    public Long userId;
    public String token;
    public String email;
}
