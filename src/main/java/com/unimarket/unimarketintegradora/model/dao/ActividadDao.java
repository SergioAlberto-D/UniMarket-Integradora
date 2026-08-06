package com.unimarket.unimarketintegradora.model.dao;

import com.unimarket.unimarketintegradora.model.Actividad;
import com.unimarket.unimarketintegradora.utils.SQLConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ActividadDao {

    public int contarTotalPublicaciones() {
        String sql = "SELECT COUNT(*) FROM articulo WHERE estado IS NULL OR estado != 'ELIMINADO'";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error al contar publicaciones: " + e.getMessage());
        }
        return 0;
    }

    public int contarTotalUsuarios() {
        String sql = "SELECT COUNT(*) FROM usuario";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error al contar usuarios: " + e.getMessage());
        }
        return 0;
    }

    public List<Actividad> obtenerActividadReciente() {
        List<Actividad> lista = new ArrayList<>();

        String sql =
                "SELECT u.nombre AS usuario, u.correo, 'Usuarios' AS modulo, 'Registro en el sistema' AS accion, u.fecha_registro AS fecha " +
                        "FROM usuario u " +
                        "UNION ALL " +
                        "SELECT u.nombre AS usuario, u.correo, 'Publicaciones' AS modulo, CONCAT('Publicó artículo ID ', a.id_articulo) AS accion, a.fecha_publicacion AS fecha " +
                        "FROM articulo a " +
                        "JOIN usuario u ON a.matricula_usuario_fk = u.matricula " +
                        "WHERE a.estado IS NULL OR a.estado != 'ELIMINADO' " +
                        "ORDER BY fecha DESC " +
                        "FETCH FIRST 15 ROWS ONLY";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Actividad act = new Actividad(
                        rs.getString("usuario"),
                        rs.getString("correo"),
                        rs.getString("modulo"),
                        rs.getString("accion"),
                        rs.getTimestamp("fecha")
                );
                lista.add(act);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener actividad reciente: " + e.getMessage());
        }
        return lista;
    }
}