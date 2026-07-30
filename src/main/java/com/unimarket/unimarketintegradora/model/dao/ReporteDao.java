package com.unimarket.unimarketintegradora.model.dao;

import com.unimarket.unimarketintegradora.model.Reporte;
import com.unimarket.unimarketintegradora.model.TransaccionDTO;
import com.unimarket.unimarketintegradora.utils.SQLConnector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReporteDao implements Dao<Reporte, String> {

    @Override
    public boolean create(Reporte entidad) {
        String sql = "INSERT INTO reporte (motivo, MATRICULA_denunciante_fk, MATRICULA_denunciado_fk, estado_reporte) VALUES (?, ?, ?, ?)";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, entidad.getMotivo());
            ps.setString(2, entidad.getIdUsuarioDenuncianteFk());
            ps.setString(3, entidad.getIdUsuarioDenunciadoFk());
            ps.setString(4, entidad.getEstadoReporte());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al registrar reporte: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Reporte> getAll() {
        List<Reporte> lista = new ArrayList<>();
        String sql = "SELECT * FROM reporte";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Reporte r = new Reporte(rs.getString("motivo"), rs.getString("id_usuario_denunciante_fk"), rs.getString("id_usuario_denunciado_fk"), rs.getString("estado_reporte"));
                r.setIdReporte(rs.getInt("id_reporte"));
                lista.add(r);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar reportes: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public Reporte getById(String id) {
        String sql = "SELECT * FROM reporte WHERE id_reporte = ?";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Reporte r = new Reporte(rs.getString("motivo"), rs.getString("id_usuario_denunciante_fk"), rs.getString("id_usuario_denunciado_fk"), rs.getString("estado_reporte"));
                    r.setIdReporte(rs.getInt("id_reporte"));
                    return r;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar reporte: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean update(Reporte entidad) {
        String sql = "UPDATE reporte SET estado_reporte = ? WHERE id_reporte = ?";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, entidad.getEstadoReporte());
            ps.setInt(2, entidad.getIdReporte());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar reporte: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(String id) { return false; } // Los reportes no se borran por auditoría de administradores


}