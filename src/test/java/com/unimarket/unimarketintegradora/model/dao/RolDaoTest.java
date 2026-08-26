package com.unimarket.unimarketintegradora.model.dao;

import com.unimarket.unimarketintegradora.model.Rol;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RolDaoTest {
    private RolDao dao;
    private Rol rolPrueba;

    @BeforeEach
    void setUp() {
        this.dao = new RolDao();
        this.rolPrueba = new Rol("TEST_ROL");
    }

    @Test
    void create() {
        Assertions.assertDoesNotThrow(() -> this.dao.create(this.rolPrueba), "La creación del rol no debería lanzar excepciones SQL.");
    }

    @Test
    void getAll() {
        List<Rol> roles = this.dao.getAll();
        Assertions.assertNotNull(roles, "La lista de roles no debe ser nula.");
    }

    @Test
    void getById() {
        Assertions.assertDoesNotThrow(() -> this.dao.getById("1"), "La consulta de rol por ID no debería lanzar excepciones SQL.");
    }

    @Test
    void update() {
        this.rolPrueba.setIdRol(1);
        this.rolPrueba.setNombreRol("TEST_ROL_ACTUALIZADO");
        Assertions.assertDoesNotThrow(() -> this.dao.update(this.rolPrueba), "La actualización del rol no debería lanzar excepciones SQL.");
    }

    @Test
    void delete() {
        Assertions.assertDoesNotThrow(() -> this.dao.delete("9999"), "La eliminación del rol no debería lanzar excepciones SQL.");
    }
}