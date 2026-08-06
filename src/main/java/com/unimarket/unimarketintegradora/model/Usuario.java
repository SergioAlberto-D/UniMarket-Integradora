package com.unimarket.unimarketintegradora.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    private String idUsuario;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String numeroCelular;
    private int idDivisionAcademicaFk;
    private String carrera;
    private Date fechaRegistro;
    private String correoInstitucional;
    private int idRolFk;
    private String estado;
    private String tokenRecuperacion;
    private Timestamp tokenExpiracion;
    private String fotoPerfil;
    private String contrasena;

    // 1. Constructor Vacío
    public Usuario() {
    }

    // 2. CONSTRUCTOR DE 7 PARÁMETROS (Requerido tal cual por tu RegisterServlet)
    public Usuario(String nombre, String apellidoPaterno, String apellidoMaterno, String telefono, String carrera, String correo, String contrasena) {
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.numeroCelular = telefono;
        this.carrera = carrera;
        this.correoInstitucional = correo;
        this.contrasena = contrasena;

        // Intentar convertir la carrera a entero por si es un ID numérico
        try {
            this.idDivisionAcademicaFk = Integer.parseInt(carrera);
        } catch (NumberFormatException e) {
            this.idDivisionAcademicaFk = 1; // Valor por defecto
        }

        // Valores por defecto
        this.fechaRegistro = new Date(System.currentTimeMillis());
        this.idRolFk = 2; // Rol usuario/cliente por defecto
        this.estado = "ACTIVO";
    }

    // 3. Constructor completo original
    public Usuario(String nombre, String apellidoPaterno, String apellidoMaterno, String numeroCelular,
                   int idDivisionAcademicaFk, Date fechaRegistro, String correoInstitucional,
                   int idRolFk, String estado) {
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.numeroCelular = numeroCelular;
        this.idDivisionAcademicaFk = idDivisionAcademicaFk;
        this.fechaRegistro = fechaRegistro;
        this.correoInstitucional = correoInstitucional;
        this.idRolFk = idRolFk;
        this.estado = estado;
    }



    public String getNombres() {
        return this.nombre;
    }

    public String getTelefono() {
        return this.numeroCelular;
    }

    public String getInicial() {
        if (this.nombre != null && !this.nombre.trim().isEmpty()) {
            return this.nombre.trim().substring(0, 1).toUpperCase();
        }
        return "U";
    }

    // --- GETTERS Y SETTERS ---

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getNumeroCelular() {
        return numeroCelular;
    }

    public void setNumeroCelular(String numeroCelular) {
        this.numeroCelular = numeroCelular;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public int getIdDivisionAcademicaFk() {
        return idDivisionAcademicaFk;
    }

    public void setIdDivisionAcademicaFk(int idDivisionAcademicaFk) {
        this.idDivisionAcademicaFk = idDivisionAcademicaFk;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getCorreoInstitucional() {
        return correoInstitucional;
    }

    public void setCorreoInstitucional(String correoInstitucional) {
        this.correoInstitucional = correoInstitucional;
    }

    public int getIdRolFk() {
        return idRolFk;
    }

    public void setIdRolFk(int idRolFk) {
        this.idRolFk = idRolFk;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTokenRecuperacion() {
        return tokenRecuperacion;
    }

    public void setTokenRecuperacion(String tokenRecuperacion) {
        this.tokenRecuperacion = tokenRecuperacion;
    }

    public Timestamp getTokenExpiracion() {
        return tokenExpiracion;
    }

    public void setTokenExpiracion(Timestamp tokenExpiracion) {
        this.tokenExpiracion = tokenExpiracion;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}