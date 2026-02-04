package com.example.bankcards.controller;

import com.example.bankcards.dto.AuthRequest;
import com.example.bankcards.dto.AuthResponse;
import com.example.bankcards.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Set the jwtExpiration field using reflection
        try {
            java.lang.reflect.Field field = AuthController.class.getDeclaredField("jwtExpiration");
            field.setAccessible(true);
            field.set(authController, 3600000L);
        } catch (Exception e) {
            fail("Failed to set jwtExpiration field");
        }
    }

    @Test
    void login_Success() {
        // Arrange
        AuthRequest request = new AuthRequest();
        request.setUsername("testuser");
        request.setPassword("testpass");

        User userDetails = new User("testuser", "encodedpass",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtTokenProvider.generateToken(authentication)).thenReturn("mock-jwt-token");

        // Act
        ResponseEntity<AuthResponse> response = authController.login(request);

        // Assert
        assertNotNull(response.getBody());
        assertEquals("mock-jwt-token", response.getBody().getToken());
        assertEquals("testuser", response.getBody().getUsername());
        assertEquals(3600000L, response.getBody().getExpiration());
        assertArrayEquals(new String[]{"ROLE_USER"}, response.getBody().getRoles());
        assertNull(response.getBody().getMessage());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtTokenProvider).generateToken(authentication);
    }

    @Test
    void login_InvalidCredentials_ReturnsUnauthorized() {
        // Arrange
        AuthRequest request = new AuthRequest();
        request.setUsername("testuser");
        request.setPassword("wrongpass");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        // Act
        ResponseEntity<AuthResponse> response = authController.login(request);

        // Assert
        assertNotNull(response.getBody());
        assertEquals("testuser", response.getBody().getUsername());
        assertEquals("Неверный логин или пароль", response.getBody().getMessage());
        assertNull(response.getBody().getToken());
        assertNull(response.getBody().getRoles());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtTokenProvider, never()).generateToken(any());
    }
}
