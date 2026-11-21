package com.learning.languageflow.controller;

import com.learning.languageflow.dto.ApiResponse;
import com.learning.languageflow.dto.AuthRequest;
import com.learning.languageflow.dto.AuthResponse;
import com.learning.languageflow.security.JwtService;
import com.learning.languageflow.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody AuthRequest request) {
        if (request.getUsername() == null || request.getPassword() == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Username and password are required."));
        }

        boolean authenticated = authService.authenticateUser(request.getUsername(), request.getPassword());
        if (authenticated) {
            // Generate signed JWT token for the authenticated user
            String token = jwtService.generateToken(request.getUsername().trim());
            AuthResponse data = new AuthResponse(true, request.getUsername().trim(), token, "Login successful");
            return ResponseEntity.ok(ApiResponse.ok("Login successful", data));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Invalid credentials"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@RequestBody AuthRequest request) {
        try {
            boolean registered = authService.registerUser(request.getUsername(), request.getPassword());
            if (registered) {
                // Also issue a signed JWT upon successful registration for immediate login
                String token = jwtService.generateToken(request.getUsername().trim());
                AuthResponse data = new AuthResponse(true, request.getUsername().trim(), token, "Registration successful");
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(ApiResponse.ok("Registration successful", data));
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.error("Registration failed. Username may already exist."));
            }
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }
}
