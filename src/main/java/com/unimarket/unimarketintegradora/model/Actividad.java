package com.unimarket.unimarketintegradora.model;

import java.sql.Timestamp;

public class Actividad {
    private String usuario;
    private String correo;
    private String modulo;
    private String accion;
    private Timestamp fecha;

    public Actividad() {}

    public Actividad(String usuario, String correo, String modulo, String accion, Timestamp fecha) {
        this.usuario = usuario;
        this.correo = correo;
        this.modulo = modulo;
        this.accion = accion;
        this.fecha = fecha;
    }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getModulo() { return modulo; }
    public void setModulo(String modulo) { this.modulo = modulo; }

    public String getAccion() { return accion; }
    public void setAccion(String accion) { this.accion = accion; }

    public Timestamp getFecha() { return fecha; }
    public void setFecha(Timestamp fecha) { this.fecha = fecha; }
}