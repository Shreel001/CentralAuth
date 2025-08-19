package com.bankingApp.authService.repository;

import com.bankingApp.authService.model.Staff;
import com.bankingApp.authService.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StaffRepository extends JpaRepository<Staff, Long> {
    Optional<Staff> findByUsername(String username);
    boolean existsByUsername(String username);
}
