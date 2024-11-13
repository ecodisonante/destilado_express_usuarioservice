package com.destilado_express.usuarioservice.model;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;
}
