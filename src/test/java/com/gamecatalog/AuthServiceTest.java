package com.gamecatalog;

import com.gamecatalog.dto.auth.AuthRequest;
import com.gamecatalog.dto.auth.AuthResponse;
import com.gamecatalog.dto.auth.RegisterRequest;
import com.gamecatalog.model.user.User;
import com.gamecatalog.repository.GameRepository;
import com.gamecatalog.repository.ReviewRepository;
import com.gamecatalog.repository.UserRepository;
import com.gamecatalog.security.JwtUtil;
import com.gamecatalog.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @Test
    void register_ShouldReturnError_WhenEmailAlreadyExists(){
        RegisterRequest request = new RegisterRequest();
        request.setEmail("zajety@email.com");

        //symulowanie ze email jest juz w bazie
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        AuthResponse response = authService.register(request);

        assertEquals("Email already exists", response.getMessage());
        //nie ma tokenu w takim razie
        assertNull(response.getToken());

        verify(userRepository, never()).save(any());
    }

    @Test
    void register_ShouldSaveUser_WhenEmailIsUnique(){
        RegisterRequest request = new RegisterRequest();
        request.setEmail("zajety@email.com");
        request.setPassword("haslo1234");
        request.setEmail("Imie");
        request.setEmail("Nazwisko");

        //symulowanie ze emaila nie ma w bazie
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(jwtUtil.generateToken(any(User.class))).thenReturn("fake-jwt-token");

        AuthResponse response = authService.register(request);

        assertNotNull(response.getToken());
        assertEquals("fake-jwt-token", response.getToken());
        assertEquals("User registered successfully", response.getMessage());

        //czy na pewno wywołano save tylko raz
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void login_ShouldGenerateToken_WhenCredentialsAreCorrect() {
        AuthRequest request = new AuthRequest();
        request.setEmail("jan@test.pl");
        request.setPassword("password");

        User user = new User();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken(user)).thenReturn("login-token");

        AuthResponse response = authService.login(request);

        assertEquals("login-token", response.getToken());
        assertEquals("Login successful", response.getMessage());
    }




















}
