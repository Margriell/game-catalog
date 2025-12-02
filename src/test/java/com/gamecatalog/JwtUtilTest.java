package com.gamecatalog;

import com.gamecatalog.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {
    private JwtUtil jwtUtil;

    //przykładowe dane
    private final String SECRET_KEY = "11111111222222222222222222221111111111111133333333333111111111111111111111111";
    private final long EXPIRATION_TIME = 1000 * 60 * 60;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secretKey", SECRET_KEY);
        ReflectionTestUtils.setField(jwtUtil, "jwtExpiration", EXPIRATION_TIME);
    }

    @Test
    void generateToken_ShouldCreateValidToken() {
        UserDetails user = new User("test@test.pl", "haslo1234", new ArrayList<>());
        String token = jwtUtil.generateToken(user);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void extractUsername_ShouldReturnCorrectEmail() {
        UserDetails user = new User("test@test.pl", "haslo1234", new ArrayList<>());
        String token = jwtUtil.generateToken(user);

        String extractedUsername = jwtUtil.extractUsername(token);

        assertEquals("test@test.pl", extractedUsername);
    }

    @Test
    void isTokenValid_ShouldReturnTrue_ForValidUserAndToken() {
        UserDetails user = new User("test@test.pl", "haslo1234", new ArrayList<>());
        String token = jwtUtil.generateToken(user);

        boolean isValid = jwtUtil.isTokenValid(token, user);

        assertTrue(isValid);
    }

    @Test
    void isTokenValid_ShouldReturnFalse_ForDifferentUser() {
        UserDetails user1 = new User("test1@test.pl", "haslo1234", new ArrayList<>());
        UserDetails user2 = new User("test2@test.pl", "haslo1234", new ArrayList<>());
        String token = jwtUtil.generateToken(user1);

        boolean isValid = jwtUtil.isTokenValid(token, user2);

        assertFalse(isValid);
    }
}
