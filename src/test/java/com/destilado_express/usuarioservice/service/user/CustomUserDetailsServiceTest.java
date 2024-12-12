package com.destilado_express.usuarioservice.service.user;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import com.destilado_express.usuarioservice.model.Rol;
import com.destilado_express.usuarioservice.model.Usuario;
import com.destilado_express.usuarioservice.repository.UsuarioRepository;

class CustomUserDetailsServiceTest {

    @Mock
    private UsuarioRepository repository;

    @InjectMocks
    private CustomUserDetailsService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUserByUsername() {
        var rol = new Rol(1L, "USER");
        var usuario = new Usuario("Test 1", "email1@test.com", "12345", "Test Dir 1", rol);
        usuario.setId(1L);

        when(repository.findByEmail("email1@test.com")).thenReturn(Optional.of(usuario));
        var result = service.loadUserByUsername("email1@test.com");

        assertInstanceOf(UserDetails.class, result);
    }

}
