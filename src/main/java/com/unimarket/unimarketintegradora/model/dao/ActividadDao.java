package com.unimarket.unimarketintegradora.model.dao;

import com.unimarket.unimarketintegradora.model.Actividad;
import com.unimarket.unimarketintegradora.utils.SQLConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Objeto de acceso a datos (DAO) de MUA para la entidad Actividad.
 *
 * @author Dulce
 */

public class ActividadDao {
/**
 * Cuenta el total de publicaciones registradas que cumplen las condiciones establecidas por el DAO.
 * @return Devuelve el número de registros que cumplen la condición de la consulta; si no existen registros que coincidan, el resultado es `0`.
 */
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
/**
 * Cuenta el total de usuarios registrados que cumplen las condiciones establecidas por el DAO.
 * @return Devuelve el número de registros que cumplen la condición de la consulta; si no existen registros que coincidan, el resultado es `0`.
 */
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
}