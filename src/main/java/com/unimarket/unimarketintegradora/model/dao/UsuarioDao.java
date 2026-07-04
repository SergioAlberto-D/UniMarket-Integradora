package com.unimarket.unimarketintegradora.model.dao;

import com.unimarket.unimarketintegradora.model.Usuario;
import com.unimarket.unimarketintegradora.utils.SQLConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDao implements Dao<Usuario,Integer> {

    public boolean create(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nombres, apellido_paterno, apellido_materno, telefono, carrera, correo_institucional, contrasena) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usuario.getNombres());
            ps.setString(2, usuario.getApellidoPaterno());
            ps.setString(3, usuario.getApellidoMaterno());
            ps.setString(4, usuario.getTelefono());
            ps.setString(5, usuario.getCarrera());
            ps.setString(6, usuario.getCorreoInstitucional());
            ps.setString(7, usuario.getContrasena());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("error al registrar alumno: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Usuario> getAll() {
        List<Usuario> datos = new ArrayList<>();
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM usuarios");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setNombres(rs.getString("nombres"));
                usuario.setApellidoPaterno(rs.getString("apellido_paterno"));
                usuario.setApellidoMaterno(rs.getString("apellido_materno"));
                usuario.setTelefono(rs.getString("telefono"));
                usuario.setCarrera(rs.getString("carrera"));
                usuario.setCorreoInstitucional(rs.getString("correo_institucional"));
                usuario.setContrasena(rs.getString("contrasena"));
                datos.add(usuario);
            }
        } catch (SQLException e) {
            System.out.println("fallo"+e.getMessage());
        }
        return datos;
    }

    @Override
    public Usuario getById(Integer id) {
        return null;
    }

    @Override
    public boolean update(Usuario entidad) {
        return false;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }

    public boolean existeCorreo(String correoInstitucional) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE correo_institucional = ?";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, correoInstitucional);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public Usuario buscarPorCorreoYContrasena(String correoInstitucional, String contrasena) throws SQLException {
        String sql = "SELECT id_usuario, nombres, apellido_paterno, apellido_materno, telefono, carrera, correo_institucional, contrasena " +
                "FROM usuarios WHERE correo_institucional = ? AND contrasena = ?";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, correoInstitucional);
            ps.setString(2, contrasena);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setIdUsuario(rs.getInt("id_usuario"));
                    usuario.setNombres(rs.getString("nombres"));
                    usuario.setApellidoPaterno(rs.getString("apellido_paterno"));
                    usuario.setApellidoMaterno(rs.getString("apellido_materno"));
                    usuario.setTelefono(rs.getString("telefono"));
                    usuario.setCarrera(rs.getString("carrera"));
                    usuario.setCorreoInstitucional(rs.getString("correo_institucional"));
                    usuario.setContrasena(rs.getString("contrasena"));
                    return usuario;
                }
            }
        }

        return null;
    }
}
