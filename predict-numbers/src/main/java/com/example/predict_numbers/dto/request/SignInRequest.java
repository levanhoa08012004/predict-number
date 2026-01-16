package com.example.predict_numbers.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SignInRequest {
    @NotBlank(message = "username must be not null")
    String username;

    @NotBlank(message = "password must be not null")
    String password;
}
