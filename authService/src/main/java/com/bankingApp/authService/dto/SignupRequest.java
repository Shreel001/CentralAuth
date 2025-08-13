package com.bankingApp.authService.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
public class SignupRequest {
    private String username;
    private String password;
    private String email;
}

