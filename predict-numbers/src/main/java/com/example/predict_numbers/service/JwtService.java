package com.example.predict_numbers.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String generateToken(UserDetails userDetails);

    String extractUsername(String token);

    boolean isValid(String token, UserDetails userDetails);
}
