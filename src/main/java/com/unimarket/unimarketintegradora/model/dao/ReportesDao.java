package com.unimarket.unimarketintegradora.model.dao;

import com.unimarket.unimarketintegradora.model.Reportes;
import com.unimarket.unimarketintegradora.utils.SQLConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReportesDao implements Dao<Reportes, Integer> {

    @Override
    public boolean create(Reportes entidad) {
        // Lógica de autoincremento manual sin secuencias
        String sqlMaxId = "SELECT NVL(MAX(ID_REPORTE), 0) + 1 AS SIGUIENTE_ID FROM REPORTE";
        String sqlInsert = "INSERT INTO REPORTE (ID_REPORTE, TIPO_DENUNCIA, MOTIVO, MATRICULA_DENUNCIANTE_FK, MATRICULA_DENUNCIADO_FK, ESTADO_REPORTE) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = SQLConnector.getConnection()) {
            int siguienteId = 1;

            // 1. Obtenemos el nuevo ID
            try (PreparedStatement psId = con.prepareStatement(sqlMaxId);
                 ResultSet rs = psId.executeQuery()) {
                if (rs.next()) {
                    siguienteId = rs.getInt("SIGUIENTE_ID");
                }
            }

            // 2. Insertamos el reporte
            try (PreparedStatement psInsert = con.prepareStatement(sqlInsert)) {
                psInsert.setInt(1, siguienteId);
                psInsert.setString(2, entidad.getTipoDenuncia());
                psInsert.setString(3, entidad.getMotivo()); // JDBC maneja el null automáticamente
                psInsert.setString(4, entidad.getIdUsuarioDenuncianteFk());
                psInsert.setString(5, entidad.getIdUsuarioDenunciadoFk());
                psInsert.setString(6, entidad.getEstadoReporte());
                return psInsert.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al crear reporte: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Reportes> getAll() {
        List<Reportes> lista = new ArrayList<>();
        String sql = "SELECT * FROM REPORTE ORDER BY ID_REPORTE DESC";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Reportes r = new Reportes();
                r.setIdReporte(rs.getInt("ID_REPORTE"));
                r.setTipoDenuncia(rs.getString("TIPO_DENUNCIA"));
                r.setMotivo(rs.getString("MOTIVO"));
                r.setIdUsuarioDenuncianteFk(rs.getString("MATRICULA_DENUNCIANTE_FK"));
                r.setIdUsuarioDenunciadoFk(rs.getString("MATRICULA_DENUNCIADO_FK"));
                r.setEstadoReporte(rs.getString("ESTADO_REPORTE"));
                lista.add(r);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar reportes: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public Reportes getById(Integer id) {
        String sql = "SELECT * FROM REPORTE WHERE ID_REPORTE = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Reportes r = new Reportes();
                    r.setIdReporte(rs.getInt("ID_REPORTE"));
                    r.setTipoDenuncia(rs.getString("TIPO_DENUNCIA"));
                    r.setMotivo(rs.getString("MOTIVO"));
                    r.setIdUsuarioDenuncianteFk(rs.getString("MATRICULA_DENUNCIANTE_FK"));
                    r.setIdUsuarioDenunciadoFk(rs.getString("MATRICULA_DENUNCIADO_FK"));
                    r.setEstadoReporte(rs.getString("ESTADO_REPORTE"));
                    return r;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar reporte: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean update(Reportes entidad) {
        String sql = "UPDATE REPORTE SET ESTADO_REPORTE = ? WHERE ID_REPORTE = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, entidad.getEstadoReporte()); // Normalmente solo se actualiza si fue atendido
            ps.setInt(2, entidad.getIdReporte());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar reporte: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM REPORTE WHERE ID_REPORTE = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar reporte: " + e.getMessage());
            return false;
        }
    }

    public List<Reportes> listarReportes() {
        return null;
    }

    public void actualizarEstadoReporte(int idReporte, String atendido) {
    }
}