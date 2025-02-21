package com.example.event_management.controllers;

import com.example.event_management.models.AuthRequest;
import com.example.event_management.models.AuthResponse;
import com.example.event_management.models.User;
import com.example.event_management.security.JwtUtil;
import com.example.event_management.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        User user = userService.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Password errata");
        }

        // Genera il token includendo il ruolo dell'utente
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        // Restituisce il token, l'ID utente e il ruolo
        return ResponseEntity.ok(new AuthResponse(user.getId(), user.getEmail(), user.getRole().name(), token));
    }
}
