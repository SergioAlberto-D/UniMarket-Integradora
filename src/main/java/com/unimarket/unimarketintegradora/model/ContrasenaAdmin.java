package com.unimarket.unimarketintegradora.model;

public class ContrasenaAdmin {
    private int idContrasenaAdmin;
    private int idAdminFk;
    private String contrasenaHash;

    public ContrasenaAdmin() {}

    public ContrasenaAdmin(int idAdminFk, String contrasenaHash) {
        this.idAdminFk = idAdminFk;
        this.contrasenaHash = contrasenaHash;
    }

    public int getIdContrasenaAdmin() { return idContrasenaAdmin; }
    public void setIdContrasenaAdmin(int idContrasenaAdmin) { this.idContrasenaAdmin = idContrasenaAdmin; }
    
    public int getIdAdminFk() { return idAdminFk; }
    public void setIdAdminFk(int idAdminFk) { this.idAdminFk = idAdminFk; }
    
    public String getContrasenaHash() { return contrasenaHash; }
    public void setContrasenaHash(String contrasenaHash) { this.contrasenaHash = contrasenaHash; }
}