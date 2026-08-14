package com.unimarket.unimarketintegradora.model.dao;

import com.unimarket.unimarketintegradora.model.ContrasenaAdmin;
import com.unimarket.unimarketintegradora.utils.SQLConnector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContrasenaAdminDao implements Dao<ContrasenaAdmin, Integer> {

    @Override
    public boolean create(ContrasenaAdmin entidad) {
        String sql = "INSERT INTO contrasena_admin (id_admin_fk, contrasena_hash) VALUES (?, ?)";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, entidad.getIdAdminFk());
            ps.setString(2, entidad.getContrasenaHash());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al guardar contraseña de admin: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<ContrasenaAdmin> getAll() { return new ArrayList<>(); }

    @Override
    public ContrasenaAdmin getById(Integer id) {
        String sql = "SELECT * FROM contrasena_admin WHERE id_admin_fk = ?";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ContrasenaAdmin pass = new ContrasenaAdmin(rs.getInt("id_admin_fk"), rs.getString("contrasena_hash"));
                    pass.setIdContrasenaAdmin(rs.getInt("id_contrasena_admin"));
                    return pass;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar contraseña de admin: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean update(ContrasenaAdmin entidad) {
        String sql = "UPDATE contrasena_admin SET contrasena_hash = ? WHERE id_admin_fk = ?";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, entidad.getContrasenaHash());
            ps.setInt(2, entidad.getIdAdminFk());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar contraseña de admin: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM contrasena_admin WHERE id_admin_fk = ?";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al borrar contraseña de admin: " + e.getMessage());
            return false;
        }
    }
}