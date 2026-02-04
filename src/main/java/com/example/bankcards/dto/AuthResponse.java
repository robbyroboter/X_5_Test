package com.example.bankcards.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

    private String token;

    private String type = "Bearer";

    private Long expiration;

    private String username;

    private String[] roles;

    private String message;
}
