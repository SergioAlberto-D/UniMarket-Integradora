package com.unimarket.unimarketintegradora.model.dao;

import com.unimarket.unimarketintegradora.model.Usuario;
import java.sql.Date;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UsuarioDaoTest {
    private UsuarioDao dao;
    private Usuario usuarioPrueba;
    private final String MATRICULA_TEST = "TEST202601";
    private final String CORREO_TEST = "test2026@utez.edu.mx";
    private final String TOKEN_TEST = "TOK1234567";

    @BeforeEach
    void setUp() {
        this.dao = new UsuarioDao();
        this.dao.eliminarPorCorreo("test2026@utez.edu.mx");
        this.usuarioPrueba = new Usuario();
        this.usuarioPrueba.setIdUsuario("TEST202601");
        this.usuarioPrueba.setNombre("Juan");
        this.usuarioPrueba.setApellidoPaterno("Pérez");
        this.usuarioPrueba.setApellidoMaterno("García");
        this.usuarioPrueba.setCorreoInstitucional("test2026@utez.edu.mx");
        this.usuarioPrueba.setNumeroCelular("7771234567");
        this.usuarioPrueba.setIdDivisionAcademicaFk(1);
        this.usuarioPrueba.setIdRolFk(1);
        this.usuarioPrueba.setEstado("PENDIENTE");
        this.usuarioPrueba.setFotoPerfil("default.png");
        this.usuarioPrueba.setFechaRegistro(new Date(System.currentTimeMillis()));
    }

    @Test
    void create() {
        boolean resultado = this.dao.create(this.usuarioPrueba);
        Assertions.assertTrue(resultado, "El usuario debería crearse correctamente en la base de datos.");
    }

    @Test
    void getAll() {
        List<Usuario> usuarios = this.dao.getAll();
        Assertions.assertNotNull(usuarios, "La lista de usuarios no debe ser nula.");
    }

    @Test
    void getById() {
        this.dao.create(this.usuarioPrueba);
        Usuario usuario = this.dao.getById("TEST202601");
        Assertions.assertNotNull(usuario, "Debería encontrar un usuario con la matrícula especificada.");
    }

    @Test
    void update() {
        this.dao.create(this.usuarioPrueba);
        this.usuarioPrueba.setNombre("Juan Actualizado");
        boolean resultado = this.dao.update(this.usuarioPrueba);
        Assertions.assertTrue(resultado, "El usuario debería actualizarse correctamente.");
    }

    @Test
    void delete() {
        this.dao.create(this.usuarioPrueba);
        boolean resultado = this.dao.delete("TEST202601");
        Assertions.assertTrue(resultado, "El usuario debería eliminarse por su matrícula.");
    }

    @Test
    void activarCuenta() {
        this.dao.create(this.usuarioPrueba);
        boolean resultado = this.dao.activarCuenta("test2026@utez.edu.mx");
        Assertions.assertTrue(resultado, "La cuenta de usuario debería cambiar a estado ACTIVO.");
    }

    @Test
    void existeCorreo() {
        this.dao.create(this.usuarioPrueba);
        boolean existe = this.dao.existeCorreo("test2026@utez.edu.mx");
        Assertions.assertTrue(existe, "El correo de prueba debería existir en la base de datos.");
    }

    @Test
    void buscarPorCorreo() {
        this.dao.create(this.usuarioPrueba);
        Usuario usuario = this.dao.buscarPorCorreo("test2026@utez.edu.mx");
        Assertions.assertNotNull(usuario, "Debería retornar el usuario correspondiente al correo.");
        Assertions.assertEquals("test2026@utez.edu.mx", usuario.getCorreoInstitucional());
    }

    @Test
    void eliminarPorCorreo() {
        this.dao.create(this.usuarioPrueba);
        boolean resultado = this.dao.eliminarPorCorreo("test2026@utez.edu.mx");
        Assertions.assertTrue(resultado, "El usuario debería eliminarse utilizando su correo.");
    }

    @Test
    void guardarTokenRecuperacion() {
        this.dao.create(this.usuarioPrueba);
        boolean resultado = this.dao.guardarTokenRecuperacion("test2026@utez.edu.mx", "TOK1234567");
        Assertions.assertTrue(resultado, "El token de recuperación debería guardarse exitosamente.");
    }

    @Test
    void validarToken() {
        this.dao.create(this.usuarioPrueba);
        this.dao.guardarTokenRecuperacion("test2026@utez.edu.mx", "TOK1234567");
        boolean esValido = this.dao.validarToken("test2026@utez.edu.mx", "TOK1234567");
        Assertions.assertTrue(esValido, "El token generado debería ser válido y no haber expirado.");
    }

    @Test
    void limpiarToken() {
        this.dao.create(this.usuarioPrueba);
        this.dao.guardarTokenRecuperacion("test2026@utez.edu.mx", "TOK1234567");
        boolean resultado = this.dao.limpiarToken("test2026@utez.edu.mx");
        Assertions.assertTrue(resultado, "El token de recuperación debería limpiarse correctamente.");
    }

    @Test
    void actualizarFotoPerfil() {
        this.dao.create(this.usuarioPrueba);
        boolean resultado = this.dao.actualizarFotoPerfil("TEST202601", "nueva_foto_perfil.png");
        Assertions.assertTrue(resultado, "La foto de perfil del usuario debería actualizarse.");
    }

    @Test
    void actualizarTelefono() {
        this.dao.create(this.usuarioPrueba);
        boolean resultado = this.dao.actualizarTelefono("TEST202601", "7779876543");
        Assertions.assertTrue(resultado, "El número telefónico del usuario debería actualizarse.");
    }

    @Test
    void actualizarRol() {
        this.dao.create(this.usuarioPrueba);
        boolean resultado = this.dao.actualizarRol("TEST202601", 2);
        Assertions.assertTrue(resultado, "El rol del usuario debería actualizarse al nuevo ID asignado.");
    }
}