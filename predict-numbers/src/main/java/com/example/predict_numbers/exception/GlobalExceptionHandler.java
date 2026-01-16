package com.example.predict_numbers.exception;

import com.example.predict_numbers.dto.response.ApiResponse;
import com.example.predict_numbers.util.enums.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.Objects;

import jakarta.validation.ConstraintViolation;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final String MIN_ATTRIBUTE = "min";

    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<ApiResponse<Void>> handleAppException(AppException ex) {
        ErrorCode errorCode = ex.getErrorCode();

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.<Void>builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String enumKey = ex.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        Map<String, Object> attributes = null;
        try {
            errorCode = ErrorCode.valueOf(enumKey);
            var constraintViolations = ex.getBindingResult().getFieldErrors().getFirst().unwrap(ConstraintViolation.class);
            attributes = constraintViolations.getConstraintDescriptor().getAttributes();
        } catch(IllegalArgumentException e) {
            log.warn("Validation error key [{}] not found in ErrorCode enum", enumKey);
        }
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.<Void>builder()
                        .code(errorCode.getCode())
                        .message(Objects.nonNull(attributes) ? mapAttribute(ex.getMessage(), attributes) : errorCode.getMessage())
                        .build());

    }

    private String mapAttribute(String message, Map<String, Object> attributes) {
        String minvalue = attributes.get(MIN_ATTRIBUTE).toString();
        return message.replace("{" + MIN_ATTRIBUTE + "}", minvalue);
    }
}