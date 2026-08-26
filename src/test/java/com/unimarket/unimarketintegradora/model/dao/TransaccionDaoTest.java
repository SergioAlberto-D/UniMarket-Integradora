package com.unimarket.unimarketintegradora.model.dao;

import com.unimarket.unimarketintegradora.model.Transaccion;
import com.unimarket.unimarketintegradora.model.TransaccionDTO;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TransaccionDaoTest {
    private TransaccionDao dao;
    private Transaccion transaccionPrueba;
    private final String MATRICULA_VENDEDOR_TEST = "TEST202601";
    private final String MATRICULA_COMPRADOR_TEST = "TEST202602";
    private final int ID_ARTICULO_TEST = 1;

    @BeforeEach
    void setUp() {
        this.dao = new TransaccionDao();
        this.transaccionPrueba = new Transaccion();
        this.transaccionPrueba.setIdArticuloFk(1);
        this.transaccionPrueba.setIdUsuarioVendedorFk("TEST202601");
        this.transaccionPrueba.setIdUsuarioCompradorFk("TEST202602");
        this.transaccionPrueba.setMontoFinal(new BigDecimal("150.00"));
        this.transaccionPrueba.setFechaTransaccion(new Date(System.currentTimeMillis()));
    }

    @Test
    void create() {
        Assertions.assertDoesNotThrow(() -> this.dao.create(this.transaccionPrueba), "La ejecución de la creación no debería lanzar excepciones.");
    }

    @Test
    void crearTransaccionPendiente() {
        Assertions.assertDoesNotThrow(() -> this.dao.crearTransaccionPendiente(1, "TEST202601", "TEST202602", new BigDecimal("250.00")), "La creación de la transacción pendiente no debería lanzar excepciones.");
    }

    @Test
    void getAll() {
        List<Transaccion> lista = this.dao.getAll();
        Assertions.assertNotNull(lista, "La lista de transacciones obtenida no debe ser nula.");
    }

    @Test
    void getById() {
        Assertions.assertDoesNotThrow(() -> this.dao.getById("1"), "La consulta por ID no debería generar excepciones SQL.");
    }

    @Test
    void update() {
        boolean resultado = this.dao.update(this.transaccionPrueba);
        Assertions.assertFalse(resultado, "El método update debe retornar false ya que las transacciones son históricas.");
    }

    @Test
    void delete() {
        boolean resultado = this.dao.delete("1");
        Assertions.assertFalse(resultado, "El método delete debe retornar false por políticas de auditoría.");
    }

    @Test
    void obtenerHistorialActividad() {
        List<TransaccionDTO> historial = this.dao.obtenerHistorialActividad("TEST202601");
        Assertions.assertNotNull(historial, "El historial de actividad devuelto no debe ser nulo.");
    }

    @Test
    void obtenerCorreoVendedor() {
        Assertions.assertDoesNotThrow(() -> this.dao.obtenerCorreoVendedor("TEST202601"), "La consulta de correo del vendedor no debe lanzar errores.");
    }

    @Test
    void obtenerTelefonoUsuario() {
        Assertions.assertDoesNotThrow(() -> this.dao.obtenerTelefonoUsuario("TEST202602"), "La consulta del teléfono del usuario no debe lanzar errores.");
    }

    @Test
    void contarVentasCompletadas() {
        int ventas = this.dao.contarVentasCompletadas("TEST202601");
        Assertions.assertTrue(ventas >= 0, "El conteo de ventas completadas debe ser un entero mayor o igual a cero.");
    }

    @Test
    void contarComprasCompletadas() {
        int compras = this.dao.contarComprasCompletadas("TEST202602");
        Assertions.assertTrue(compras >= 0, "El conteo de compras completadas debe ser un entero mayor o igual a cero.");
    }
}