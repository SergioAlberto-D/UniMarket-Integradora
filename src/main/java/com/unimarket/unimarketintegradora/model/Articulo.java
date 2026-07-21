package com.unimarket.unimarketintegradora.model;

import java.math.BigDecimal;

public class Articulo {
    private int idArticulo;
    private String nombre;
    private BigDecimal precio;
    private int idCategoriaFk;
    private String descripcion;
    private String idUsuarioFk;
    private String imagenPrincipal;

    public Articulo() {}

    public Articulo(String nombre, BigDecimal precio, int idCategoriaFk, String descripcion, String idUsuarioFk) {
        this.nombre = nombre;
        this.precio = precio;
        this.idCategoriaFk = idCategoriaFk;
        this.descripcion = descripcion;
        this.idUsuarioFk = idUsuarioFk;
    }

    // Getters y Setters
    public int getIdArticulo() { return idArticulo; }
    public void setIdArticulo(int idArticulo) { this.idArticulo = idArticulo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    public int getIdCategoriaFk() { return idCategoriaFk; }
    public void setIdCategoriaFk(int idCategoriaFk) { this.idCategoriaFk = idCategoriaFk; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getIdUsuarioFk() { return idUsuarioFk; }
    public void setIdUsuarioFk(String idUsuarioFk) { this.idUsuarioFk = idUsuarioFk; }
    public String getImagenPrincipal() {
        return imagenPrincipal;
    }
    public void setImagenPrincipal(String imagenPrincipal) {
        this.imagenPrincipal = imagenPrincipal;
    }
}