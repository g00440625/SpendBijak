package com.helene.spendbijak.model.dto;

import lombok.Data;

@Data
public class AuthResponse {
    public String token;
    public String email;
}
