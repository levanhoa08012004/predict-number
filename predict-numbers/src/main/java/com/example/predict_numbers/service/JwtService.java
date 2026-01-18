package com.example.predict_numbers.service;

import com.example.predict_numbers.util.enums.TokenType;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

public interface JwtService {
    String generateAccessToken(UserDetails userDetails);

    String generateRefreshToken(UserDetails userDetails);

    String extractUsername(String token, TokenType type);

    Date extractExpiration(String token, TokenType type);

    boolean isValid(String token, TokenType type, UserDetails userDetails);
}
