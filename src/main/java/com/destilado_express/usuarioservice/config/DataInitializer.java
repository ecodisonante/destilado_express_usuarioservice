package com.destilado_express.usuarioservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.destilado_express.usuarioservice.model.Rol;
import com.destilado_express.usuarioservice.model.Usuario;
import com.destilado_express.usuarioservice.repository.RolRepository;
import com.destilado_express.usuarioservice.repository.UsuarioRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Override
    public void run(String... args) throws Exception {

        // Crear datos parametricos
        Rol admin = new Rol(1L, "ADMIN");
        Rol cliente = new Rol(2L, "USER");

        // Poblar Roles
        if (rolRepository.count() == 0) {
            rolRepository.save(admin);
            rolRepository.save(cliente);
        }

        // Poblar Usuarios
        if (usuarioRepository.count() == 0) {
            usuarioRepository.save(new Usuario("Usuario Admin", "admin@ecodisonante.com", "Secret123", "Direccion Admin", admin));
            usuarioRepository.save(new Usuario("Usuario Test", "ecodisonante@gmail.com", "Secret123", "Direccion Test", cliente));
        }
    }
}
