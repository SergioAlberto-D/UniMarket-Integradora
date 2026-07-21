package com.unimarket.unimarketintegradora.model;

public class Comentario {
    private int idComentario;
    private String comentario;
    private int calificacion;
    private Integer idArticuloFk; // Se usa Integer para permitir nulos (NULL)
    private String idUsuarioRemitenteFk;
    private String idUsuarioReceptorFk;

    public Comentario() {}

    public Comentario(String comentario, int calificacion, Integer idArticuloFk, String idUsuarioRemitenteFk, String idUsuarioReceptorFk) {
        this.comentario = comentario;
        this.calificacion = calificacion;
        this.idArticuloFk = idArticuloFk;
        this.idUsuarioRemitenteFk = idUsuarioRemitenteFk;
        this.idUsuarioReceptorFk = idUsuarioReceptorFk;
    }

    // Getters y Setters (Asegúrate de que get/setIdArticuloFk utilicen Integer)
    public int getIdComentario() { return idComentario; }
    public void setIdComentario(int idComentario) { this.idComentario = idComentario; }
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
    public int getCalificacion() { return calificacion; }
    public void setCalificacion(int calificacion) { this.calificacion = calificacion; }
    public Integer getIdArticuloFk() { return idArticuloFk; }
    public void setIdArticuloFk(Integer idArticuloFk) { this.idArticuloFk = idArticuloFk; }
    public String getIdUsuarioRemitenteFk() { return idUsuarioRemitenteFk; }
    public void setIdUsuarioRemitenteFk(String idUsuarioRemitenteFk) { this.idUsuarioRemitenteFk = idUsuarioRemitenteFk; }
    public String getIdUsuarioReceptorFk() { return idUsuarioReceptorFk; }
    public void setIdUsuarioReceptorFk(String idUsuarioReceptorFk) { this.idUsuarioReceptorFk = idUsuarioReceptorFk; }
}