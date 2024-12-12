package com.destilado_express.usuarioservice.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.destilado_express.usuarioservice.model.AuthRequest;
import com.destilado_express.usuarioservice.model.Usuario;
import com.destilado_express.usuarioservice.service.auth.JwtService;
import com.destilado_express.usuarioservice.service.user.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Debería autenticar al usuario y devolver un token")
    void testLoginSuccess() throws Exception {
        // Datos de prueba
        AuthRequest authRequest = new AuthRequest();
        authRequest.setEmail("user@test.com");
        authRequest.setPassword("12345");
        Usuario user = new Usuario("Test", "user@test.com", "12345", "", null);

        // Mock del UsuarioService
        when(usuarioService.getUsuarioByEmail(anyString())).thenReturn(user);
        
        // Mock del AuthenticationManager
        Authentication authentication = new UsernamePasswordAuthenticationToken("user@example.com", null);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        // Mock del JwtService
        when(jwtService.generateToken(eq("user@example.com"), eq(user))).thenReturn("fake-jwt-token");

        // Realizar la solicitud POST
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake-jwt-token"));
    }

    @Test
    @DisplayName("Debería devolver UNAUTHORIZED si las credenciales son incorrectas")
    void testLoginFailure() throws Exception {
        // Datos de prueba
        AuthRequest authRequest = new AuthRequest();
        authRequest.setEmail("user@example.com");
        authRequest.setPassword("wrongpassword");

        // Mock del AuthenticationManager para lanzar una excepción
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Authentication failed"));

        // Realizar la solicitud POST
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Usuario o contraseña incorrectos"));
    }
}
