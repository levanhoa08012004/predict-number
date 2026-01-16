package com.example.predict_numbers.service.impl;

import com.example.predict_numbers.repository.UserRepository;
import com.example.predict_numbers.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDetailsService userDetailsService() {
        return username -> this.userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("user not found"));
    }
}
