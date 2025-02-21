package com.example.event_management.controllers;

import com.example.event_management.models.AuthRequest;
import com.example.event_management.models.AuthResponse;
import com.example.event_management.models.User;
import com.example.event_management.models.Role;
import com.example.event_management.security.JwtUtil;
import com.example.event_management.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    /**
     * Endpoint per la registrazione di un nuovo utente.
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userService.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body("Errore: Email già in uso!");
        }

        // Imposta il ruolo predefinito come USER se non specificato
        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }

        // Salva l'utente con password criptata
        User savedUser = userService.saveUser(user);

        return ResponseEntity.ok("Utente registrato con successo! ID: " + savedUser.getId());
    }

    /**
     * Endpoint per il login e generazione del token JWT.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            // Autenticazione con AuthenticationManager
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            // Recupera l'utente dal database
            User user = userService.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("Errore: Utente non trovato"));

            // Genera il token JWT includendo il ruolo dell'utente
            String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

            // Restituisce il token, l'ID utente e il ruolo
            return ResponseEntity.ok(new AuthResponse(user.getId(), user.getEmail(), user.getRole().name(), token));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Errore: Credenziali non valide!");
        }
    }
}
