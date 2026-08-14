package com.unimarket.unimarketintegradora.model;

import java.math.BigDecimal;
import java.sql.Date;

public class Transaccion {
    private int idTransaccion;
    private int idArticuloFk;
    private String idUsuarioVendedorFk;
    private String idUsuarioCompradorFk;
    private BigDecimal montoFinal;
    private Date fechaTransaccion;

    public Transaccion() {}

    public Transaccion(int idArticuloFk, String idUsuarioVendedorFk, String idUsuarioCompradorFk, BigDecimal montoFinal, Date fechaTransaccion) {
        this.idArticuloFk = idArticuloFk;
        this.idUsuarioVendedorFk = idUsuarioVendedorFk;
        this.idUsuarioCompradorFk = idUsuarioCompradorFk;
        this.montoFinal = montoFinal;
        this.fechaTransaccion = fechaTransaccion;
    }

    // Getters y Setters (Omitidos por brevedad, generarlos en tu IDE)
    public int getIdTransaccion() { return idTransaccion; }
    public void setIdTransaccion(int idTransaccion) { this.idTransaccion = idTransaccion; }
    public int getIdArticuloFk() { return idArticuloFk; }
    public void setIdArticuloFk(int idArticuloFk) { this.idArticuloFk = idArticuloFk; }
    public String getIdUsuarioVendedorFk() { return idUsuarioVendedorFk; }
    public void setIdUsuarioVendedorFk(String idUsuarioVendedorFk) { this.idUsuarioVendedorFk = idUsuarioVendedorFk; }
    public String getIdUsuarioCompradorFk() { return idUsuarioCompradorFk; }
    public void setIdUsuarioCompradorFk(String idUsuarioCompradorFk) { this.idUsuarioCompradorFk = idUsuarioCompradorFk; }
    public BigDecimal getMontoFinal() { return montoFinal; }
    public void setMontoFinal(BigDecimal montoFinal) { this.montoFinal = montoFinal; }
    public Date getFechaTransaccion() { return fechaTransaccion; }
    public void setFechaTransaccion(Date fechaTransaccion) { this.fechaTransaccion = fechaTransaccion; }
}