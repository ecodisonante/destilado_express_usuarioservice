package com.destilado_express.usuarioservice.service;

import com.destilado_express.usuarioservice.model.Usuario;
import com.destilado_express.usuarioservice.repository.UsuarioRepository;

import lombok.extern.java.Log;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Log
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // Buscar usuario por email
        log.info("Cargando usuario con email: " + email);

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> {
                    String message = String.format("Usuario con email %s no encontrado", email);
                    log.severe(message);
                    return new UsernameNotFoundException(message);
                });

        log.info("Usuario encontrado: " + usuario.getEmail() + "Rol: " + usuario.getRol().getNombre());

        // Convertir rol a GrantedAuthority "ROLE_ADMIN"
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().getNombre().toUpperCase());
        return new User(usuario.getEmail(), usuario.getPassword(), Collections.singletonList(authority));
    }
}