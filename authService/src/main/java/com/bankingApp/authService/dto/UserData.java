package com.bankingApp.authService.dto;


import com.bankingApp.authService.model.User;
import com.bankingApp.authService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserData {

    @Autowired
    UserRepository userRepository;

    public Optional<User> userData(String username){
        System.out.println(userRepository.findByUsername(username));
        return userRepository.findByUsername(username);
    }

}
