package com.example.predict_numbers.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "tokens")
public class Token extends BaseEntity {

    @Column(name = "username", unique=true)
    String username;

    @Column(name = "access_token")
    String accessToken;

    @Column(name = "refresh_token")
    String refreshToken;
}
