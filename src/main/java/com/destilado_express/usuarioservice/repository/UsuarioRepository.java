package com.destilado_express.usuarioservice.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.destilado_express.usuarioservice.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // buscar usuario por email
    Optional<Usuario> findByEmail(String email);
}
