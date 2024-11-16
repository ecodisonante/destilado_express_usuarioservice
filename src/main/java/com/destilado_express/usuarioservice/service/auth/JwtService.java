package com.destilado_express.usuarioservice.service.auth;

public interface JwtService {
    String generateToken(String username, String role);

    boolean validateToken(String token);

    String extractUsername(String token);

    String extractRole(String token);
}