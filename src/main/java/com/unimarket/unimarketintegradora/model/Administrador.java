package com.unimarket.unimarketintegradora.model;

public class Administrador {
    private int idAdmin;
    private String nombre;
    private String correo;
    private int idDivisionAcademicaFk;
    private int idRolFk;

    public Administrador() {}

    public Administrador(String nombre, String correo, int idDivisionAcademicaFk, int idRolFk) {
        this.nombre = nombre;
        this.correo = correo;
        this.idDivisionAcademicaFk = idDivisionAcademicaFk;
        this.idRolFk = idRolFk;
    }

    // Getters y Setters
    public int getIdAdmin() { return idAdmin; }
    public void setIdAdmin(int idAdmin) { this.idAdmin = idAdmin; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public int getIdDivisionAcademicaFk() { return idDivisionAcademicaFk; }
    public void setIdDivisionAcademicaFk(int idDivisionAcademicaFk) { this.idDivisionAcademicaFk = idDivisionAcademicaFk; }
    public int getIdRolFk() { return idRolFk; }
    public void setIdRolFk(int idRolFk) { this.idRolFk = idRolFk; }
}