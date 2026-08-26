package com.unimarket.unimarketintegradora.model.dao;

import com.unimarket.unimarketintegradora.model.Notificacion;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NotificacionDaoTest {
    private NotificacionDao dao;
    private final String MATRICULA_TEST = "TEST202601";

    @BeforeEach
    void setUp() {
        this.dao = new NotificacionDao();
    }

    @Test
    void crearNotificacion() {
        Assertions.assertDoesNotThrow(() -> this.dao.crearNotificacion("TEST202601", "Nueva oferta recibida para tu artículo", "OFERTA"), "La creación de la notificación no debería lanzar excepciones SQL.");
    }

    @Test
    void obtenerNoLeidas() {
        List<Notificacion> lista = this.dao.obtenerNoLeidas("TEST202601");
        Assertions.assertNotNull(lista, "La lista de notificaciones no leídas no debe ser nula.");
    }

    @Test
    void marcarTodasComoLeidas() {
        Assertions.assertDoesNotThrow(() -> this.dao.marcarTodasComoLeidas("TEST202601"), "El marcado de notificaciones como leídas no debería lanzar excepciones SQL.");
    }
}