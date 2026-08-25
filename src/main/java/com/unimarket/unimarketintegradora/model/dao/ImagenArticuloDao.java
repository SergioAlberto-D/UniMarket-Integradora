package com.unimarket.unimarketintegradora.model.dao;

import com.unimarket.unimarketintegradora.model.ImagenArticulo;
import com.unimarket.unimarketintegradora.utils.SQLConnector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Objeto de acceso a datos (DAO) de MUA para la entidad Imagen Articulo.
 *
 * @author Sergio
 */

public class ImagenArticuloDao implements Dao<ImagenArticulo, Integer> {

/**
 * Registra una nueva entidad en la tabla correspondiente mediante una sentencia INSERT.
 * @param entidad Objeto de la entidad que contiene los datos que se almacenarán o actualizarán en la base de datos.
 * @return Devuelve `true` porque la sentencia SQL afectó al menos un registro, lo que indica que la operación se realizó correctamente. Devuelve `false` cuando no se modificó ningún registro o cuando ocurre una excepción de base de datos.
 */
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

/**
 * Consulta y obtiene todos los registros disponibles de la entidad, mapeando cada fila de la base de datos a su objeto correspondiente.
 * @return Devuelve una lista de objetos `ImagenArticulo` construida a partir de los registros encontrados. Si no existen registros o ocurre un error durante la consulta, se conserva una lista vacía para evitar devolver `null`.
 */
    @Override
    public List<ImagenArticulo> getAll() { return new ArrayList<>(); }
/**
 * Busca un registro específico utilizando su identificador y, si existe, lo convierte a la entidad correspondiente.
 * @param id Identificador único del registro que se desea consultar, actualizar o eliminar.
 * @return Devuelve un objeto `ImagenArticulo` con los datos recuperados de la base de datos cuando existe un registro que coincide con el identificador o criterio recibido. Si no se encuentra ningún registro, devuelve `null`.
 */
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
/**
 * Obtiene las imágenes asociadas a un artículo específico.
 * @param idArticuloFk Identificador del artículo al que pertenece la imagen.
 * @return Devuelve una lista de objetos `ImagenArticulo` construida a partir de los registros encontrados. Si no existen registros o ocurre un error durante la consulta, se conserva una lista vacía para evitar devolver `null`.
 */
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

/**
 * Actualiza los datos de una entidad existente utilizando su identificador como referencia.
 * @param entidad Objeto de la entidad que contiene los datos que se almacenarán o actualizarán en la base de datos.
 * @return Devuelve un valor booleano que indica si la operación se realizó correctamente; `true` representa éxito y `false` representa que no se pudo completar la operación.
 */
    @Override
    public boolean update(ImagenArticulo entidad) { return false; }

/**
 * Elimina o realiza la baja lógica del registro identificado, de acuerdo con las reglas definidas para la entidad.
 * @param id Identificador único del registro que se desea consultar, actualizar o eliminar.
 * @return Devuelve `true` porque la sentencia SQL afectó al menos un registro, lo que indica que la operación se realizó correctamente. Devuelve `false` cuando no se modificó ningún registro o cuando ocurre una excepción de base de datos.
 */
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
/**
 * Obtiene las imágenes registradas para el artículo indicado.
 * @param idArticulo Identificador único del artículo sobre el que se realizará la consulta, validación, eliminación o modificación.
 * @return Devuelve una lista de objetos `ImagenArticulo` construida a partir de los registros encontrados. Si no existen registros o ocurre un error durante la consulta, se conserva una lista vacía para evitar devolver `null`.
 */
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
/**
 * Elimina las imágenes asociadas al artículo indicado de la base de datos.
 * @param idArticulo Identificador único del artículo sobre el que se realizará la consulta, validación, eliminación o modificación.
 * @return Devuelve `true` porque la sentencia SQL afectó al menos un registro, lo que indica que la operación se realizó correctamente. Devuelve `false` cuando no se modificó ningún registro o cuando ocurre una excepción de base de datos.
 */
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