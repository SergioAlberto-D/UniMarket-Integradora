package com.unimarket.unimarketintegradora.model.dao;

import com.unimarket.unimarketintegradora.model.Articulo;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ArticuloDaoTest {
    private ArticuloDao dao;
    private Articulo articuloPrueba;
    private final String MATRICULA_TEST = "TEST202601";
    private final String ID_ARTICULO_TEST = "1";

    @BeforeEach
    void setUp() {
        this.dao = new ArticuloDao();
        this.articuloPrueba = new Articulo("Calculadora Científica", new BigDecimal("350.00"), 1, "Calculadora en excelente estado para ingeniería.", "TEST202601");
        this.articuloPrueba.setIdArticulo(Integer.parseInt("1"));
    }

    @Test
    void create() {
        Assertions.assertDoesNotThrow(() -> this.dao.create(this.articuloPrueba), "La creación del artículo no debería lanzar excepciones SQL.");
    }

    @Test
    void getAll() {
        List<Articulo> lista = this.dao.getAll();
        Assertions.assertNotNull(lista, "La lista de artículos no debe ser nula.");
    }

    @Test
    void getById() {
        Assertions.assertDoesNotThrow(() -> this.dao.getById("1"), "La consulta del artículo por ID no debería lanzar excepciones SQL.");
    }

    @Test
    void update() {
        this.articuloPrueba.setNombre("Calculadora Científica Casio");
        this.articuloPrueba.setPrecio(new BigDecimal("300.00"));
        Assertions.assertDoesNotThrow(() -> this.dao.update(this.articuloPrueba), "La actualización del artículo no debería lanzar excepciones SQL.");
    }

    @Test
    void delete() {
        Assertions.assertDoesNotThrow(() -> this.dao.delete("1"), "La eliminación lógica del artículo no debería lanzar excepciones SQL.");
    }

    @Test
    void obtenerUltimoIdPorUsuario() {
        int ultimoId = this.dao.obtenerUltimoIdPorUsuario("TEST202601");
        Assertions.assertTrue(ultimoId >= -1, "El último ID retornado debe ser mayor o igual a -1.");
    }

    @Test
    void getDetallesCompletos() {
        Assertions.assertDoesNotThrow(() -> this.dao.getDetallesCompletos("1"), "La consulta de detalles completos no debería lanzar excepciones SQL.");
    }

    @Test
    void contarPorUsuario() {
        int contador = this.dao.contarPorUsuario("TEST202601");
        Assertions.assertTrue(contador >= 0, "El total de artículos del usuario debe ser mayor o igual a cero.");
    }

    @Test
    void filtrarArticulos() {
        List<Articulo> lista = this.dao.filtrarArticulos("Precio menor", 1, 1, new BigDecimal("100.00"), new BigDecimal("500.00"));
        Assertions.assertNotNull(lista, "La lista filtrada de artículos no debe ser nula.");
    }

    @Test
    void obtenerPorUsuarioYEstado() {
        List<Articulo> lista = this.dao.obtenerPorUsuarioYEstado("TEST202601", true);
        Assertions.assertNotNull(lista, "La lista de artículos por estado no debe ser nula.");
    }
}
