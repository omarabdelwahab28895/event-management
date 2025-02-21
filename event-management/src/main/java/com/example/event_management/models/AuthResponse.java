package com.example.event_management.models;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class AuthResponse {
    private Long userId;
    private String email;
    private String role;
    private String token;
}

