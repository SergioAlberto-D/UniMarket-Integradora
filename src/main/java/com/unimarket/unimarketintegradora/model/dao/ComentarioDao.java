package com.unimarket.unimarketintegradora.model.dao;

import com.unimarket.unimarketintegradora.model.Comentario;
import com.unimarket.unimarketintegradora.utils.SQLConnector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComentarioDao implements Dao<Comentario, Integer> {

    @Override
    public boolean create(Comentario c) {
        // Usamos SEQ_COMENTARIO.NEXTVAL y SYSDATE, omitiendo totalmente el artículo
        String sql = "INSERT INTO comentario (id_comentario, comentario, calificacion, matricula_remitente_fk, matricula_receptor_fk, fecha_comentario) " +
                "VALUES (SEQ_COMENTARIO.NEXTVAL, ?, ?, ?, ?, SYSDATE)";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getComentario());
            ps.setInt(2, c.getCalificacion());
            ps.setString(3, c.getIdUsuarioRemitenteFk());
            ps.setString(4, c.getIdUsuarioReceptorFk());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error en create Comentario: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Comentario getById(Integer id) {
        String sql = "SELECT * FROM comentario WHERE id_comentario = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearComentarioBasico(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en getById Comentario: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Comentario> getAll() {
        List<Comentario> lista = new ArrayList<>();
        String sql = "SELECT * FROM comentario ORDER BY id_comentario DESC";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearComentarioBasico(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error en getAll Comentario: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean update(Comentario c) {
        String sql = "UPDATE comentario SET comentario = ?, calificacion = ?, matricula_remitente_fk = ?, matricula_receptor_fk = ? WHERE id_comentario = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getComentario());
            ps.setInt(2, c.getCalificacion());
            ps.setString(3, c.getIdUsuarioRemitenteFk());
            ps.setString(4, c.getIdUsuarioReceptorFk());
            ps.setInt(5, c.getIdComentario());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error en update Comentario: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM comentario WHERE id_comentario = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error en delete Comentario: " + e.getMessage());
            return false;
        }
    }

    public List<Comentario> obtenerPorVendedor(String matriculaVendedor) {
        List<Comentario> lista = new ArrayList<>();
        String sql = "SELECT c.*, u.nombre AS nombre_remitente, u.apellido_paterno, u.foto_perfil AS foto_remitente " +
                "FROM comentario c " +
                "JOIN usuario u ON c.matricula_remitente_fk = u.matricula " +
                "WHERE c.matricula_receptor_fk = ? ORDER BY c.fecha_comentario DESC";

        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, matriculaVendedor);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Comentario c = mapearComentarioBasico(rs);
                    c.setFechaComentario(rs.getTimestamp("fecha_comentario"));
                    c.setNombreRemitente(rs.getString("nombre_remitente") + " " + rs.getString("apellido_paterno"));
                    c.setFotoRemitente(rs.getString("foto_remitente"));
                    lista.add(c);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener comentarios: " + e.getMessage());
        }
        return lista;
    }

    public int contarComentariosRealizados(String matriculaRemitente) {
        String sql = "SELECT COUNT(*) FROM comentario WHERE matricula_remitente_fk = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, matriculaRemitente);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en contarComentariosRealizados: " + e.getMessage());
        }
        return 0;
    }

    private Comentario mapearComentarioBasico(ResultSet rs) throws SQLException {
        Comentario c = new Comentario(
                rs.getString("comentario"),
                rs.getInt("calificacion"),
                rs.getString("matricula_remitente_fk"),
                rs.getString("matricula_receptor_fk")
        );
        c.setIdComentario(rs.getInt("id_comentario"));
        try {
            c.setFechaComentario(rs.getTimestamp("fecha_comentario"));
        } catch (Exception ignored) {}
        return c;
    }
}