package com.destilado_express.usuarioservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String email;
    private String password;
    private String direccion;

    @ManyToOne
    @JoinColumn(name = "rol_id")
    private Rol rol;

    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    public Usuario(String nombre, String email, String password, String direccion, Rol rol) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.direccion = direccion;
        this.rol = rol;
    }

    @PrePersist
    public void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }
}
