package com.example.predict_numbers.service;

import com.example.predict_numbers.dto.request.SignInRequest;
import com.example.predict_numbers.dto.response.TokenResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthenticationService {
    TokenResponse authenticate(SignInRequest signInRequest);

    TokenResponse refreshToken(HttpServletRequest request);

    String logout(HttpServletRequest request);
}
