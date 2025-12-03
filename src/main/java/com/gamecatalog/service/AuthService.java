package com.gamecatalog.service;

import com.gamecatalog.dto.auth.AuthRequest;
import com.gamecatalog.dto.auth.AuthResponse;
import com.gamecatalog.dto.auth.RegisterRequest;
import com.gamecatalog.model.user.User;
import com.gamecatalog.model.user.enums.Role;
import com.gamecatalog.model.user.enums.Status;
import com.gamecatalog.repository.UserRepository;
import com.gamecatalog.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if(userRepository.existsByEmail(request.getEmail())){
            return AuthResponse.builder()
                    .message("Adres email jest już zajęty")
                    .build();

        }
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .status(Status.ACTIVE)
                .build();

        userRepository.save(user);

        var jwtToken = jwtUtil.generateToken(user);

        return AuthResponse.builder()
                .token(jwtToken)
                .email(user.getEmail())
                .role(String.valueOf(user.getRole()))
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .message("Użytkownik zarejestrowany pomyślnie")
                .build();
        }

    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtUtil.generateToken(user);

        return AuthResponse.builder()
                .token(jwtToken)
                .email(user.getEmail())
                .role(String.valueOf(user.getRole()))
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .message("Logowanie pomyślne")
                .build();
    }
}
