package com.unimarket.unimarketintegradora.model;

public class DivisionAcademica {
    private int idDivisionAcademica;
    private String divisionAcademica;

    public DivisionAcademica() {}

    public DivisionAcademica(String divisionAcademica) {
        this.divisionAcademica = divisionAcademica;
    }

    public int getIdDivisionAcademica() { return idDivisionAcademica; }
    public void setIdDivisionAcademica(int idDivisionAcademica) { this.idDivisionAcademica = idDivisionAcademica; }
    public String getDivisionAcademica() { return divisionAcademica; }
    public void setDivisionAcademica(String divisionAcademica) { this.divisionAcademica = divisionAcademica; }
}