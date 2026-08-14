package com.unimarket.unimarketintegradora.model.dao;

import com.unimarket.unimarketintegradora.model.categoria;
import com.unimarket.unimarketintegradora.utils.SQLConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class categoriaDao implements Dao<categoria, Integer> {

    public List<categoria> listarCategorias() throws SQLException {
        List<categoria> lista = new ArrayList<>();
        String sql = "SELECT ID_CATEGORIA, CATEGORIA FROM CATEGORIA ORDER BY ID_CATEGORIA ASC";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                categoria cat = new categoria();
                cat.setIdCategoria(rs.getInt("ID_CATEGORIA"));
                cat.setCategoria(rs.getString("CATEGORIA"));
                lista.add(cat);
            }
        }
        return lista;
    }

    public void agregarCategoria(categoria categoria) throws SQLException {
        // Consulta para obtener el ID más alto registrado (si la tabla está vacía, devuelve 0)
        String sqlMaxId = "SELECT NVL(MAX(ID_CATEGORIA), 0) + 1 AS SIGUIENTE_ID FROM CATEGORIA";
        String sqlInsert = "INSERT INTO CATEGORIA (ID_CATEGORIA, CATEGORIA) VALUES (?, ?)";

        try (Connection con = SQLConnector.getConnection()) {
            int siguienteId = 1;

            // 1. Obtenemos el nuevo ID
            try (PreparedStatement psId = con.prepareStatement(sqlMaxId);
                 ResultSet rs = psId.executeQuery()) {
                if (rs.next()) {
                    siguienteId = rs.getInt("SIGUIENTE_ID");
                }
            }

            // 2. Insertamos la categoría asignándole el ID calculado
            try (PreparedStatement psInsert = con.prepareStatement(sqlInsert)) {
                psInsert.setInt(1, siguienteId);
                psInsert.setString(2, categoria.getCategoria());
                psInsert.executeUpdate();
            }
        }
    }

    public void eliminarCategoria(int idCategoria) throws SQLException {
        String sql = "DELETE FROM CATEGORIA WHERE ID_CATEGORIA = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCategoria);
            ps.executeUpdate();
        }
    }
    @Override
    public boolean create(categoria entidad) {
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
    public List<categoria> getAll() {
        List<categoria> categorias = new ArrayList<>();
        String sql = "SELECT * FROM categoria ORDER BY ID_CATEGORIA ASC";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                categoria cat = new categoria(rs.getString("categoria"));
                cat.setIdCategoria(rs.getInt("id_categoria"));
                categorias.add(cat);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener categorias: " + e.getMessage());
        }
        return categorias;
    }

    @Override
    public categoria getById(Integer id) {
        String sql = "SELECT * FROM categoria WHERE id_categoria = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    categoria cat = new categoria(rs.getString("categoria"));
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
    public boolean update(categoria entidad) {
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