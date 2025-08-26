package com.banking.accountService.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

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

    @Enumerated(EnumType.STRING)
    private AccountType account_type;   // CHECKING, SAVINGS, LOAN

    private Double balance;

    @Column(nullable = false)
    private Long userId; // Link to User in auth service (foreign key or via API)

    @OneToMany(mappedBy = "bankAccount", cascade = CascadeType.ALL)
    private List<TransactionType> transactions;

}
