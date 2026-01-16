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
    UNAUTHORIZED(1007, "YOU DO NOT HAVE PERMISSION", HttpStatus.FORBIDDEN),
    INVALID_DOB(1008, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
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
