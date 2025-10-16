package com.banking.practice.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
public class SignupRequestDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
