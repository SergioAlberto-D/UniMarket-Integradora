package com.unimarket.unimarketintegradora.model.dao;

import com.unimarket.unimarketintegradora.model.Categoria;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CategoriaDaoTest {
    private CategoriaDao dao;
    private Categoria categoriaPrueba;
    private final int ID_CATEGORIA_TEST = 1;

    @BeforeEach
    void setUp() {
        this.dao = new CategoriaDao();
        this.categoriaPrueba = new Categoria("Electrónica");
        this.categoriaPrueba.setIdCategoria(1);
    }

    @Test
    void create() {
        Assertions.assertDoesNotThrow(() -> this.dao.create(this.categoriaPrueba), "La creación de la categoría no debería lanzar excepciones SQL.");
    }

    @Test
    void getAll() {
        List<Categoria> lista = this.dao.getAll();
        Assertions.assertNotNull(lista, "La lista de categorías obtenida no debe ser nula.");
    }

    @Test
    void getById() {
        Assertions.assertDoesNotThrow(() -> this.dao.getById(1), "La búsqueda de categoría por ID no debería lanzar excepciones SQL.");
    }

    @Test
    void update() {
        this.categoriaPrueba.setCategoria("Electrónica y Cómputo");
        Assertions.assertDoesNotThrow(() -> this.dao.update(this.categoriaPrueba), "La actualización de la categoría no debería lanzar excepciones SQL.");
    }

    @Test
    void delete() {
        Assertions.assertDoesNotThrow(() -> this.dao.delete(1), "La eliminación de la categoría por ID no debería lanzar excepciones SQL.");
    }
}
