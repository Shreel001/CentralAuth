package com.bankingApp.authService.service;

import com.bankingApp.authService.model.BaseUser;
import com.bankingApp.authService.repository.StaffRepository;
import com.bankingApp.authService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDetailsImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        BaseUser baseUser = userRepository.findByUsername(username)
                .map(u -> (BaseUser) u)
                .orElseGet(() -> staffRepository.findByUsername(username)
                        .map(s -> (BaseUser) s)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username)));

        return buildUserDetails(baseUser);
    }

    private UserDetails buildUserDetails(BaseUser authUser) {
        Set<GrantedAuthority> authorities = authUser.getRoleEntity().getPermissions()
                .stream()
                .map(p -> new SimpleGrantedAuthority(p.getName()))
                .collect(Collectors.toSet());

        return org.springframework.security.core.userdetails.User.builder()
                .username(authUser.getUsername())
                .password(authUser.getPassword())
                .authorities(authorities)
                .build();
    }
}
