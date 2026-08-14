package com.unimarket.unimarketintegradora.model;

public class Reportes {
    private int idReporte;
    private String tipoDenuncia; // NUEVO CAMPO
    private String motivo; // Ahora puede ser null
    private String idUsuarioDenuncianteFk;
    private String idUsuarioDenunciadoFk;
    private String estadoReporte;

    public Reportes() {}

    public Reportes(String tipoDenuncia, String motivo, String idUsuarioDenuncianteFk, String idUsuarioDenunciadoFk, String estadoReporte) {
        this.tipoDenuncia = tipoDenuncia;
        this.motivo = motivo;
        this.idUsuarioDenuncianteFk = idUsuarioDenuncianteFk;
        this.idUsuarioDenunciadoFk = idUsuarioDenunciadoFk;
        this.estadoReporte = estadoReporte;
    }

    public int getIdReporte() { return idReporte; }
    public void setIdReporte(int idReporte) { this.idReporte = idReporte; }

    public String getTipoDenuncia() { return tipoDenuncia; }
    public void setTipoDenuncia(String tipoDenuncia) { this.tipoDenuncia = tipoDenuncia; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public String getIdUsuarioDenuncianteFk() { return idUsuarioDenuncianteFk; }
    public void setIdUsuarioDenuncianteFk(String idUsuarioDenuncianteFk) { this.idUsuarioDenuncianteFk = idUsuarioDenuncianteFk; }

    public String getIdUsuarioDenunciadoFk() { return idUsuarioDenunciadoFk; }
    public void setIdUsuarioDenunciadoFk(String idUsuarioDenunciadoFk) { this.idUsuarioDenunciadoFk = idUsuarioDenunciadoFk; }

    public String getEstadoReporte() { return estadoReporte; }
    public void setEstadoReporte(String estadoReporte) { this.estadoReporte = estadoReporte; }
}