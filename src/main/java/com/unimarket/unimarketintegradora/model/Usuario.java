package com.unimarket.unimarketintegradora.model;

import java.sql.Date;
import java.sql.Timestamp;

public class Usuario {
    private String matricula;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String numeroCelular;
    private int idDivisionAcademicaFk;
    private Date fechaRegistro;
    private String correoInstitucional;
    private int idRolFk;
    private String estado;
    private String tokenRecuperacion;
    private Timestamp tokenExpiracion;
    private String fotoPerfil;
    private String fotoCredencialFrente;
    private String fotoCredencialReverso;

    public Usuario() {}

    public Usuario(String nombre, String apellidoPaterno, String apellidoMaterno, String numeroCelular, int idDivision, Object o, String correoInstitucional, int idRol, String estado) {
    }

    // Getters y Setters
    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidoPaterno() { return apellidoPaterno; }
    public void setApellidoPaterno(String apellidoPaterno) { this.apellidoPaterno = apellidoPaterno; }

    public String getApellidoMaterno() { return apellidoMaterno; }
    public void setApellidoMaterno(String apellidoMaterno) { this.apellidoMaterno = apellidoMaterno; }

    public String getNumeroCelular() { return numeroCelular; }
    public void setNumeroCelular(String numeroCelular) { this.numeroCelular = numeroCelular; }

    public int getIdDivisionAcademicaFk() { return idDivisionAcademicaFk; }
    public void setIdDivisionAcademicaFk(int idDivisionAcademicaFk) { this.idDivisionAcademicaFk = idDivisionAcademicaFk; }

    public Date getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(Date fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public String getCorreoInstitucional() { return correoInstitucional; }
    public void setCorreoInstitucional(String correoInstitucional) { this.correoInstitucional = correoInstitucional; }

    public int getIdRolFk() { return idRolFk; }
    public void setIdRolFk(int idRolFk) { this.idRolFk = idRolFk; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getTokenRecuperacion() { return tokenRecuperacion; }
    public void setTokenRecuperacion(String tokenRecuperacion) { this.tokenRecuperacion = tokenRecuperacion; }

    public Timestamp getTokenExpiracion() { return tokenExpiracion; }
    public void setTokenExpiracion(Timestamp tokenExpiracion) { this.tokenExpiracion = tokenExpiracion; }

    public String getFotoPerfil() { return fotoPerfil; }
    public void setFotoPerfil(String fotoPerfil) { this.fotoPerfil = fotoPerfil; }

    public String getFotoCredencialFrente() { return fotoCredencialFrente; }
    public void setFotoCredencialFrente(String fotoCredencialFrente) { this.fotoCredencialFrente = fotoCredencialFrente; }

    public String getFotoCredencialReverso() { return fotoCredencialReverso; }
    public void setFotoCredencialReverso(String fotoCredencialReverso) { this.fotoCredencialReverso = fotoCredencialReverso; }
}