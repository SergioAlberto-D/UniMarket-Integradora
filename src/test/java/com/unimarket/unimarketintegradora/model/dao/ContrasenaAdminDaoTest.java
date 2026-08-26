package com.unimarket.unimarketintegradora.model.dao;

import com.unimarket.unimarketintegradora.model.ContrasenaAdmin;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContrasenaAdminDaoTest {
    private ContrasenaAdminDao dao;
    private ContrasenaAdmin passAdminPrueba;
    private final int ID_ADMIN_TEST = 1;

    @BeforeEach
    void setUp() {
        this.dao = new ContrasenaAdminDao();
        this.passAdminPrueba = new ContrasenaAdmin(1, "hash_admin_123");
        this.passAdminPrueba.setIdContrasenaAdmin(1);
    }

    @Test
    void create() {
        Assertions.assertDoesNotThrow(() -> this.dao.create(this.passAdminPrueba), "La creación de la contraseña de administrador no debería lanzar excepciones SQL.");
    }

    @Test
    void getAll() {
        List<ContrasenaAdmin> lista = this.dao.getAll();
        Assertions.assertNotNull(lista, "La lista devuelta por getAll no debe ser nula.");
        Assertions.assertTrue(lista.isEmpty(), "El método getAll debe retornar una lista vacía por seguridad.");
    }

    @Test
    void getById() {
        Assertions.assertDoesNotThrow(() -> this.dao.getById(1), "La consulta de la contraseña por ID de administrador no debería lanzar excepciones SQL.");
    }

    @Test
    void update() {
        this.passAdminPrueba.setContrasenaHash("nuevo_hash_admin_456");
        Assertions.assertDoesNotThrow(() -> this.dao.update(this.passAdminPrueba), "La actualización de la contraseña de administrador no debería lanzar excepciones SQL.");
    }

    @Test
    void delete() {
        Assertions.assertDoesNotThrow(() -> this.dao.delete(1), "La eliminación de la contraseña de administrador no debería lanzar excepciones SQL.");
    }
}
