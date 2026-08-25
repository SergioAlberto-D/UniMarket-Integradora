package com.unimarket.unimarketintegradora.model.dao;

import com.unimarket.unimarketintegradora.model.ContrasenaUsuario;
import com.unimarket.unimarketintegradora.utils.SQLConnector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Objeto de acceso a datos (DAO) de MUA para la entidad Contrasena Usuario.
 *
 * @author Sergio
 */

public class ContrasenaUsuarioDao implements Dao<ContrasenaUsuario, String> {

/**
 * Registra una nueva entidad en la tabla correspondiente mediante una sentencia INSERT.
 * @param entidad Objeto de la entidad que contiene los datos que se almacenarán o actualizarán en la base de datos.
 * @return Devuelve `true` porque la sentencia SQL afectó al menos un registro, lo que indica que la operación se realizó correctamente. Devuelve `false` cuando no se modificó ningún registro o cuando ocurre una excepción de base de datos.
 */
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

/**
 * Consulta y obtiene todos los registros disponibles de la entidad, mapeando cada fila de la base de datos a su objeto correspondiente.
 * @return Devuelve una lista de objetos `ContrasenaUsuario` construida a partir de los registros encontrados. Si no existen registros o ocurre un error durante la consulta, se conserva una lista vacía para evitar devolver `null`.
 */
    @Override
    public List<ContrasenaUsuario> getAll() {
        return new ArrayList<>(); // No deberías listar contraseñas por seguridad
    }

/**
 * Busca un registro específico utilizando su identificador y, si existe, lo convierte a la entidad correspondiente.
 * @param id Identificador único del registro que se desea consultar, actualizar o eliminar.
 * @return Devuelve un objeto `ContrasenaUsuario` con los datos recuperados de la base de datos cuando existe un registro que coincide con el identificador o criterio recibido. Si no se encuentra ningún registro, devuelve `null`.
 */
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

/**
 * Actualiza los datos de una entidad existente utilizando su identificador como referencia.
 * @param entidad Objeto de la entidad que contiene los datos que se almacenarán o actualizarán en la base de datos.
 * @return Devuelve `true` porque la sentencia SQL afectó al menos un registro, lo que indica que la operación se realizó correctamente. Devuelve `false` cuando no se modificó ningún registro o cuando ocurre una excepción de base de datos.
 */
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

/**
 * Elimina o realiza la baja lógica del registro identificado, de acuerdo con las reglas definidas para la entidad.
 * @param id Identificador único del registro que se desea consultar, actualizar o eliminar.
 * @return Devuelve `true` porque la sentencia SQL afectó al menos un registro, lo que indica que la operación se realizó correctamente. Devuelve `false` cuando no se modificó ningún registro o cuando ocurre una excepción de base de datos.
 */
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