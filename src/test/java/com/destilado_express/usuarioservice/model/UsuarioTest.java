package com.destilado_express.usuarioservice.model;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

class UsuarioTest {

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuario = new Usuario("Test 1", "email1@test.com", "12345", "Test Dir 1", new Rol());
        usuario.setId(1L);
    }

    @Test
    void onCreateTest() {
        // arrange
        usuario = new Usuario();
        // act
        usuario.onCreate();

        // assert
        assertNotNull(usuario.getFechaCreacion());
        assertTrue(usuario.getFechaCreacion().isBefore(LocalDateTime.now())
                || usuario.getFechaCreacion().isEqual(LocalDateTime.now()));
    }

    @Test
    void onUpdateTest() {
        // arrange
        usuario = new Usuario();
        // act
        usuario.onUpdate();

        // assert
        assertNotNull(usuario.getFechaActualizacion());
        assertTrue(usuario.getFechaActualizacion().isBefore(LocalDateTime.now())
                || usuario.getFechaActualizacion().isEqual(LocalDateTime.now()));
    }

}
