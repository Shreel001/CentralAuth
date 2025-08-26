package com.bankingApp.authService.controller;

import com.bankingApp.authService.dto.LoginRequest;
import com.bankingApp.authService.dto.SignupRequest;
import com.bankingApp.authService.dto.UserData;
import com.bankingApp.authService.model.Role;
import com.bankingApp.authService.model.User;
import com.bankingApp.authService.repository.RoleRepository;
import com.bankingApp.authService.repository.UserRepository;
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

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    @Autowired
    private UserRepository userRepository;

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

    @Autowired
    private UserData userData;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("User already exists");
        }

        Role role = roleRepository.findByName(request.getRole())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());        // store role name
        user.setRoleEntity(role);               // store FK role_id

        user = userRepository.save(user);

        Set<GrantedAuthority> authorities = user.getRoleEntity().getPermissions()
                .stream()
                .map(p -> new SimpleGrantedAuthority(p.getName()))
                .collect(Collectors.toSet());

        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest request){
        Map<String, Object> response = new HashMap<>();
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            userData.userData(request.getUsername());
            UserDetails userDetails = userDetailsImpl.loadUserByUsername(request.getUsername());
            String jwt = jwtUtils.generateToken(userDetails.getUsername(), userDetails.getAuthorities());
            response.put("token",jwt);
            response.put("user", userRepository.findByUsername(request.getUsername()));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Exception occured while creating Authentication token ", e );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @GetMapping
    public List<User> listUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{username}")
    public Optional<User> getUser(@PathVariable String username) {
        return userRepository.findByUsername(username);
    }

}
