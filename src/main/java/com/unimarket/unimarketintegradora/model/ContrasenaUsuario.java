package com.unimarket.unimarketintegradora.model;

public class ContrasenaUsuario {
    private int idContrasena;
    private String idUsuarioFk;
    private String contrasenaHash;

    public ContrasenaUsuario() {}

    public ContrasenaUsuario(String idUsuarioFk, String contrasenaHash) {
        this.idUsuarioFk = idUsuarioFk;
        this.contrasenaHash = contrasenaHash;
    }

    public int getIdContrasena() { return idContrasena; }
    public void setIdContrasena(int idContrasena) { this.idContrasena = idContrasena; }
    
    public String getIdUsuarioFk() { return idUsuarioFk; }
    public void setIdUsuarioFk(String idUsuarioFk) { this.idUsuarioFk = idUsuarioFk; }
    
    public String getContrasenaHash() { return contrasenaHash; }
    public void setContrasenaHash(String contrasenaHash) { this.contrasenaHash = contrasenaHash; }
}