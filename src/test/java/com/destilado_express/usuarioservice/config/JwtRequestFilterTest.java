package com.destilado_express.usuarioservice.config;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;

import com.destilado_express.usuarioservice.service.auth.JwtService;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

class JwtRequestFilterTest {

    private JwtRequestFilter jwtRequestFilter;

    @Mock
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtRequestFilter = new JwtRequestFilter(jwtService);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("El filtro debe ignorar rutas especificadas en shouldNotFilter")
    void testShouldNotFilter() throws ServletException, IOException {
        // Crear una solicitud a una ruta que debe ser ignorada
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/auth/login");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        // Llamar al método doFilterInternal
        jwtRequestFilter.doFilter(request, response, filterChain);

        // Verificar que el filtroChain continuó sin modificar el SecurityContext
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    @DisplayName("El filtro debe permitir acceso cuando el token JWT es válido")
    void testDoFilterInternalValidToken() throws ServletException, IOException {
        // Crear una solicitud a una ruta que requiere filtrado
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/recipes/1");
        request.addHeader("Authorization", "Bearer valid-token");

        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        // Configurar el jwtService mockeado
        when(jwtService.extractUsername("valid-token")).thenReturn("user@example.com");
        when(jwtService.extractRole("valid-token")).thenReturn("USER");
        when(jwtService.validateToken("valid-token")).thenReturn(true);

        // Llamar al método doFilterInternal
        jwtRequestFilter.doFilter(request, response, filterChain);

        // Verificar que el SecurityContext tiene la autenticación establecida
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals("user@example.com", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        assertTrue(SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    @DisplayName("El filtro debe rechazar acceso cuando el token JWT es inválido")
    void testDoFilterInternalInvalidToken() throws ServletException, IOException {
        // Crear una solicitud a una ruta que requiere filtrado
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/recipes/1");
        request.addHeader("Authorization", "Bearer invalid-token");

        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        // Configurar el jwtService mockeado
        when(jwtService.extractUsername("invalid-token")).thenReturn(null);
        when(jwtService.extractRole("invalid-token")).thenReturn(null);
        when(jwtService.validateToken("invalid-token")).thenReturn(false);

        // Llamar al método doFilterInternal
        jwtRequestFilter.doFilter(request, response, filterChain);

        // Verificar que el SecurityContext no tiene autenticación
        assertNull(SecurityContextHolder.getContext().getAuthentication());

        // Verificar que se estableció el código de estado 401 Unauthorized
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
    }

    @Test
    @DisplayName("El filtro debe rechazar acceso cuando el token JWT falta")
    void testDoFilterInternalMissingToken() throws ServletException, IOException {
        // Crear una solicitud sin encabezado de autorización
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/recipes/1");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        // Llamar al método doFilterInternal
        jwtRequestFilter.doFilter(request, response, filterChain);

        // Verificar que el SecurityContext no tiene autenticación
        assertNull(SecurityContextHolder.getContext().getAuthentication());

        // Verificar que se estableció el código de estado 401 Unauthorized
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
    }

}
