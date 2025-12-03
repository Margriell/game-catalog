package com.gamecatalog.controller;

import com.gamecatalog.dto.auth.AuthResponse;
import com.gamecatalog.dto.auth.ChangePasswordRequest;
import com.gamecatalog.model.user.User;
import com.gamecatalog.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @GetMapping("/me")
    public ResponseEntity<AuthResponse> getMyProfile(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Nie znaleziono użytkownika"));

        return ResponseEntity.ok(AuthResponse.builder()
                .email(user.getEmail())
                .role(user.getRole().name())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .token(null)
                .message("Dane użytkownika: " + user.getFullName())
                .build());
    }

    @PatchMapping("/me")
    public ResponseEntity<String> updateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam String firstName,
            @RequestParam String lastName
    ) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Nie znaleziono użytkownika"));

        user.setFirstName(firstName);
        user.setLastName(lastName);
        userRepository.save(user);

        return ResponseEntity.ok("Profil zaktualizowany");
    }

    @PostMapping("/me/password")
    public ResponseEntity<String> changePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Nie znaleziono użytkownika"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stare hasło jest nieprawidłowe");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return ResponseEntity.ok("Hasło zostało zmienione");
    }
}