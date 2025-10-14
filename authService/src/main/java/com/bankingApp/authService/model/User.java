package com.bankingApp.authService.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "customers")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public class User extends BaseUser{ }
