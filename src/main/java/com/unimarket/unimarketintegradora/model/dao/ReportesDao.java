package com.unimarket.unimarketintegradora.model.dao;

import com.unimarket.unimarketintegradora.model.Reportes;
import com.unimarket.unimarketintegradora.utils.SQLConnector; // Ajusta a la ruta exacta de tu conexion

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReportesDao {

    // Método para obtener todos los reportes
    public List<Reportes> listarReportes() throws SQLException {
        List<Reportes> lista = new ArrayList<>();
        // Ajusta el nombre de la tabla y columnas si varían en tu BD de MySQL
        String sql = "SELECT * FROM reportes ORDER BY idReporte DESC";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Reportes rep = new Reportes();
                rep.setIdReporte(rs.getInt("idReporte"));
                rep.setMotivo(rs.getString("motivo"));
                rep.setIdUsuarioDenuncianteFk(rs.getString("idUsuarioDenuncianteFk"));
                rep.setIdUsuarioDenunciadoFk(rs.getString("idUsuarioDenunciadoFk"));
                rep.setEstadoReporte(rs.getString("estadoReporte"));

                lista.add(rep);
            }
        }
        return lista;
    }

    // Método para cambiar el estado
    public void actualizarEstadoReporte(int idReporte, String nuevoEstado) throws SQLException {
        String sql = "UPDATE reportes SET estadoReporte = ? WHERE idReporte = ?";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevoEstado);
            ps.setInt(2, idReporte);
            ps.executeUpdate();
        }
    }
}