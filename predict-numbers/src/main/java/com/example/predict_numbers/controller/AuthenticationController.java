package com.example.predict_numbers.controller;

import com.example.predict_numbers.dto.request.SignInRequest;
import com.example.predict_numbers.dto.response.TokenResponse;
import com.example.predict_numbers.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Validated
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody SignInRequest signInRequest) {
        return ResponseEntity.ok(authenticationService.authenticate(signInRequest));
    }
}
