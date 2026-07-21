package com.unimarket.unimarketintegradora.model.dao;

import com.unimarket.unimarketintegradora.model.Comentario;
import com.unimarket.unimarketintegradora.utils.SQLConnector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComentarioDao implements Dao<Comentario, String> {

    @Override
    public boolean create(Comentario entidad) {
        String sql = "INSERT INTO comentario (comentario, calificacion, id_articulo_fk, MATRICULA_remitente_fk, MATRICULA_receptor_fk) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, entidad.getComentario());
            ps.setInt(2, entidad.getCalificacion());
            if (entidad.getIdArticuloFk() != null) {
                ps.setInt(3, entidad.getIdArticuloFk());
            } else {
                ps.setNull(3, Types.INTEGER); // Permite nulos como pediste
            }
            ps.setString(4, entidad.getIdUsuarioRemitenteFk());
            ps.setString(5, entidad.getIdUsuarioReceptorFk());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al registrar comentario: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Comentario> getAll() {
        List<Comentario> lista = new ArrayList<>();
        String sql = "SELECT * FROM comentario";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Integer idArt = rs.getInt("id_articulo_fk");
                if (rs.wasNull()) idArt = null;
                Comentario c = new Comentario(rs.getString("comentario"), rs.getInt("calificacion"), idArt, rs.getString("id_usuario_remitente_fk"), rs.getString("id_usuario_receptor_fk"));
                c.setIdComentario(rs.getInt("id_comentario"));
                lista.add(c);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar comentarios: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public Comentario getById(String id) {
        String sql = "SELECT * FROM comentario WHERE id_comentario = ?";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Integer idArt = rs.getInt("id_articulo_fk");
                    if (rs.wasNull()) idArt = null;
                    Comentario c = new Comentario(rs.getString("comentario"), rs.getInt("calificacion"), idArt, rs.getString("id_usuario_remitente_fk"), rs.getString("id_usuario_receptor_fk"));
                    c.setIdComentario(rs.getInt("id_comentario"));
                    return c;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar comentario: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean update(Comentario entidad) {
        String sql = "UPDATE comentario SET comentario = ?, calificacion = ? WHERE id_comentario = ?";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, entidad.getComentario());
            ps.setInt(2, entidad.getCalificacion());
            ps.setInt(3, entidad.getIdComentario());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar comentario: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        String sql = "DELETE FROM comentario WHERE id_comentario = ?";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al borrar comentario: " + e.getMessage());
            return false;
        }
    }
}