package com.example.predict_numbers.service;

import com.example.predict_numbers.dto.request.CreateUserRequest;
import com.example.predict_numbers.dto.request.PredictNumberRequest;
import com.example.predict_numbers.dto.response.PredictResponse;
import com.example.predict_numbers.dto.response.UserResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService {
    UserDetailsService userDetailsService();

    UserResponse createUser(CreateUserRequest createUserRequest);

    UserResponse getUser();

    List<UserResponse> leaderboard();

    UserResponse addTurn(String username);

    PredictResponse predict(PredictNumberRequest predictNumberRequest);

}
