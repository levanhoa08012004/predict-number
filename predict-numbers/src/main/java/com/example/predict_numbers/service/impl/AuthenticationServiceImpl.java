package com.example.predict_numbers.service.impl;

import com.example.predict_numbers.configuration.UserPrincipal;
import com.example.predict_numbers.dto.request.SignInRequest;
import com.example.predict_numbers.dto.response.TokenResponse;
import com.example.predict_numbers.entity.Token;
import com.example.predict_numbers.entity.User;
import com.example.predict_numbers.exception.AppException;
import com.example.predict_numbers.repository.UserRepository;
import com.example.predict_numbers.service.AuthenticationService;
import com.example.predict_numbers.service.JwtService;
import com.example.predict_numbers.service.TokenService;
import com.example.predict_numbers.util.enums.ErrorCode;
import com.example.predict_numbers.util.enums.TokenType;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenService tokenService;

    @Override
    public TokenResponse authenticate(SignInRequest signInRequest) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signInRequest.getUsername(),
                        signInRequest.getPassword()
                )
        );
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        String accessToken = this.jwtService.generateAccessToken(principal);

        String refreshToken = this.jwtService.generateRefreshToken(principal);

        this.tokenService.saveToken(Token.builder()
                .username(principal.getUsername())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build());

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(principal.getId())
                .build();
    }

    @Override
    public TokenResponse refreshToken(HttpServletRequest request) {
        String refreshToken = request.getHeader("x-token");
        if(StringUtils.isBlank(refreshToken)){
           throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        final String username = this.jwtService.extractUsername(refreshToken, TokenType.REFRESH_TOKEN);

        Optional<User> user = this.userRepository.findByUsername(username);
        if(!jwtService.isValid(refreshToken, TokenType.REFRESH_TOKEN,user.get())){
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
        UserPrincipal principal = new UserPrincipal(user.get().getId(), user.get().getUsername(), user.get().getEmail(), user.get().getPassword(), user.get().getAuthorities());

        String accessToken  = this.jwtService.generateAccessToken(principal);
        Token token = this.tokenService.getByUsername(user.get().getUsername());
        token.setAccessToken(accessToken);
        this.tokenService.saveToken(token);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.get().getId())
                .build();
    }

    @Override
    public String logout(HttpServletRequest request) {
        final String authorization = request.getHeader("Authorization");

        if(StringUtils.isBlank(authorization) || !authorization.startsWith("Bearer ")) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
        final String accesstoken = authorization.substring("Bearer ".length());

        final String username = this.jwtService.extractUsername(accesstoken, TokenType.ACCESS_TOKEN);

        Token token = this.tokenService.getByUsername(username);

        this.tokenService.deleteToken(token);

        return "logged out successfully";
    }


}
