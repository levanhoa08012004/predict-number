package com.example.predict_numbers.exception;

import com.example.predict_numbers.util.enums.ErrorCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppException extends RuntimeException{
    private ErrorCode errorCode;
    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
