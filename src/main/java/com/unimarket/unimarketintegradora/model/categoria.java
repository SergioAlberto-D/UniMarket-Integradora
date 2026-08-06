package com.unimarket.unimarketintegradora.model;

public class categoria {
    private int idCategoria;
    private String categoria;

    public categoria() {}

    public categoria(String categoria) {
        this.categoria = categoria;
    }

    public int getIdCategoria() { return idCategoria; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
}