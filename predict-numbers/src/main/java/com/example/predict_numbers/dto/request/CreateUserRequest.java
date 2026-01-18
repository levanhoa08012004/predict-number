package com.example.predict_numbers.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateUserRequest {
    @Size(min = 4, message = "{USERNAME_INVALID}")
    @NotBlank(message = "username must not be empty")
    String username;

    @Size(min = 6, message = "{INVALID_PASSWORD}")
    @NotBlank(message = "password must not be empty")
    String password;

    @NotBlank(message = "email must not be empty")
    @Email(message = "{INVALID_EMAIL}")
    String email;
}
