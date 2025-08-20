package com.bankingApp.authService.controller;

import com.bankingApp.authService.dto.LoginRequest;
import com.bankingApp.authService.dto.SignupRequest;
import com.bankingApp.authService.model.Role;
import com.bankingApp.authService.model.Staff;
import com.bankingApp.authService.repository.RoleRepository;
import com.bankingApp.authService.repository.StaffRepository;
import com.bankingApp.authService.service.UserDetailsImpl;
import com.bankingApp.authService.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/staff")
@Slf4j
public class StaffController {

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsImpl userDetailsImpl;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {

        if (staffRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("User already exists");
        }

        Role role = roleRepository.findByName(request.getRole())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        Staff staff = new Staff();
        staff.setUsername(request.getUsername());
        staff.setEmail(request.getEmail());
        staff.setPassword(passwordEncoder.encode(request.getPassword()));
        staff.setRole(request.getRole());
        staff.setRoleEntity(role);

        staff = staffRepository.save(staff);

        Set<GrantedAuthority> authorities = staff.getRoleEntity().getPermissions()
                .stream()
                .map(p -> new SimpleGrantedAuthority(p.getName()))
                .collect(Collectors.toSet());

        return ResponseEntity.ok(staff);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request){
        Map<String, String> response = new HashMap<>();
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            UserDetails userDetails = userDetailsImpl.loadUserByUsername(request.getUsername());
            String jwt = jwtUtils.generateToken(userDetails.getUsername(), userDetails.getAuthorities());
            response.put("token", jwt);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Exception occured while creating Authentication token ", e );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @GetMapping
    public List<Staff> listUsers() {
        return staffRepository.findAll();
    }

}
