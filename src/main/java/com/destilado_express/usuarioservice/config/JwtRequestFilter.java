package com.destilado_express.usuarioservice.config;

import java.io.IOException;

import java.util.List;
import java.util.Map;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.destilado_express.usuarioservice.service.auth.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // Excluir de la validación del token
        Map<String, String> ignoredRequests = Map.of(
                "/api/auth/login", "POST", // login
                "/api/usuarios", "POST", // registrarse
                "/api/usuarios/recover", "POST" // recuperar contraseña
        );

        String path = request.getRequestURI();
        String method = request.getMethod();

        return ignoredRequests.containsKey(path) && ignoredRequests.get(path).equalsIgnoreCase(method);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        String token = null;
        String username = null;
        String role = null;

        // Extraer el token JWT del encabezado Authorization
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
            username = jwtService.extractUsername(token);
            role = jwtService.extractRole(token);
        }

        // Token valido, contiuar
        if (username != null && role != null && jwtService.validateToken(token)) {
            // Agregar roles al usuario
            List<GrantedAuthority> authorities = Collections
                    .singletonList(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
                    null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            filterChain.doFilter(request, response);
        } else {
            // Token invalido, rechazar
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido");
        }
    }
}
