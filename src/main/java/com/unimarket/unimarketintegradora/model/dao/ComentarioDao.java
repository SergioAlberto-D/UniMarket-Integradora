package com.unimarket.unimarketintegradora.model.dao;

import com.unimarket.unimarketintegradora.model.Comentario;
import com.unimarket.unimarketintegradora.utils.SQLConnector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComentarioDao {
    public List<Comentario> obtenerPorVendedor(String matriculaVendedor) {
        List<Comentario> lista = new ArrayList<>();
        String sql = "SELECT c.*, u.nombre AS nombre_remitente, u.foto_perfil AS foto_remitente " +
                "FROM comentario c " +
                "JOIN usuario u ON c.MATRICULA_REMITENTE_FK = u.matricula " +
                "WHERE c.MATRICULA_RECEPTOR_FK = ? ORDER BY c.id_comentario DESC";

        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, matriculaVendedor);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Comentario c = new Comentario(
                            rs.getString("comentario"),
                            rs.getInt("calificacion"),
                            rs.getObject("id_articulo_fk") != null ? rs.getInt("id_articulo_fk") : null,
                            rs.getString("MATRICULA_REMITENTE_FK"),
                            rs.getString("MATRICULA_RECEPTOR_FK")
                    );
                    c.setIdComentario(rs.getInt("id_comentario"));
                    c.setNombreRemitente(rs.getString("nombre_remitente"));
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
}