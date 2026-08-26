package com.unimarket.unimarketintegradora.model.dao;

import com.unimarket.unimarketintegradora.model.Oferta;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OfertaDaoTest {
    private OfertaDao dao;
    private Oferta ofertaPrueba;
    private final String MATRICULA_TEST = "TEST202601";
    private final int ID_ARTICULO_TEST = 1;
    private final int ID_OFERTA_TEST = 1;

    @BeforeEach
    void setUp() {
        this.dao = new OfertaDao();
        this.ofertaPrueba = new Oferta(1, "TEST202601", new BigDecimal("150.00"), "PENDIENTE");
        this.ofertaPrueba.setIdOferta(1);
    }

    @Test
    void create() {
        Assertions.assertDoesNotThrow(() -> this.dao.create(this.ofertaPrueba), "La creación de la oferta no debería lanzar excepciones SQL.");
    }

    @Test
    void getById() {
        Assertions.assertDoesNotThrow(() -> this.dao.getById(1), "La consulta de oferta por ID no debería lanzar excepciones SQL.");
    }

    @Test
    void getAll() {
        List<Oferta> lista = this.dao.getAll();
        Assertions.assertNotNull(lista, "La lista de ofertas obtenida no debe ser nula.");
    }

    @Test
    void update() {
        this.ofertaPrueba.setMonto(new BigDecimal("200.00"));
        Assertions.assertDoesNotThrow(() -> this.dao.update(this.ofertaPrueba), "La actualización de la oferta no debería lanzar excepciones SQL.");
    }

    @Test
    void delete() {
        Assertions.assertDoesNotThrow(() -> this.dao.delete(1), "La eliminación de la oferta no debería lanzar excepciones SQL.");
    }

    @Test
    void existeOfertaPrevia() {
        Assertions.assertDoesNotThrow(() -> this.dao.existeOfertaPrevia(1, "TEST202601"), "La comprobación de oferta previa no debería lanzar excepciones SQL.");
    }

    @Test
    void obtenerOfertasHechasPorUsuario() {
        List<Oferta> ofertas = this.dao.obtenerOfertasHechasPorUsuario("TEST202601");
        Assertions.assertNotNull(ofertas, "La lista de ofertas hechas por el usuario no debe ser nula.");
    }

    @Test
    void obtenerOfertasRecibidas() {
        List<Oferta> ofertas = this.dao.obtenerOfertasRecibidas("TEST202601");
        Assertions.assertNotNull(ofertas, "La lista de ofertas recibidas no debe ser nula.");
    }

    @Test
    void cambiarEstado() {
        Assertions.assertDoesNotThrow(() -> this.dao.cambiarEstado(1, "ACEPTADA"), "El cambio de estado de la oferta no debería lanzar excepciones SQL.");
    }

    @Test
    void obtenerDetalleParaNotificacion() {
        Assertions.assertDoesNotThrow(() -> this.dao.obtenerDetalleParaNotificacion(1), "La obtención del detalle para notificación no debería lanzar excepciones SQL.");
    }

    @Test
    void eliminarOferta() {
        Assertions.assertDoesNotThrow(() -> this.dao.eliminarOferta(1), "La eliminación de la oferta por método auxiliar no debería lanzar excepciones SQL.");
    }
}