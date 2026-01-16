package com.example.predict_numbers.service.impl;

import com.example.predict_numbers.dto.request.SignInRequest;
import com.example.predict_numbers.dto.response.TokenResponse;
import com.example.predict_numbers.entity.User;
import com.example.predict_numbers.repository.UserRepository;
import com.example.predict_numbers.service.AuthenticationService;
import com.example.predict_numbers.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public TokenResponse authenticate(SignInRequest signInRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword()));

        User user = this.userRepository.findByUsername(signInRequest.getUsername()).orElseThrow(()-> new UsernameNotFoundException("username or password is incorrect"));

        String accessToken = this.jwtService.generateToken(user);
        return TokenResponse.builder()
                .accessToken("")
                .refreshToken("")
                .userId(user.getId())
                .build();
    }
}
