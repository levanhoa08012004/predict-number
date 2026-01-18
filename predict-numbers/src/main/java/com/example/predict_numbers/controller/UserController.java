package com.example.predict_numbers.controller;

import com.example.predict_numbers.dto.request.PredictNumberRequest;
import com.example.predict_numbers.dto.response.ApiResponse;
import com.example.predict_numbers.dto.response.PredictResponse;
import com.example.predict_numbers.dto.response.UserResponse;
import com.example.predict_numbers.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Validated
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getUser() {
        return ResponseEntity.ok(ApiResponse.<UserResponse>builder()
                        .code(HttpStatus.OK.value())
                        .data(this.userService.getUser())
                        .message("get me success")
                .build());
    }


    @GetMapping("/leaderboard")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getLeaderboard(Authentication authentication) {
        return ResponseEntity.ok(ApiResponse.<List<UserResponse>>builder()
                        .code(HttpStatus.OK.value())
                        .data(this.userService.leaderboard())
                        .message("Get top 10 users with highest scores successfully")
                .build());
    }



    @PreAuthorize("hasAuthority('USER:PREDICT')")
    @PostMapping("/guess")
    public ResponseEntity<ApiResponse<PredictResponse>> predictNumber(@Valid @RequestBody PredictNumberRequest predictNumberRequest) {
        return ResponseEntity.ok(ApiResponse.<PredictResponse>builder()
                        .code(HttpStatus.OK.value())
                        .data(this.userService.predict(predictNumberRequest))
                        .message("Predict successfully")
                .build());
    }

}
