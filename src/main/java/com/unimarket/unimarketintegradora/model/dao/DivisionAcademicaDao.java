package com.unimarket.unimarketintegradora.model.dao;

import com.unimarket.unimarketintegradora.model.DivisionAcademica;
import com.unimarket.unimarketintegradora.utils.SQLConnector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DivisionAcademicaDao implements Dao<DivisionAcademica, Integer> {

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