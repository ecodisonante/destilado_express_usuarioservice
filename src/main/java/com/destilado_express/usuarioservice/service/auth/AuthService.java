package com.destilado_express.usuarioservice.service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.destilado_express.usuarioservice.model.Usuario;
import com.destilado_express.usuarioservice.service.user.UsuarioService;

@Service
public class AuthService {
    
    @Autowired
    private UsuarioService usuarioService;

    // Método para verificar si el usuario autenticado es admin
    public boolean esAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
    }

    // Método para obtener el usuario autenticado
    public Usuario getUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return usuarioService.getUsuarioByEmail(authentication.getName());
    }
}
