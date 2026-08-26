package com.unimarket.unimarketintegradora.model.dao;

import com.unimarket.unimarketintegradora.model.Comentario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ComentarioDaoTest {

    private ComentarioDao dao;
    private Comentario comentarioPrueba;
    private final String REMITENTE_TEST = "TEST202601";
    private final String RECEPTOR_TEST = "TEST202602";
    private final int ID_COMENTARIO_TEST = 1;

    @BeforeEach
    void setUp() {
        dao = new ComentarioDao();
        comentarioPrueba = new Comentario(
                "Excelente atención y entrega rápida.",
                5,
                REMITENTE_TEST,
                RECEPTOR_TEST
        );
        comentarioPrueba.setIdComentario(ID_COMENTARIO_TEST);
    }

    @Test
    void create() {
        assertDoesNotThrow(() -> dao.create(comentarioPrueba),
                "La creación del comentario no debería lanzar excepciones SQL.");
    }

    @Test
    void getById() {
        assertDoesNotThrow(() -> dao.getById(ID_COMENTARIO_TEST),
                "La consulta del comentario por ID no debería lanzar excepciones SQL.");
    }

    @Test
    void getAll() {
        List<Comentario> lista = dao.getAll();
        assertNotNull(lista, "La lista de comentarios obtenida no debe ser nula.");
    }

    @Test
    void update() {
        comentarioPrueba.setComentario("Comentario actualizado correctamente.");
        assertDoesNotThrow(() -> dao.update(comentarioPrueba),
                "La actualización del comentario no debería lanzar excepciones SQL.");
    }

    @Test
    void delete() {
        assertDoesNotThrow(() -> dao.delete(ID_COMENTARIO_TEST),
                "La eliminación del comentario no debería lanzar excepciones SQL.");
    }

    @Test
    void obtenerPorVendedor() {
        List<Comentario> lista = dao.obtenerPorVendedor(RECEPTOR_TEST);
        assertNotNull(lista, "La lista de comentarios del vendedor no debe ser nula.");
    }

    @Test
    void contarComentariosRealizados() {
        int contador = dao.contarComentariosRealizados(REMITENTE_TEST);
        assertTrue(contador >= 0, "El conteo de comentarios realizados debe ser mayor o igual a cero.");
    }
}