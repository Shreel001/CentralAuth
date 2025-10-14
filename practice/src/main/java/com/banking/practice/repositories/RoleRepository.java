package com.banking.practice.repositories;

import com.banking.practice.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    boolean existsByRoleName(String roleName); // Use the correct naming convention
}