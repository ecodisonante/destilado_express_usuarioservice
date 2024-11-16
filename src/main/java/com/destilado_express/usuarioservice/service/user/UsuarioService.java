package com.destilado_express.usuarioservice.service.user;

import com.destilado_express.usuarioservice.model.Usuario;
import java.util.List;

public interface UsuarioService {

    List<Usuario> getAllUsuarios();

    Usuario getUsuarioById(Long id);

    Usuario getUsuarioByEmail(String email);

    Usuario crearUsuario(Usuario usuario);

    void eliminarUsuario(Long id);

    Usuario actualizarUsuario(Long id, Usuario usuarioActualizado);
}
