package com.unimarket.unimarketintegradora.model.dao;

import com.unimarket.unimarketintegradora.model.DivisionAcademica;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DivisionAcademicaDaoTest {
    private DivisionAcademicaDao dao;
    private DivisionAcademica divisionPrueba;
    private final int ID_DIVISION_TEST = 1;

    @BeforeEach
    void setUp() {
        this.dao = new DivisionAcademicaDao();
        this.divisionPrueba = new DivisionAcademica("Tecnologías de la Información");
        this.divisionPrueba.setIdDivisionAcademica(1);
    }

    @Test
    void create() {
        Assertions.assertDoesNotThrow(() -> this.dao.create(this.divisionPrueba), "La creación de la división académica no debería lanzar excepciones SQL.");
    }

    @Test
    void getAll() {
        List<DivisionAcademica> lista = this.dao.getAll();
        Assertions.assertNotNull(lista, "La lista de divisiones académicas obtenida no debe ser nula.");
    }

    @Test
    void getById() {
        Assertions.assertDoesNotThrow(() -> this.dao.getById(1), "La búsqueda de división académica por ID no debería lanzar excepciones SQL.");
    }

    @Test
    void update() {
        this.divisionPrueba.setDivisionAcademica("Tecnologías de la Información y Comunicación");
        Assertions.assertDoesNotThrow(() -> this.dao.update(this.divisionPrueba), "La actualización de la división académica no debería lanzar excepciones SQL.");
    }

    @Test
    void delete() {
        Assertions.assertDoesNotThrow(() -> this.dao.delete(1), "La eliminación de la división académica por ID no debería lanzar excepciones SQL.");
    }
}