package com.unimarket.unimarketintegradora.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class TransaccionDTO {
    private String tipo;
    private String tituloArticulo;
    private BigDecimal precio;
    private Timestamp fecha;
    private String nombreContraparte;

    public TransaccionDTO(String tipo, String tituloArticulo, BigDecimal precio, Timestamp fecha, String nombreContraparte) {
        this.tipo = tipo;
        this.tituloArticulo = tituloArticulo;
        this.precio = precio;
        this.fecha = fecha;
        this.nombreContraparte = nombreContraparte;
    }

    public String getTipo() { return tipo; }
    public String getTituloArticulo() { return tituloArticulo; }
    public BigDecimal getPrecio() { return precio; }
    public Timestamp getFecha() { return fecha; }
    public String getNombreContraparte() { return nombreContraparte; }
}