package com.unimarket.unimarketintegradora.model.dao;

import com.unimarket.unimarketintegradora.model.Administrador;
import com.unimarket.unimarketintegradora.utils.SQLConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Objeto de acceso a datos (DAO) de MUA para la entidad Administrador.
 * Creacion, Consulta
 * @author Dulce
 */

public class AdministradorDao implements Dao<Administrador, Integer> {

    /**
     * Ejecuta la operación de persistencia 'create' para la entidad.
     * @param entidad Recibe los datos para poder generar una identidad Administrador
     * @return Mediante la coneccion hacia la base manda los datos insertandolos en su correspondiente
     */
/**
 * Registra una nueva entidad en la tabla correspondiente mediante una sentencia INSERT.
 * @param entidad Objeto de la entidad que contiene los datos que se almacenarán o actualizarán en la base de datos.
 * @return Devuelve `true` porque la sentencia SQL afectó al menos un registro, lo que indica que la operación se realizó correctamente. Devuelve `false` cuando no se modificó ningún registro o cuando ocurre una excepción de base de datos.
 */
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

/**
 * Consulta y obtiene todos los registros disponibles de la entidad, mapeando cada fila de la base de datos a su objeto correspondiente.
 * @return Devuelve una lista de objetos `Administrador` construida a partir de los registros encontrados. Si no existen registros o ocurre un error durante la consulta, se conserva una lista vacía para evitar devolver `null`.
 */
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

/**
 * Busca un registro específico utilizando su identificador y, si existe, lo convierte a la entidad correspondiente.
 * @param id Identificador único del registro que se desea consultar, actualizar o eliminar.
 * @return Devuelve un objeto `Administrador` con los datos recuperados de la base de datos cuando existe un registro que coincide con el identificador o criterio recibido. Si no se encuentra ningún registro, devuelve `null`.
 */
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

/**
 * Actualiza los datos de una entidad existente utilizando su identificador como referencia.
 * @param entidad Objeto de la entidad que contiene los datos que se almacenarán o actualizarán en la base de datos.
 * @return Devuelve `true` porque la sentencia SQL afectó al menos un registro, lo que indica que la operación se realizó correctamente. Devuelve `false` cuando no se modificó ningún registro o cuando ocurre una excepción de base de datos.
 */
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

/**
 * Elimina o realiza la baja lógica del registro identificado, de acuerdo con las reglas definidas para la entidad.
 * @param id Identificador único del registro que se desea consultar, actualizar o eliminar.
 * @return Devuelve `true` porque la sentencia SQL afectó al menos un registro, lo que indica que la operación se realizó correctamente. Devuelve `false` cuando no se modificó ningún registro o cuando ocurre una excepción de base de datos.
 */
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
/**
 * Valida las credenciales del administrador y devuelve sus datos cuando existe una coincidencia válida.
 * @param correo Correo institucional del usuario utilizado para localizar su cuenta o validar sus credenciales.
 * @param contrasenaPlana Contraseña proporcionada por el administrador para validar su acceso.
 * @return Devuelve un objeto `Administrador` con los datos recuperados de la base de datos cuando existe un registro que coincide con el identificador o criterio recibido. Si no se encuentra ningún registro, devuelve `null`.
 */
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