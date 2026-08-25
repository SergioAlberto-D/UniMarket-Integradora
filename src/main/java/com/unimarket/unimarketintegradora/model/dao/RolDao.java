package com.unimarket.unimarketintegradora.model.dao;

import com.unimarket.unimarketintegradora.model.Rol;
import com.unimarket.unimarketintegradora.utils.SQLConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RolDao implements Dao<Rol, String> {

    @Override
    public boolean create(Rol entidad) {
        String sql = "INSERT INTO rol (nombre_rol) VALUES (?)";
        try (Connection con = SQLConnector.getConnection(); // Uso del conector estándar
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, entidad.getNombreRol());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al crear rol: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Rol> getAll() {
        List<Rol> roles = new ArrayList<>();
        String sql = "SELECT * FROM rol";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                roles.add(new Rol(rs.getString("nombre_rol")));
                roles.get(roles.size() - 1).setIdRol(rs.getInt("id_rol"));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener roles: " + e.getMessage());
        }
        return roles;
    }

    @Override
    public Rol getById(String id) {
        String sql = "SELECT * FROM rol WHERE id_rol = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Rol rol = new Rol(rs.getString("nombre_rol"));
                    rol.setIdRol(rs.getInt("id_rol"));
                    return rol;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar rol: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean update(Rol entidad) {
        String sql = "UPDATE rol SET nombre_rol = ? WHERE id_rol = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, entidad.getNombreRol());
            ps.setInt(2, entidad.getIdRol());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar rol: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        String sql = "DELETE FROM rol WHERE id_rol = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar rol: " + e.getMessage());
            return false;
        }
    }
}