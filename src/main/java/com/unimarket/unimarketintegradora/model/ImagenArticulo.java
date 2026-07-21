package com.unimarket.unimarketintegradora.model;

public class ImagenArticulo {
    private int idImagen;
    private int idArticuloFk;
    private String urlImagen;

    public ImagenArticulo() {}

    public ImagenArticulo(int idArticuloFk, String urlImagen) {
        this.idArticuloFk = idArticuloFk;
        this.urlImagen = urlImagen;
    }

    public int getIdImagen() { return idImagen; }
    public void setIdImagen(int idImagen) { this.idImagen = idImagen; }

    public int getIdArticuloFk() { return idArticuloFk; }
    public void setIdArticuloFk(int idArticuloFk) { this.idArticuloFk = idArticuloFk; }

    public String getUrlImagen() { return urlImagen; }
    public void setUrlImagen(String urlImagen) { this.urlImagen = urlImagen; }
}