package com.example.predict_numbers.controller;

import com.example.predict_numbers.dto.response.ApiResponse;
import com.example.predict_numbers.exception.AppException;
import com.example.predict_numbers.service.UserService;
import com.example.predict_numbers.service.VnPayService;
import com.example.predict_numbers.util.enums.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class VnPayController {
    private final VnPayService service;
    private final UserService userService;

    @GetMapping("/buy-turns")
    public ResponseEntity<ApiResponse<String>> payment(Authentication authentication) throws UnsupportedEncodingException {
        return ResponseEntity.ok(ApiResponse.<String>builder()
                        .code(HttpStatus.OK.value())
                        .data(service.payment(authentication))
                        .message("Create method payment success")
                .build());
    }
    @GetMapping("/vnpay-return")
    public ResponseEntity<ApiResponse<String>> handleReturn(HttpServletRequest request) {
        String txnRef = request.getParameter("vnp_TxnRef");
        String[] parts = txnRef.split("_");
        String username = parts[0];
        String responseCode = request.getParameter("vnp_ResponseCode");
        if ("00".equals(responseCode)) {
            userService.addTurn(username);
            return ResponseEntity.ok(ApiResponse.<String>builder()
                            .code(ErrorCode.PAYMENT_SUCCESS.getCode())
                            .message(ErrorCode.PAYMENT_SUCCESS.getMessage())
                    .build());
        } else {
            return ResponseEntity.ok(ApiResponse.<String>builder()
                    .code(ErrorCode.PAYMENT_FAIL.getCode())
                    .message(ErrorCode.PAYMENT_FAIL.getMessage())
                    .build());
        }

    }

}
