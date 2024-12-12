package com.destilado_express.usuarioservice.service.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.destilado_express.usuarioservice.model.Rol;
import com.destilado_express.usuarioservice.model.Usuario;
import com.destilado_express.usuarioservice.repository.UsuarioRepository;

class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository repository;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private UsuarioServiceImpl service;

    private Usuario usuario1;
    private Usuario usuario2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        usuario1 = new Usuario("Test 1", "email1@test.com", "12345", "Test Dir 1", new Rol());
        usuario1.setId(1L);
        usuario2 = new Usuario("Test 2", "email2@test.com", "98765", "Test Dir 2", new Rol());
        usuario2.setId(2L);
    }

    @Test
    void testGetAllUsuarios() {
        var userList = List.of(usuario1, usuario2);
        when(repository.findAll()).thenReturn(userList);

        var result = service.getAllUsuarios();

        assertEquals(userList, result);
    }

    @Test
    void testGetUsuarioById() {
        when(repository.findById(1L)).thenReturn(Optional.of(usuario1));

        assertEquals(usuario1, service.getUsuarioById(1L));
        assertNull(service.getUsuarioById(999L));
    }

    @Test
    void testGetUsuarioByEmail() {
        when(repository.findByEmail("email1@test.com")).thenReturn(Optional.of(usuario1));

        assertEquals(usuario1, service.getUsuarioByEmail("email1@test.com"));
        assertNull(service.getUsuarioByEmail("notfoundemail@test.com"));
    }

    @Test
    void testCrearUsuario() {
        when(repository.save(usuario1)).thenReturn(usuario1);

        var result = service.crearUsuario(usuario1);

        assertEquals(usuario1.getEmail(), result.getEmail());
    }

    @Test
    void testActualizarUsuario() {
        when(repository.findById(1L)).thenReturn(Optional.of(usuario1));
        when(repository.save(usuario1)).thenReturn(usuario1);

        var result = service.actualizarUsuario(1L, usuario1);

        assertEquals(usuario1.getEmail(), result.getEmail());
        assertNull(service.actualizarUsuario(999L, usuario2));
    }

    @Test
    void testEliminarUsuario() {
        service.eliminarUsuario(1L);

        verify(repository, times(1)).deleteById(1L);

    }

}