//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.unimarket.unimarketintegradora.model.dao;

import com.unimarket.unimarketintegradora.model.Administrador;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AdministradorDaoTest {
    private AdministradorDao dao;
    private Administrador adminPrueba;
    private final int ID_ADMIN_TEST = 1;

    @BeforeEach
    void setUp() {
        this.dao = new AdministradorDao();
        this.adminPrueba = new Administrador("Admin General", "admin@unimarket.edu.mx", 1, 1);
        this.adminPrueba.setIdAdmin(1);
    }

    @Test
    void create() {
        Assertions.assertDoesNotThrow(() -> this.dao.create(this.adminPrueba), "La creación del administrador no debería lanzar excepciones SQL.");
    }

    @Test
    void getAll() {
        List<Administrador> lista = this.dao.getAll();
        Assertions.assertNotNull(lista, "La lista de administradores obtenida no debe ser nula.");
    }

    @Test
    void getById() {
        Assertions.assertDoesNotThrow(() -> this.dao.getById(1), "La búsqueda de administrador por ID no debería lanzar excepciones SQL.");
    }

    @Test
    void update() {
        this.adminPrueba.setNombre("Admin Modificado");
        Assertions.assertDoesNotThrow(() -> this.dao.update(this.adminPrueba), "La actualización del administrador no debería lanzar excepciones SQL.");
    }

    @Test
    void delete() {
        Assertions.assertDoesNotThrow(() -> this.dao.delete(1), "La eliminación del administrador por ID no debería lanzar excepciones SQL.");
    }
}
