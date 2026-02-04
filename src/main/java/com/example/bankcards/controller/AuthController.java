package com.example.bankcards.controller;

import com.example.bankcards.dto.AuthRequest;
import com.example.bankcards.dto.AuthResponse;
import com.example.bankcards.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            String token = jwtTokenProvider.generateToken(authentication);
            var userDetails = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

            AuthResponse response = AuthResponse.builder()
                    .token(token)
                    .expiration(jwtExpiration)
                    .username(userDetails.getUsername())
                    .roles(userDetails.getAuthorities().stream()
                            .map(Object::toString)
                            .toArray(String[]::new))
                    .build();

            return ResponseEntity.ok(response);

        } catch (org.springframework.security.authentication.BadCredentialsException ex) {
            return ResponseEntity.status(401).body(
                    AuthResponse.builder()
                            .username(request.getUsername())
                            .message("Неверный логин или пароль")
                            .build()
            );
        }
    }

}
