package com.unimarket.unimarketintegradora.model;

import java.math.BigDecimal;

public class Oferta {
    private int idOferta;
    private int idArticuloFk;
    private String matriculaUsuarioFk; // Es la matrícula del comprador
    private BigDecimal monto;
    private String estado; // Ejemplo: 'PENDIENTE', 'ACEPTADA', 'RECHAZADA'

    // Atributos extra para mostrar en listas de perfil si los ocupas luego
    private String nombreArticulo;
    private String imagenArticulo;
    private String nombreUsuario;

    public Oferta() {}

    public Oferta(int idArticuloFk, String matriculaUsuarioFk, BigDecimal monto, String estado) {
        this.idArticuloFk = idArticuloFk;
        this.matriculaUsuarioFk = matriculaUsuarioFk;
        this.monto = monto;
        this.estado = estado;
    }

    // --- GETTERS Y SETTERS ---
    public int getIdOferta() { return idOferta; }
    public void setIdOferta(int idOferta) { this.idOferta = idOferta; }

    public int getIdArticuloFk() { return idArticuloFk; }
    public void setIdArticuloFk(int idArticuloFk) { this.idArticuloFk = idArticuloFk; }

    public String getMatriculaUsuarioFk() { return matriculaUsuarioFk; }
    public void setMatriculaUsuarioFk(String matriculaUsuarioFk) { this.matriculaUsuarioFk = matriculaUsuarioFk; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getNombreArticulo() { return nombreArticulo; }
    public void setNombreArticulo(String nombreArticulo) { this.nombreArticulo = nombreArticulo; }

    public String getImagenArticulo() { return imagenArticulo; }
    public void setImagenArticulo(String imagenArticulo) { this.imagenArticulo = imagenArticulo; }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
}