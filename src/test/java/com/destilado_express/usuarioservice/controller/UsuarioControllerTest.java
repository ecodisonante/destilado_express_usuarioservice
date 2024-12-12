package com.destilado_express.usuarioservice.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.destilado_express.usuarioservice.model.Rol;
import com.destilado_express.usuarioservice.model.Usuario;
import com.destilado_express.usuarioservice.service.auth.AuthService;
import com.destilado_express.usuarioservice.service.user.UsuarioService;

class UsuarioControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private AuthService authService;

    @InjectMocks
    private UsuarioController usuarioController;

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
    void testObtenerTodosLosUsuarios() {
        var userList = List.of(usuario1, usuario2);
        when(usuarioService.getAllUsuarios()).thenReturn(userList);

        var result = usuarioController.obtenerTodosLosUsuarios();

        assertEquals(200, result.getStatusCode().value());
        assertEquals(userList, result.getBody());
    }

    @Test
    void testObtenerUsuarioPorIdAdminIdValido() {
        when(authService.esAdmin()).thenReturn(true);
        when(usuarioService.getUsuarioById(1L)).thenReturn(usuario1);

        var result1 = usuarioController.obtenerUsuarioPorId(1L);

        assertEquals(200, result1.getStatusCode().value());
        assertEquals(1L, ((Usuario) result1.getBody()).getId());
    }

    @Test
    void testObtenerUsuarioPorIdNoExistente() {
        when(authService.esAdmin()).thenReturn(true);
        when(usuarioService.getUsuarioById(1L)).thenReturn(usuario1);

        var result2 = usuarioController.obtenerUsuarioPorId(999L);

        assertEquals(404, result2.getStatusCode().value());
    }

    @Test
    void testObtenerUsuarioPorIdValido() {
        when(authService.esAdmin()).thenReturn(false);
        when(authService.getUsuarioAutenticado()).thenReturn(usuario1);
        when(usuarioService.getUsuarioById(1L)).thenReturn(usuario1);

        var result1 = usuarioController.obtenerUsuarioPorId(1L);

        assertEquals(200, result1.getStatusCode().value());
        assertEquals(1L, ((Usuario) result1.getBody()).getId());
    }

    @Test
    void testObtenerUsuarioPorIdNoPermitido() {
        when(authService.esAdmin()).thenReturn(false);
        when(authService.getUsuarioAutenticado()).thenReturn(usuario1);
        when(usuarioService.getUsuarioById(1L)).thenReturn(usuario1);

        var result2 = usuarioController.obtenerUsuarioPorId(999L);

        assertEquals(403, result2.getStatusCode().value());
        assertEquals("No tienes permiso para ver otros usuarios", result2.getBody());
    }

    @Test
    void testRecuperaContrasenaCorreoValido() {
        when(usuarioService.getUsuarioByEmail("email1@test.com")).thenReturn(usuario1);

        // Si el correo retorna un usuario valido
        var result1 = usuarioController.recuperaContrasena("email1@test.com");
        assertEquals(200, result1.getStatusCode().value());
        assertEquals("Revisa tu correo para restablecer tu contraseña.", result1.getBody());
    }

    @Test
    void testRecuperaContrasenaCorreoInvalido() {
        when(usuarioService.getUsuarioByEmail("email1@test.com")).thenReturn(usuario1);

        // Si el correo retorna un usuario valido
        var result = usuarioController.recuperaContrasena("invalid");
        assertEquals(404, result.getStatusCode().value());
        assertEquals("No se encontró ningún usuario con ese email.", result.getBody());
    }

    @Test
    void testObtenerUsuarioPorEmail() {
        when(usuarioService.getUsuarioByEmail("email1@test.com")).thenReturn(usuario1);

        var result = usuarioController.obtenerUsuarioPorEmail("email1@test.com");

        assertEquals(200, result.getStatusCode().value());
        assertEquals(usuario1, result.getBody());
    }

    @Test
    void testObtenerUsuarioPorEmailNotFound() {
        var result = usuarioController.obtenerUsuarioPorEmail("not found");

        assertEquals(404, result.getStatusCode().value());
    }

    @Test
    void testCrearUsuario() {
        when(usuarioService.crearUsuario(usuario1)).thenReturn(usuario1);

        var result = usuarioController.crearUsuario(usuario1);

        assertEquals(201, result.getStatusCode().value());
        assertEquals(usuario1, result.getBody());
    }

    @Test
    void testActualizarUsuario() {
        when(authService.esAdmin()).thenReturn(false);
        when(authService.getUsuarioAutenticado()).thenReturn(usuario1);
        when(usuarioService.actualizarUsuario(1L, usuario1)).thenReturn(usuario1);

        var result = usuarioController.actualizarUsuario(1L, usuario1);

        assertEquals(200, result.getStatusCode().value());
        assertEquals("Usuario actualizado", result.getBody());
    }

    @Test
    void testActualizarUsuarioNoPermitido() {
        when(authService.esAdmin()).thenReturn(false);
        when(authService.getUsuarioAutenticado()).thenReturn(usuario1);

        var result = usuarioController.actualizarUsuario(999L, usuario1);

        assertEquals(403, result.getStatusCode().value());
        assertEquals("No tienes permiso para actualizar otros usuarios", result.getBody());
    }

    @Test
    void testActualizarUsuarioNoEncontrado() {
        when(authService.esAdmin()).thenReturn(true);
        when(usuarioService.getUsuarioById(1L)).thenReturn(usuario1);

        var result = usuarioController.actualizarUsuario(1L, usuario1);

        assertEquals(404, result.getStatusCode().value());
        assertEquals("Usuario no encontrado", result.getBody());
    }

    @Test
    void testEliminarUsuario() {
        var result = usuarioController.eliminarUsuario(1L);

        verify(usuarioService, times(1)).eliminarUsuario(1L);
        assertEquals(204, result.getStatusCode().value());
    }

}
