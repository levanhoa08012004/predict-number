package com.example.predict_numbers.exception;

import com.example.predict_numbers.dto.response.ApiResponse;
import com.example.predict_numbers.util.enums.ErrorCode;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

import jakarta.validation.ConstraintViolation;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final String MIN_ATTRIBUTE = "min";
    private static final String MAX_ATTRIBUTE = "max";
    private static final String VALUE_ATTRIBUTE = "value";


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
    @ExceptionHandler(ConstraintViolationException.class)
    public ApiResponse<Void> handleConstraintViolation(
            ConstraintViolationException ex,
            WebRequest request) {

        String message = ex.getConstraintViolations()
                .stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .findFirst()
                .orElse("Invalid parameter");

        return ApiResponse.<Void>builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(message)
                .build();
    }
    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFoundException(ResourceNotFoundException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.<Void>builder()
                        .message(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex) {

        var fieldError = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .orElseThrow();

        String rawMessage = fieldError.getDefaultMessage();

        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        String finalMessage = rawMessage;
        Map<String, Object> attributes = null;

        if (rawMessage.startsWith("{") && rawMessage.endsWith("}")) {
            String enumKey = rawMessage.substring(1, rawMessage.length() - 1);

            try {
                errorCode = ErrorCode.valueOf(enumKey);

                var constraintViolation = fieldError.unwrap(ConstraintViolation.class);
                attributes = constraintViolation
                        .getConstraintDescriptor()
                        .getAttributes();

                finalMessage = mapAttribute(errorCode.getMessage(), attributes);

            } catch (IllegalArgumentException e) {
                log.warn("Validation error key [{}] not found in ErrorCode enum", enumKey);
            }
        }

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.<Void>builder()
                        .code(errorCode.getCode())
                        .message(finalMessage)
                        .build());
    }


    private String mapAttribute(String message, Map<String, Object> attributes) {

        if (attributes.containsKey(MIN_ATTRIBUTE)) {
            message = message.replace(
                    "{" + MIN_ATTRIBUTE + "}",
                    attributes.get(MIN_ATTRIBUTE).toString()
            );
        }

        if (attributes.containsKey(MAX_ATTRIBUTE)) {
            message = message.replace(
                    "{" + MAX_ATTRIBUTE + "}",
                    attributes.get(MAX_ATTRIBUTE).toString()
            );
        }

        if (attributes.containsKey(VALUE_ATTRIBUTE)) {
            String value = attributes.get(VALUE_ATTRIBUTE).toString();

            message = message.replace("{" + MIN_ATTRIBUTE + "}", value);
            message = message.replace("{" + MAX_ATTRIBUTE + "}", value);
        }

        return message;
    }



}