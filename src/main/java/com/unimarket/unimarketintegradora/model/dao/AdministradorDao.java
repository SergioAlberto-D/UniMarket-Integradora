package com.unimarket.unimarketintegradora.model.dao;

import com.unimarket.unimarketintegradora.model.Administrador;
import com.unimarket.unimarketintegradora.utils.SQLConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdministradorDao implements Dao<Administrador, Integer> { 

    @Override
    public boolean create(Administrador entidad) {
        String sql = "INSERT INTO administrador (nombre, correo, id_division_academica_fk, id_rol_fk) VALUES (?, ?, ?, ?)";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, entidad.getNombre());
            ps.setString(2, entidad.getCorreo());
            ps.setInt(3, entidad.getIdDivisionAcademicaFk());
            ps.setInt(4, entidad.getIdRolFk());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al crear administrador: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Administrador> getAll() {
        List<Administrador> admins = new ArrayList<>();
        String sql = "SELECT * FROM administrador";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Administrador admin = new Administrador(
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getInt("id_division_academica_fk"),
                        rs.getInt("id_rol_fk")
                );
                admin.setIdAdmin(rs.getInt("id_admin"));
                admins.add(admin);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener administradores: " + e.getMessage());
        }
        return admins;
    }

    @Override
    public Administrador getById(Integer id) {
        String sql = "SELECT * FROM administrador WHERE id_admin = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Administrador admin = new Administrador(
                            rs.getString("nombre"),
                            rs.getString("correo"),
                            rs.getInt("id_division_academica_fk"),
                            rs.getInt("id_rol_fk")
                    );
                    admin.setIdAdmin(rs.getInt("id_admin"));
                    return admin;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar administrador: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean update(Administrador entidad) {
        String sql = "UPDATE administrador SET nombre = ?, correo = ?, id_division_academica_fk = ?, id_rol_fk = ? WHERE id_admin = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, entidad.getNombre());
            ps.setString(2, entidad.getCorreo());
            ps.setInt(3, entidad.getIdDivisionAcademicaFk());
            ps.setInt(4, entidad.getIdRolFk());
            ps.setInt(5, entidad.getIdAdmin());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar administrador: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM administrador WHERE id_admin = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar administrador: " + e.getMessage());
            return false;
        }
    }
    public Administrador validarLoginAdmin(String correo, String contrasenaPlana) {
        String sql = "SELECT a.* FROM administrador a " +
                "JOIN contrasena_admin ca ON a.id_admin = ca.id_admin_fk " +
                "WHERE a.correo = ? AND ca.contrasena_hash = LOWER(RAWTOHEX(STANDARD_HASH(?, 'SHA256')))";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, correo);
            ps.setString(2, contrasenaPlana);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Administrador admin = new Administrador(
                            rs.getString("nombre"),
                            rs.getString("correo"),
                            rs.getInt("id_division_academica_fk"),
                            rs.getInt("id_rol_fk")
                    );
                    admin.setIdAdmin(rs.getInt("id_admin"));
                    return admin;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al validar login de admin: " + e.getMessage());
        }
        return null;
    }
}