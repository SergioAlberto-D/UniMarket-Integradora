package com.unimarket.unimarketintegradora.model.dao;

import com.unimarket.unimarketintegradora.model.ImagenArticulo;
import com.unimarket.unimarketintegradora.utils.SQLConnector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ImagenArticuloDao implements Dao<ImagenArticulo, Integer> {

    @Override
    public boolean create(ImagenArticulo entidad) {
        String sql = "INSERT INTO imagen_articulo (id_imagen, id_articulo_fk, url_imagen) VALUES (SEQ_IMAGEN_ARTICULO.NEXTVAL, ?, ?)";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, entidad.getIdArticuloFk());
            ps.setString(2, entidad.getUrlImagen());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al guardar imagen: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<ImagenArticulo> getAll() { return new ArrayList<>(); }
    @Override
    public ImagenArticulo getById(Integer id) {
        String sql = "SELECT * FROM imagen_articulo WHERE id_imagen = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ImagenArticulo img = new ImagenArticulo(
                            rs.getInt("id_articulo_fk"),
                            rs.getString("url_imagen")
                    );
                    img.setIdImagen(rs.getInt("id_imagen"));
                    return img;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar imagen por ID: " + e.getMessage());
        }
        return null;
    }

    public List<ImagenArticulo> getByArticuloId(Integer idArticuloFk) {
        List<ImagenArticulo> lista = new ArrayList<>();
        String sql = "SELECT * FROM imagen_articulo WHERE id_articulo_fk = ?";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idArticuloFk);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ImagenArticulo img = new ImagenArticulo(rs.getInt("id_articulo_fk"), rs.getString("url_imagen"));
                    img.setIdImagen(rs.getInt("id_imagen"));
                    lista.add(img);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar imágenes: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean update(ImagenArticulo entidad) { return false; }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM imagen_articulo WHERE id_imagen = ?";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar imagen: " + e.getMessage());
            return false;
        }
    }

    public List<ImagenArticulo> obtenerPorArticulo(int idArticulo) {
        List<ImagenArticulo> lista = new ArrayList<>();
        String sql = "SELECT * FROM imagen_articulo WHERE id_articulo_fk = ?";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idArticulo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ImagenArticulo img = new ImagenArticulo(rs.getInt("id_articulo_fk"), rs.getString("url_imagen"));
                    img.setIdImagen(rs.getInt("id_imagen"));
                    lista.add(img);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener imágenes: " + e.getMessage());
        }
        return lista;
    }

    public boolean eliminarPorArticulo(int idArticulo) {
        String sql = "DELETE FROM imagen_articulo WHERE id_articulo_fk = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idArticulo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar las imágenes del artículo: " + e.getMessage());
            return false;
        }
    }
}