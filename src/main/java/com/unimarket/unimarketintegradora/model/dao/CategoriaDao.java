package com.unimarket.unimarketintegradora.model.dao;

import com.unimarket.unimarketintegradora.model.Categoria;
import com.unimarket.unimarketintegradora.utils.SQLConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDao implements Dao<Categoria, Integer> {

    @Override
    public boolean create(Categoria entidad) {
        String sql = "INSERT INTO categoria (categoria) VALUES (?)";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, entidad.getCategoria());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al crear categoria: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Categoria> getAll() {
        List<Categoria> categorias = new ArrayList<>();
        String sql = "SELECT * FROM categoria";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Categoria cat = new Categoria(rs.getString("categoria"));
                cat.setIdCategoria(rs.getInt("id_categoria"));
                categorias.add(cat);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener categorias: " + e.getMessage());
        }
        return categorias;
    }

    @Override
    public Categoria getById(Integer id) {
        String sql = "SELECT * FROM categoria WHERE id_categoria = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Categoria cat = new Categoria(rs.getString("categoria"));
                    cat.setIdCategoria(rs.getInt("id_categoria"));
                    return cat;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar categoria: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean update(Categoria entidad) {
        String sql = "UPDATE categoria SET categoria = ? WHERE id_categoria = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, entidad.getCategoria());
            ps.setInt(2, entidad.getIdCategoria());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar categoria: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM categoria WHERE id_categoria = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar categoria: " + e.getMessage());
            return false;
        }
    }
}