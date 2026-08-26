package com.unimarket.unimarketintegradora.model.dao;

import com.unimarket.unimarketintegradora.model.ContrasenaUsuario;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContrasenaUsuarioDaoTest {
    private ContrasenaUsuarioDao dao;
    private ContrasenaUsuario passPrueba;
    private final String MATRICULA_TEST = "TEST202601";

    @BeforeEach
    void setUp() {
        this.dao = new ContrasenaUsuarioDao();
        this.passPrueba = new ContrasenaUsuario("TEST202601", "hash_encriptado_123");
        this.passPrueba.setIdContrasena(1);
    }

    @Test
    void create() {
        Assertions.assertDoesNotThrow(() -> this.dao.create(this.passPrueba), "La creación de la contraseña no debería lanzar excepciones SQL.");
    }

    @Test
    void getAll() {
        List<ContrasenaUsuario> lista = this.dao.getAll();
        Assertions.assertNotNull(lista, "La lista devuelta por getAll no debe ser nula.");
        Assertions.assertTrue(lista.isEmpty(), "El método getAll debe retornar una lista vacía por seguridad.");
    }

    @Test
    void getById() {
        Assertions.assertDoesNotThrow(() -> this.dao.getById("TEST202601"), "La consulta de la contraseña por matrícula no debería lanzar excepciones SQL.");
    }

    @Test
    void update() {
        this.passPrueba.setContrasenaHash("nuevo_hash_456");
        Assertions.assertDoesNotThrow(() -> this.dao.update(this.passPrueba), "La actualización de la contraseña no debería lanzar excepciones SQL.");
    }

    @Test
    void delete() {
        Assertions.assertDoesNotThrow(() -> this.dao.delete("TEST202601"), "La eliminación de la contraseña no debería lanzar excepciones SQL.");
    }
}
