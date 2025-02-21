package com.example.event_management.models;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class AuthRequest {
    private String email;
    private String password;
}
