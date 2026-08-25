package com.unimarket.unimarketintegradora.model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Comentario {
    private int idComentario;
    private String comentario;
    private int calificacion;
    private String idUsuarioRemitenteFk;
    private String idUsuarioReceptorFk;
    private Timestamp fechaComentario; // NUEVO CAMPO DE FECHA
    private String nombreRemitente;
    private String fotoRemitente;

    public Comentario() {}

    public Comentario(String comentario, int calificacion, String idUsuarioRemitenteFk, String idUsuarioReceptorFk) {
        this.comentario = comentario;
        this.calificacion = calificacion;
        this.idUsuarioRemitenteFk = idUsuarioRemitenteFk;
        this.idUsuarioReceptorFk = idUsuarioReceptorFk;
    }

    public String getFechaFormateada() {
        if (fechaComentario == null) {
            return "Reciente";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(fechaComentario);
    }

    // Getters y Setters
    public int getIdComentario() { return idComentario; }
    public void setIdComentario(int idComentario) { this.idComentario = idComentario; }
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
    public int getCalificacion() { return calificacion; }
    public void setCalificacion(int calificacion) { this.calificacion = calificacion; }
    public String getIdUsuarioRemitenteFk() { return idUsuarioRemitenteFk; }
    public void setIdUsuarioRemitenteFk(String idUsuarioRemitenteFk) { this.idUsuarioRemitenteFk = idUsuarioRemitenteFk; }
    public String getIdUsuarioReceptorFk() { return idUsuarioReceptorFk; }
    public void setIdUsuarioReceptorFk(String idUsuarioReceptorFk) { this.idUsuarioReceptorFk = idUsuarioReceptorFk; }
    public Timestamp getFechaComentario() { return fechaComentario; }
    public void setFechaComentario(Timestamp fechaComentario) { this.fechaComentario = fechaComentario; }
    public String getNombreRemitente() { return nombreRemitente; }
    public void setNombreRemitente(String nombreRemitente) { this.nombreRemitente = nombreRemitente; }
    public String getFotoRemitente() { return fotoRemitente; }
    public void setFotoRemitente(String fotoRemitente) { this.fotoRemitente = fotoRemitente; }
}