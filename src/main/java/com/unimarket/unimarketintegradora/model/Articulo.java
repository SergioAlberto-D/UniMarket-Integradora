package com.unimarket.unimarketintegradora.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Articulo {
    private int idArticulo;
    private int idUsuario;
    private String titulo;
    private String descripcion;
    private BigDecimal precio;
    private String categoria;
    private String carrera;
    private String lugarEncuentro;
    private String estado;
    private Timestamp fechaPublicacion;
    private String imagenPrincipal;

    public Articulo() {
    }

    public Articulo(int idUsuario, String titulo, String descripcion, BigDecimal precio,
                    String categoria, String carrera, String lugarEncuentro) {
        this.idUsuario = idUsuario;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoria = categoria;
        this.carrera = carrera;
        this.lugarEncuentro = lugarEncuentro;
    }

    public int getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(int idArticulo) {
        this.idArticulo = idArticulo;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public String getLugarEncuentro() {
        return lugarEncuentro;
    }

    public void setLugarEncuentro(String lugarEncuentro) {
        this.lugarEncuentro = lugarEncuentro;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Timestamp getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(Timestamp fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public String getImagenPrincipal() {
        return imagenPrincipal;
    }

    public void setImagenPrincipal(String imagenPrincipal) {
        this.imagenPrincipal = imagenPrincipal;
    }
}