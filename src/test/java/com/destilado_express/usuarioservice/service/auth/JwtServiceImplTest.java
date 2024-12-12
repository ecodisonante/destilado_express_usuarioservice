package com.destilado_express.usuarioservice.service.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.destilado_express.usuarioservice.model.Rol;
import com.destilado_express.usuarioservice.model.Usuario;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

class JwtServiceImplTest {

    private JwtServiceImpl jwtService;
    private static final String SECRET_KEY = "Juro solemnemente que mis intenciones no son buenas";
    private String token;

    @BeforeEach
    void setUp() {
        jwtService = new JwtServiceImpl();
        String username = "testuser";
        String role = "ROLE_USER";
        token = generateTestToken(username, role, 1L);
    }

    @Test
    void shouldGenerateToken() {
        // arrange
        var usuario = new Usuario("Test 1", "email1@test.com", "12345", "Test Dir 1", new Rol());
        usuario.setId(1L);
        // act & assert
        var result = jwtService.generateToken("user", usuario);
        assertInstanceOf(String.class, result);
    }

    @Test
    void shouldValidateTokenSuccessfully() {
        // arrange
        String invalidToken = "invalid.token.value";
        // act & assert
        assertTrue(jwtService.validateToken(token));
        assertFalse(jwtService.validateToken(invalidToken));
    }

    @Test
    void shouldExtractUsernameFromToken() {
        // arrange
        String username = "testuser";
        // act
        String extractedUsername = jwtService.extractUsername(token);
        // assert
        assertEquals(username, extractedUsername);
    }

    @Test
    void shouldExtractRoleFromToken() {
        // arrange
        String role = "ROLE_USER";
        // act
        String extractedRole = jwtService.extractRole(token);
        // assert
        assertEquals(role, extractedRole);
    }

    @Test
    void shouldExtractIntIdFromToken() {
        // int ID
        Long id = jwtService.extractId(token);
        assertEquals(1L, id);
    }

    @Test
    void shouldExtractLongIdFromToken() {
        // long ID
        token = generateTestToken("testuser", "ROLE_USER", 9999999999999L);
        Long id = jwtService.extractId(token);
        assertEquals(9999999999999L, id);
    }

    @Test
    void extractIdFromTokenFails() {
        // NaN ID
        token = generateTestToken("testuser", "ROLE_USER", "potato");
        assertThrows(IllegalArgumentException.class, () -> jwtService.extractId(token));
    }



    
    private String generateTestToken(String username, String role, Object id) {

        byte[] keyBytes = SECRET_KEY.getBytes();
        var sign = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.builder()
                .subject(username)
                .claim("role", role)
                .claim("id", id)
                .claim("name", "Test User")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 horas de validez
                .signWith(sign)
                .compact();
    }

}
