package com.banking.accountService.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;

    @Enumerated(EnumType.STRING)
    private TransactionType type; // DEPOSIT, WITHDRAWAL, TRANSFER

    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private BankAccount bankAccount;

    private String description;
}
