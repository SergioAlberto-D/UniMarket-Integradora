package com.unimarket.unimarketintegradora.model.dao;

import com.unimarket.unimarketintegradora.model.ContrasenaUsuario;
import com.unimarket.unimarketintegradora.utils.SQLConnector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContrasenaUsuarioDao implements Dao<ContrasenaUsuario, String> {

    @Override
    public boolean create(ContrasenaUsuario entidad) {
        String sql = "INSERT INTO CONTRASENA_USUARIO (MATRICULA_USUARIO_FK, CONTRASENA_HASH) VALUES (?, ?)";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, entidad.getIdUsuarioFk());
            ps.setString(2, entidad.getContrasenaHash());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al guardar contraseña: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<ContrasenaUsuario> getAll() {
        return new ArrayList<>(); // No deberías listar contraseñas por seguridad
    }

    @Override
    public ContrasenaUsuario getById(String id) {
        // Se cambiaron las columnas explícitas para evitar errores con ResultSet
        String sql = "SELECT ID_CONTRASENA, MATRICULA_USUARIO_FK, CONTRASENA_HASH FROM CONTRASENA_USUARIO WHERE MATRICULA_USUARIO_FK = ?";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // AQUÍ ESTABA EL ERROR: Se le pide la columna exacta que viene de la base de datos
                    ContrasenaUsuario pass = new ContrasenaUsuario(
                            rs.getString("MATRICULA_USUARIO_FK"),
                            rs.getString("CONTRASENA_HASH")
                    );
                    pass.setIdContrasena(rs.getInt("ID_CONTRASENA"));
                    return pass;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar contraseña: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean update(ContrasenaUsuario entidad) {
        String sql = "UPDATE CONTRASENA_USUARIO SET CONTRASENA_HASH = ? WHERE MATRICULA_USUARIO_FK = ?";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, entidad.getContrasenaHash());
            ps.setString(2, entidad.getIdUsuarioFk());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar contraseña: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        String sql = "DELETE FROM CONTRASENA_USUARIO WHERE MATRICULA_USUARIO_FK = ?";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar contraseña: " + e.getMessage());
            return false;
        }
    }
}