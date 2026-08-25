package com.unimarket.unimarketintegradora.model.dao;

import com.unimarket.unimarketintegradora.model.DivisionAcademica;
import com.unimarket.unimarketintegradora.utils.SQLConnector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Objeto de acceso a datos (DAO) de MUA para la entidad Division Academica.
 *
 * @author Sergio
 */

public class DivisionAcademicaDao implements Dao<DivisionAcademica, Integer> {

/**
 * Registra una nueva entidad en la tabla correspondiente mediante una sentencia INSERT.
 * @param entidad Objeto de la entidad que contiene los datos que se almacenarán o actualizarán en la base de datos.
 * @return Devuelve `true` porque la sentencia SQL afectó al menos un registro, lo que indica que la operación se realizó correctamente. Devuelve `false` cuando no se modificó ningún registro o cuando ocurre una excepción de base de datos.
 */
    @Override
    public boolean create(DivisionAcademica entidad) {
        String sql = "INSERT INTO division_academica (division_academica) VALUES (?)";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, entidad.getDivisionAcademica());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al crear division: " + e.getMessage());
            return false;
        }
    }

/**
 * Consulta y obtiene todos los registros disponibles de la entidad, mapeando cada fila de la base de datos a su objeto correspondiente.
 * @return Devuelve una lista de objetos `DivisionAcademica` construida a partir de los registros encontrados. Si no existen registros o ocurre un error durante la consulta, se conserva una lista vacía para evitar devolver `null`.
 */
    @Override
    public List<DivisionAcademica> getAll() {
        List<DivisionAcademica> lista = new ArrayList<>();
        String sql = "SELECT * FROM division_academica";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                DivisionAcademica d = new DivisionAcademica(rs.getString("division_academica"));
                d.setIdDivisionAcademica(rs.getInt("id_division_academica"));
                lista.add(d);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar divisiones: " + e.getMessage());
        }
        return lista;
    }

/**
 * Busca un registro específico utilizando su identificador y, si existe, lo convierte a la entidad correspondiente.
 * @param id Identificador único del registro que se desea consultar, actualizar o eliminar.
 * @return Devuelve un objeto `DivisionAcademica` con los datos recuperados de la base de datos cuando existe un registro que coincide con el identificador o criterio recibido. Si no se encuentra ningún registro, devuelve `null`.
 */
    @Override
    public DivisionAcademica getById(Integer id) {
        String sql = "SELECT * FROM division_academica WHERE id_division_academica = ?";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    DivisionAcademica d = new DivisionAcademica(rs.getString("division_academica"));
                    d.setIdDivisionAcademica(rs.getInt("id_division_academica"));
                    return d;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar division: " + e.getMessage());
        }
        return null;
    }

/**
 * Actualiza los datos de una entidad existente utilizando su identificador como referencia.
 * @param entidad Objeto de la entidad que contiene los datos que se almacenarán o actualizarán en la base de datos.
 * @return Devuelve `true` porque la sentencia SQL afectó al menos un registro, lo que indica que la operación se realizó correctamente. Devuelve `false` cuando no se modificó ningún registro o cuando ocurre una excepción de base de datos.
 */
    @Override
    public boolean update(DivisionAcademica entidad) {
        String sql = "UPDATE division_academica SET division_academica = ? WHERE id_division_academica = ?";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, entidad.getDivisionAcademica());
            ps.setInt(2, entidad.getIdDivisionAcademica());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar division: " + e.getMessage());
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
        String sql = "DELETE FROM division_academica WHERE id_division_academica = ?";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al borrar division: " + e.getMessage());
            return false;
        }
    }
}