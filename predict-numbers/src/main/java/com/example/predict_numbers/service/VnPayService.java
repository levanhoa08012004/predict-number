package com.example.predict_numbers.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

import java.io.UnsupportedEncodingException;

public interface VnPayService {
    String payment(Authentication authentication) throws UnsupportedEncodingException;
}
