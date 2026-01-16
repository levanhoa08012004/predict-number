package com.example.predict_numbers.service;

import com.example.predict_numbers.dto.request.SignInRequest;
import com.example.predict_numbers.dto.response.TokenResponse;

public interface AuthenticationService {
    TokenResponse authenticate(SignInRequest signInRequest);
}
