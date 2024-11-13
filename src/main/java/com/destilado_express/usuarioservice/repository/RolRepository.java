package com.destilado_express.usuarioservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.destilado_express.usuarioservice.model.Rol;

public interface RolRepository extends JpaRepository<Rol, Long> {

}
