package com.banking.practice.controller;

import com.banking.practice.dto.LoginRequest;
import com.banking.practice.dto.SignUpRequest;
import com.banking.practice.model.User;
import com.banking.practice.repositories.RoleRepository;
import com.banking.practice.repositories.UserRepository;
import com.banking.practice.service.UserDetailsImpl;
import com.banking.practice.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@Slf4j
public class UserController {

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private PasswordEncoder passwordEncoder;

    private AuthenticationManager authenticationManager;

    private UserDetailsImpl userDetailsImpl;

    private JwtUtils jwtUtils;

    @PostMapping("/signup")
    public ResponseEntity<?> userSignUp(SignUpRequest request){

        if(userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("User already exists");
        }

        if(roleRepository.existsByRoleName(request.getRole())){
            throw new RuntimeException("Invalid role");
        }

        User user = new User();
        request.setFirstName(request.getFirstName());
        request.setLastName(request.getLastName());
        request.setEmail(request.getEmail());
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        request.setRole(request.getRole());

        user = userRepository.save(user);

        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@RequestBody LoginRequest request){
        Map<String, Object> response = new HashMap<>();

        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            Optional<User> userData = userRepository.findByEmail(request.getEmail());
            UserDetails userDetails = userDetailsImpl.loadUserByUsername(request.getEmail());
            String jwt = jwtUtils.generateToken(userDetails.getUsername(), userDetails.getAuthorities());

            response.put("firstname", userData.get().getFirstName());
            response.put("lastName", userData.get().getLastName());
            response.put("email", userData.get().getEmail());
            response.put("role", userData.get().getRole());
            response.put("token",jwt);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Exception occured while creating Authentication token ", e );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

    }

}
