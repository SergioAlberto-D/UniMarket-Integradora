package com.unimarket.unimarketintegradora.model;

import java.math.BigDecimal;

public class Oferta {
    private int idOferta;
    private int idArticuloFk;
    private String idUsuarioFk;
    private BigDecimal monto;
    private String estado;

    public Oferta() {}

    public Oferta(int idArticuloFk, String idUsuarioFk, BigDecimal monto, String estado) {
        this.idArticuloFk = idArticuloFk;
        this.idUsuarioFk = idUsuarioFk;
        this.monto = monto;
        this.estado = estado;
    }

    public int getIdOferta() { return idOferta; }
    public void setIdOferta(int idOferta) { this.idOferta = idOferta; }

    public int getIdArticuloFk() { return idArticuloFk; }
    public void setIdArticuloFk(int idArticuloFk) { this.idArticuloFk = idArticuloFk; }

    public String getIdUsuarioFk() { return idUsuarioFk; }
    public void setIdUsuarioFk(String idUsuarioFk) { this.idUsuarioFk = idUsuarioFk; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}