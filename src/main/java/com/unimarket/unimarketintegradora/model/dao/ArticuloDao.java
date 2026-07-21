package com.unimarket.unimarketintegradora.model.dao;

import com.unimarket.unimarketintegradora.model.Articulo;
import com.unimarket.unimarketintegradora.utils.SQLConnector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArticuloDao implements Dao<Articulo, String> {

    @Override
    public boolean create(Articulo entidad) {
        String sql = "INSERT INTO articulo (nombre, precio, id_categoria_fk, descripcion,MATRICULA_USUARIO_fk) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, entidad.getNombre());
            ps.setBigDecimal(2, entidad.getPrecio());
            ps.setInt(3, entidad.getIdCategoriaFk());
            ps.setString(4, entidad.getDescripcion());
            ps.setString(5, entidad.getIdUsuarioFk());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al crear artículo: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Articulo> getAll() {
        List<Articulo> lista = new ArrayList<>();
        String sql = "SELECT a.*, \n" +
                "       (SELECT ruta_imagen FROM IMAGEN_ARTICULO ia WHERE ia.id_articulo_fk = a.id_articulo FETCH FIRST 1 ROWS ONLY) AS portada \n" +
                "FROM ARTICULO a";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Articulo articulo = new Articulo();
                articulo.setIdArticulo(rs.getInt("id_articulo"));
                articulo.setNombre(rs.getString("nombre"));
                Articulo a = new Articulo(rs.getString("nombre"), rs.getBigDecimal("precio"), rs.getInt("id_categoria_fk"), rs.getString("descripcion"),rs.getString("id_usuario_fk"));
                a.setIdArticulo(rs.getInt("id_articulo"));
                articulo.setImagenPrincipal(rs.getString("portada"));
                lista.add(a);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener artículos: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public Articulo getById(String id) {
        String sql = "SELECT * FROM articulo WHERE id_articulo = ?";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Articulo a = new Articulo(rs.getString("nombre"), rs.getBigDecimal("precio"), rs.getInt("id_categoria_fk"), rs.getString("descripcion"), rs.getString("id_usuario_fk"));
                    a.setIdArticulo(rs.getInt("id_articulo"));
                    return a;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar artículo: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean update(Articulo entidad) {
        String sql = "UPDATE articulo SET nombre = ?, precio = ?, id_categoria_fk = ?, descripcion = ? WHERE id_articulo = ?";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, entidad.getNombre());
            ps.setBigDecimal(2, entidad.getPrecio());
            ps.setInt(3, entidad.getIdCategoriaFk());
            ps.setString(4, entidad.getDescripcion());
            ps.setInt(5, entidad.getIdArticulo());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar artículo: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        String sql = "DELETE FROM articulo WHERE id_articulo = ?";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar artículo: " + e.getMessage());
            return false;
        }
    }
    public int obtenerUltimoIdPorUsuario(String matriculaUsuario) {
        String sql = "SELECT MAX(id_articulo) FROM articulo WHERE matricula_usuario_fk = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, matriculaUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener último ID de artículo: " + e.getMessage());
        }
        return -1;
    }
}