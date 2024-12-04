package com.destilado_express.usuarioservice.controller;

import com.destilado_express.usuarioservice.model.Usuario;
import com.destilado_express.usuarioservice.service.auth.AuthService;
import com.destilado_express.usuarioservice.service.user.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private UsuarioService usuarioService;
    private AuthService authService;
    
    @Autowired
    public UsuarioController(UsuarioService usuarioService, AuthService authService) {
        this.usuarioService = usuarioService;
        this.authService = authService;
    }

    // Obtener todos los usuarios
    @GetMapping
    public ResponseEntity<List<Usuario>> obtenerTodosLosUsuarios() {
        List<Usuario> usuarios = usuarioService.getAllUsuarios();
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    // Obtener usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> obtenerUsuarioPorId(@PathVariable Long id) {
        // Verificar si el usuario es admin
        if (!authService.esAdmin()) {
            Usuario usuarioAutenticado = authService.getUsuarioAutenticado();
            if (usuarioAutenticado.getId().equals(id)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("No tienes permiso para ver otros usuarios");
            }
        }

        Usuario usuario = usuarioService.getUsuarioById(id);
        if (usuario != null) {
            return new ResponseEntity<>(usuario, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Recupearr contraseña
    @PostMapping("/recover")
    public ResponseEntity<String> recuperaContrasena(@RequestParam String email) {
        Usuario usuario = usuarioService.getUsuarioByEmail(email);
        if (usuario != null) {
            return ResponseEntity.ok()
                    .header("Content-Type", "text/plain")
                    .body("Revisa tu correo para restablecer tu contraseña.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .header("Content-Type", "text/plain")
                    .body("No se encontró ningún usuario con ese email.");
        }
    }

    // Obtener usuario por email
    @GetMapping("/email")
    public ResponseEntity<Usuario> obtenerUsuarioPorEmail(@RequestParam String email) {
        Usuario usuario = usuarioService.getUsuarioByEmail(email);
        if (usuario != null) {
            return new ResponseEntity<>(usuario, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Crear nuevo usuario
    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario usuario) {
        Usuario nuevoUsuario = usuarioService.crearUsuario(usuario);
        return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
    }

    // Actualizar usuario
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuarioActualizado) {
        // Verificar si el usuario es admin
        if (!authService.esAdmin()) {
            Usuario usuarioAutenticado = authService.getUsuarioAutenticado();
            if (usuarioAutenticado.getId().equals(id)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("No tienes permiso para actualizar otros usuarios");
            }
        }

        Usuario usuario = usuarioService.actualizarUsuario(id, usuarioActualizado);
        return usuario != null
                ? ResponseEntity.ok("Usuario actualizado")
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
    }

    // Eliminar un usuario por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
