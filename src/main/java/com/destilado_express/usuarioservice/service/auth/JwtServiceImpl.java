package com.destilado_express.usuarioservice.service.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import com.destilado_express.usuarioservice.model.Usuario;

@Service
public class JwtServiceImpl implements JwtService {

    private static final String SECRET_KEY = "Juro solemnemente que mis intenciones no son buenas";

    private SecretKey getSigningKey() {
        byte[] keyBytes = SECRET_KEY.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String generateToken(String username, Usuario user) {
        return Jwts.builder()
                .subject(username)
                .claim("role", user.getRol().getNombre())
                .claim("id", user.getId())
                .claim("name", user.getNombre())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 horas de validez
                .signWith(getSigningKey())
                .compact();
    }

    // Validar el token
    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Extraer el nombre de usuario (subject) del token
    @Override
    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // Método para extraer el rol del usuario desde el token
    @Override
    public String extractRole(String token) {
        return (String) Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role"); // Extraer el rol del payload
    }

    // Método para extraer el id del usuario desde el token
    public Long extractId(String token) {
       Map<String, Object> claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    
        // Convertir explícitamente a Long
        Object idObject = claims.get("id");
        if (idObject instanceof Integer) {
            return ((Integer) idObject).longValue();
        } else if (idObject instanceof Long) {
            return (Long) idObject;
        } else {
            throw new IllegalArgumentException("El campo 'id' no es un número válido");
        }
    }
}
