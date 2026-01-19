package com.example.predict_numbers.controller;

import com.example.predict_numbers.dto.request.CreateUserRequest;
import com.example.predict_numbers.dto.request.SignInRequest;
import com.example.predict_numbers.dto.response.ApiResponse;
import com.example.predict_numbers.dto.response.TokenResponse;
import com.example.predict_numbers.dto.response.UserResponse;
import com.example.predict_numbers.service.AuthenticationService;
import com.example.predict_numbers.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Validated
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(@Valid @RequestBody SignInRequest signInRequest) {
        return ResponseEntity.ok(ApiResponse.<TokenResponse>builder()
                        .code(HttpStatus.OK.value())
                        .data(authenticationService.authenticate(signInRequest))
                        .message("Successfully logged in")
                .build());
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenResponse>> refresh(HttpServletRequest request) {
        return ResponseEntity.ok(ApiResponse.<TokenResponse>builder()
                        .code(HttpStatus.OK.value())
                        .data(authenticationService.refreshToken(request))
                        .message("Successfully refreshed")
                .build());
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(@Valid @RequestBody CreateUserRequest createUserRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<UserResponse>builder()
                        .code(HttpStatus.CREATED.value())
                        .data(userService.createUser(createUserRequest))
                        .message("Successfully registered")
                .build());
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(HttpServletRequest request) {
        return ResponseEntity.ok(ApiResponse.<String>builder()
                        .code(HttpStatus.OK.value())
                        .data(this.authenticationService.logout(request))
                .build());
    }


}
