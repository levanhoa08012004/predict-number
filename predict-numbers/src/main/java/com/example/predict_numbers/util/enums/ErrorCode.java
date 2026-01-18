package com.example.predict_numbers.util.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    NCATEGORIZED_EXCEPTION(9999, "Uncategoried error", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EXISTED(1002, "User exitsted", HttpStatus.BAD_REQUEST),
    INVALID_KEY(1001, "Invalid message key", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username must be a least {min} character", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "Password must be a least {min} character", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User not exitsted", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    FORBIDDEN(1007, "you do not have permission", HttpStatus.FORBIDDEN),
    INVALID_DOB(1008, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(1009, "Invalid token", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL(1010,"Invalid email", HttpStatus.BAD_REQUEST),
    PREDICT_NUMBER_REQUIRED(1011, "Predict number not null", HttpStatus.BAD_REQUEST),
    PREDICT_NUMBER_MIN(1012, "Predict number must be a least {max}", HttpStatus.BAD_REQUEST),
    PREDICT_NUMBER_MAX(1013, "Predict number must be a greater {min}", HttpStatus.BAD_REQUEST),
    PREDICT_NUMBER_NO_TURN(1016,"Predict number has not been turned", HttpStatus.BAD_REQUEST),
    PAYMENT_SUCCESS(1017,"Payment successful", HttpStatus.OK),
    PAYMENT_FAIL(1018,"Payment failed", HttpStatus.OK),
    ;

    private int code;
    private String message;
    private HttpStatus httpStatus;
    ErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
