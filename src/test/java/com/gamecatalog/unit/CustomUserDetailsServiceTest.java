package com.gamecatalog.unit;

import com.gamecatalog.model.user.User;
import com.gamecatalog.model.user.enums.Role;
import com.gamecatalog.model.user.enums.Status;
import com.gamecatalog.repository.UserRepository;
import com.gamecatalog.security.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void loadUserByUsername_ShouldReturnUserDetails_WhenUserExists() {
        String email = "test@test.com";
        User user = new User();
        user.setEmail(email);
        user.setPassword("password");
        user.setRole(Role.USER);
        user.setStatus(Status.ACTIVE);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        UserDetails result = customUserDetailsService.loadUserByUsername(email);

        assertNotNull(result);
        assertEquals(email, result.getUsername());
    }

    @Test
    void loadUserByUsername_ShouldThrowException_WhenUserDoesNotFound() {
        String email = "nieznany@email.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        //spodziewamy się usernameNotFoundException
        assertThrows(UsernameNotFoundException.class, () -> customUserDetailsService.loadUserByUsername(email));
    }

    @Test
    void loadUserByUsername_ShouldMapAuthCorrectly_WhenUserIsAdmin() {
        String email = "admin@email.com";
        User adminUser = new User();
        adminUser.setEmail(email);
        adminUser.setPassword("password");

        adminUser.setRole(Role.ADMIN);
        adminUser.setStatus(Status.ACTIVE);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(adminUser));

        UserDetails result = customUserDetailsService.loadUserByUsername(email);

        assertNotNull(result);

        assertTrue(result.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE: ADMIN")));
    }
}
