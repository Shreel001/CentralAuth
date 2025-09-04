package com.banking.accountService.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="bank_account")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public class BankAccount  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String accountNumber;

    @Column(nullable = false)
    private Long userId; // Link to User in auth service (foreign key or via API)

    @Enumerated(EnumType.STRING)
    private AccountType account_type;   // CHECKING, SAVINGS, LOAN

    private Double balance;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false)
    private Long branchCode;

}
