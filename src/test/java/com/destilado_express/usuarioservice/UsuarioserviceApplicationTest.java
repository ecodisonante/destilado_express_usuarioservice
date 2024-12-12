package com.destilado_express.usuarioservice;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class UsuarioserviceApplicationTest {
    
    @Test
    void contextLoads() {
        // Contexto se carga sin lanzar excepciones.
    }

    @Test
    void mainMethodTest() {
        UsuarioserviceApplication.main(new String[] {});
        assertTrue(Boolean.TRUE);
    }
}
