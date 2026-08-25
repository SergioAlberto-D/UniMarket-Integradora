package com.unimarket.unimarketintegradora.model;

public class Notificacion {
    private int idNotificacion;
    private String matriculaUsuarioFk;
    private String mensaje;
    private String tipo;
    private int leida;
    private String tiempoTranscurrido; // Campo auxiliar para mostrar "Hace X min"

    public Notificacion() {}

    public Notificacion(String matriculaUsuarioFk, String mensaje, String tipo) {
        this.matriculaUsuarioFk = matriculaUsuarioFk;
        this.mensaje = mensaje;
        this.tipo = tipo;
        this.leida = 0;
    }

    // --- GETTERS Y SETTERS ---
    public int getIdNotificacion() { return idNotificacion; }
    public void setIdNotificacion(int idNotificacion) { this.idNotificacion = idNotificacion; }

    public String getMatriculaUsuarioFk() { return matriculaUsuarioFk; }
    public void setMatriculaUsuarioFk(String matriculaUsuarioFk) { this.matriculaUsuarioFk = matriculaUsuarioFk; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public int getLeida() { return leida; }
    public void setLeida(int leida) { this.leida = leida; }

    public String getTiempoTranscurrido() { return tiempoTranscurrido; }
    public void setTiempoTranscurrido(String tiempoTranscurrido) { this.tiempoTranscurrido = tiempoTranscurrido; }
}