package com.example.predict_numbers.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PredictNumberRequest {

    @NotNull(message = "{PREDICT_NUMBER_REQUIRED}")
    @Min(value = 1, message = "{PREDICT_NUMBER_MIN}")
    @Max(value = 5, message = "{PREDICT_NUMBER_MAX}")
    Integer guessNumber;
}
