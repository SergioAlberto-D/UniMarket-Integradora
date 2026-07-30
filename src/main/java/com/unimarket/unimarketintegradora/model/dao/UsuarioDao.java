package com.unimarket.unimarketintegradora.model.dao;

import com.unimarket.unimarketintegradora.model.Usuario;
import com.unimarket.unimarketintegradora.utils.SQLConnector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDao implements Dao<Usuario, String> {

    @Override
    public boolean create(Usuario entidad) {
        String sql = "INSERT INTO usuario (MATRICULA,nombre, apellido_paterno, apellido_materno, numero_celular, id_division_academica_fk, fecha_registro, correo_institucional, id_rol_fk, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, entidad.getIdUsuario()); // Asegúrate de pasar la matrícula aquí si es el primer parámetro
            ps.setString(2, entidad.getNombre());
            ps.setString(3, entidad.getApellidoPaterno());
            ps.setString(4, entidad.getApellidoMaterno());
            ps.setString(5, entidad.getNumeroCelular());
            ps.setInt(6, entidad.getIdDivisionAcademicaFk());
            ps.setDate(7, entidad.getFechaRegistro());
            ps.setString(8, entidad.getCorreoInstitucional());
            ps.setInt(9, entidad.getIdRolFk());
            ps.setString(10, "unverificado"); // <--- ESTADO POR DEFECTO CORREGIDO
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al crear usuario: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Usuario> getAll() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuario";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Usuario u = new Usuario(rs.getString("nombre"), rs.getString("apellido_paterno"), rs.getString("apellido_materno"), rs.getString("numero_celular"), rs.getInt("id_division_academica_fk"), rs.getDate("fecha_registro"), rs.getString("correo_institucional"), rs.getInt("id_rol_fk"), rs.getString("estado"));
                u.setIdUsuario(rs.getString("id_usuario"));
                lista.add(u);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener usuarios: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public Usuario getById(String matricula) {
        String sql = "SELECT * FROM usuario WHERE MATRICULA = ?";

        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, matricula);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario();
                    u.setIdUsuario(rs.getString("MATRICULA"));
                    u.setNombre(rs.getString("NOMBRE"));
                    u.setCorreoInstitucional(rs.getString("CORREO_INSTITUCIONAL"));
                    u.setNumeroCelular(rs.getString("NUMERO_CELULAR"));
                    u.setEstado(rs.getString("ESTADO"));
                    u.setIdRolFk(rs.getInt("ID_ROL_FK"));
                    u.setFotoPerfil(rs.getString("FOTO_PERFIL"));
                    u.setIdDivisionAcademicaFk(rs.getInt("ID_DIVISION_ACADEMICA_FK"));

                    return u;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar usuario: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean update(Usuario entidad) {
        // Se corrigió 'WHERE id_usuario = ?' por 'WHERE MATRICULA = ?'
        String sql = "UPDATE usuario SET nombre = ?, apellido_paterno = ?, apellido_materno = ?, numero_celular = ?, id_division_academica_fk = ?, id_rol_fk = ?, estado = ? WHERE MATRICULA = ?";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, entidad.getNombre());
            ps.setString(2, entidad.getApellidoPaterno());
            ps.setString(3, entidad.getApellidoMaterno());
            ps.setString(4, entidad.getNumeroCelular());
            ps.setInt(5, entidad.getIdDivisionAcademicaFk());
            ps.setInt(6, entidad.getIdRolFk());
            ps.setString(7, entidad.getEstado());
            // Se pasa el ID (que almacena la matrícula) al parámetro 8
            ps.setString(8, entidad.getIdUsuario());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        String sql = "UPDATE usuario SET estado = 'inactivo' WHERE MATRICULA = ?";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }
    public boolean activarCuenta(String correo) {
        boolean actualizado = false;
        // Se corrigió el nombre de la tabla a 'usuarios' y el estado a 'verificado'
        String sql = "UPDATE usuario SET estado = 'verificado' WHERE correo_institucional = ?";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, correo);
            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                actualizado = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return actualizado;
    }
    // 1. Verifica si el correo ya existe en la base de datos
    public boolean existeCorreo(String correo) {
        String sql = "SELECT COUNT(*) FROM usuario WHERE correo_institucional = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Si el conteo es mayor a 0, devuelve true
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar existencia de correo: " + e.getMessage());
        }
        return false;
    }

    // 2. Busca y devuelve un Usuario completo usando su correo
    public Usuario buscarPorCorreo(String correo) {
        String sql = "SELECT * FROM usuario WHERE correo_institucional = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario(
                            rs.getString("nombre"),
                            rs.getString("apellido_paterno"),
                            rs.getString("apellido_materno"),
                            rs.getString("numero_celular"),
                            rs.getInt("id_division_academica_fk"),
                            rs.getDate("fecha_registro"),
                            rs.getString("correo_institucional"),
                            rs.getInt("id_rol_fk"),
                            rs.getString("estado")
                    );
                    // IMPORTANTE: Recuerda que la columna ahora se llama 'matricula' en Oracle
                    u.setIdUsuario(rs.getString("matricula"));

                    // ¡NUEVA LÍNEA PARA LA FOTO!
                    u.setFotoPerfil(rs.getString("foto_perfil"));

                    return u;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar usuario por correo: " + e.getMessage());
        }
        return null;
    }

    // 3. Elimina a un usuario por su correo (Útil si falla el envío del token)
    public boolean eliminarPorCorreo(String correo) {
        String sql = "DELETE FROM usuario WHERE correo_institucional = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, correo);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar usuario por correo: " + e.getMessage());
            return false;
        }
    }
    public boolean guardarTokenRecuperacion(String correo, String token) {
        String sql = "UPDATE usuario SET token_recuperacion = ?, token_expiracion = ? WHERE correo_institucional = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Calculamos 15 minutos en el futuro (15 mins * 60 segs * 1000 milisegundos)
            long quinceMinutos = System.currentTimeMillis() + (15 * 60 * 1000);
            Timestamp expiracion = new Timestamp(quinceMinutos);

            ps.setString(1, token);
            ps.setTimestamp(2, expiracion);
            ps.setString(3, correo);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al guardar token de recuperación: " + e.getMessage());
            return false;
        }
    }

    // 2. Valida que el código coincida Y que no haya caducado
    public boolean validarToken(String correo, String token) {
        // Se añade "AND token_expiracion > ?" para validar que siga vivo
        String sql = "SELECT COUNT(*) FROM usuario WHERE correo_institucional = ? AND token_recuperacion = ? AND token_expiracion > ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, correo);
            ps.setString(2, token);
            // Mandamos la hora actual, si es mayor a la expiración, la consulta devolverá 0
            ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al validar token: " + e.getMessage());
        }
        return false;
    }

    // 3. Borra el token y su expiración por seguridad una vez que se usó
    public boolean limpiarToken(String correo) {
        String sql = "UPDATE usuario SET token_recuperacion = NULL, token_expiracion = NULL WHERE correo_institucional = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, correo);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al limpiar el token: " + e.getMessage());
            return false;
        }
    }
    // 4. Actualiza únicamente la foto de perfil del usuario
    public boolean actualizarFotoPerfil(String matricula, String rutaFoto) {
        String sql = "UPDATE usuario SET foto_perfil = ? WHERE matricula = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, rutaFoto);
            ps.setString(2, matricula);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar foto de perfil: " + e.getMessage());
            return false;
        }
    }
    // 5. Actualiza únicamente el teléfono del usuario
    public boolean actualizarTelefono(String matricula, String nuevoTelefono) {
        String sql = "UPDATE usuario SET numero_celular = ? WHERE matricula = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevoTelefono);
            ps.setString(2, matricula);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar el teléfono: " + e.getMessage());
            return false;
        }
    }
    public boolean actualizarRol(String matricula, int nuevoRol) {
        String sql = "UPDATE usuario SET id_rol_fk = ? WHERE matricula = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, nuevoRol);
            ps.setString(2, matricula);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar el rol: " + e.getMessage());
            return false;
        }
    }
}