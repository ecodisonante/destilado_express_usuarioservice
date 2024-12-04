package com.destilado_express.usuarioservice.service.auth;

import com.destilado_express.usuarioservice.model.Usuario;

public interface JwtService {
    String generateToken(String username, Usuario user);

    boolean validateToken(String token);

    String extractUsername(String token);

    String extractRole(String token);
}
