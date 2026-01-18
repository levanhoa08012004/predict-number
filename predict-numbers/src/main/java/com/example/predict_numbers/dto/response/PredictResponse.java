package com.example.predict_numbers.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PredictResponse {
    String result;
    int secretNumber;
    int guessNumber;
    Long userId;
    String username;
    int score;
    int turns;
}
