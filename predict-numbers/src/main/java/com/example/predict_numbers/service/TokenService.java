package com.example.predict_numbers.service;

import com.example.predict_numbers.entity.Token;

public interface TokenService {
    long saveToken(Token token);

    void deleteToken(Token token);

    Token getByUsername(String username);
}
