package com.destilado_express.usuarioservice.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.destilado_express.usuarioservice.model.Usuario;
import com.destilado_express.usuarioservice.repository.UsuarioRepository;

import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder encoder;

    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario getUsuarioById(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public Usuario getUsuarioByEmail(String email) {
        return usuarioRepository.findByEmail(email).orElse(null);
    }

    public Usuario crearUsuario(Usuario usuario) {
        usuario.setPassword(encoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    }

    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    public Usuario actualizarUsuario(Long id, Usuario usuarioActualizado) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario != null) {
            usuario.setNombre(usuarioActualizado.getNombre());
            usuario.setEmail(usuarioActualizado.getEmail());
            usuario.setDireccion(usuarioActualizado.getDireccion());
            // Actualizar otros campos según sea necesario
            return usuarioRepository.save(usuario);
        }
        return null;
    }
}
