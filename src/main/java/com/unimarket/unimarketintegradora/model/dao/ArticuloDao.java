package com.unimarket.unimarketintegradora.model.dao;

import com.unimarket.unimarketintegradora.model.Articulo;
import com.unimarket.unimarketintegradora.utils.SQLConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArticuloDao implements Dao<Articulo, Integer> {

    @Override
    public boolean create(Articulo articulo) {
        return crearConImagenes(articulo, new ArrayList<>());
    }

    public boolean crearConImagenes(Articulo articulo, List<String> rutasImagenes) {
        String sqlArticulo = "INSERT INTO articulos " +
                "(id_usuario, titulo, descripcion, precio, categoria, carrera, lugar_encuentro) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        String sqlImagen = "INSERT INTO articulo_imagenes " +
                "(id_articulo, ruta_imagen, es_principal) " +
                "VALUES (?, ?, ?)";

        try (Connection con = SQLConnector.getConnection()) {
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(sqlArticulo, new String[]{"id_articulo"})) {
                ps.setInt(1, articulo.getIdUsuario());
                ps.setString(2, articulo.getTitulo());
                ps.setString(3, articulo.getDescripcion());
                ps.setBigDecimal(4, articulo.getPrecio());
                ps.setString(5, articulo.getCategoria());
                ps.setString(6, articulo.getCarrera());
                ps.setString(7, articulo.getLugarEncuentro());

                int filas = ps.executeUpdate();

                if (filas == 0) {
                    con.rollback();
                    return false;
                }

                int idArticulo;

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        idArticulo = rs.getInt(1);
                    } else {
                        con.rollback();
                        return false;
                    }
                }

                if (rutasImagenes != null && !rutasImagenes.isEmpty()) {
                    try (PreparedStatement psImg = con.prepareStatement(sqlImagen)) {
                        for (int i = 0; i < rutasImagenes.size(); i++) {
                            psImg.setInt(1, idArticulo);
                            psImg.setString(2, rutasImagenes.get(i));
                            psImg.setInt(3, i == 0 ? 1 : 0);
                            psImg.addBatch();
                        }

                        psImg.executeBatch();
                    }
                }

                con.commit();
                return true;

            } catch (SQLException e) {
                con.rollback();
                System.out.println("error al registrar articulo: " + e.getMessage());
                return false;
            } finally {
                con.setAutoCommit(true);
            }

        } catch (SQLException e) {
            System.out.println("error de conexion al registrar articulo: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Articulo> getAll() {
        List<Articulo> articulos = new ArrayList<>();

        String sql = "SELECT id_articulo, id_usuario, titulo, descripcion, precio, categoria, carrera, lugar_encuentro, estado, fecha_publicacion " +
                "FROM articulos " +
                "WHERE estado = 'activo' " +
                "ORDER BY fecha_publicacion DESC";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Articulo articulo = mapearArticulo(rs);
                articulo.setImagenPrincipal(obtenerImagenPrincipal(con, articulo.getIdArticulo()));
                articulos.add(articulo);
            }

        } catch (SQLException e) {
            System.out.println("error al obtener articulos: " + e.getMessage());
        }

        return articulos;
    }

    @Override
    public Articulo getById(Integer id) {
        String sql = "SELECT id_articulo, id_usuario, titulo, descripcion, precio, categoria, carrera, lugar_encuentro, estado, fecha_publicacion " +
                "FROM articulos " +
                "WHERE id_articulo = ? AND estado = 'activo'";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Articulo articulo = mapearArticulo(rs);
                    articulo.setImagenPrincipal(obtenerImagenPrincipal(con, articulo.getIdArticulo()));
                    return articulo;
                }
            }

        } catch (SQLException e) {
            System.out.println("error al buscar articulo: " + e.getMessage());
        }

        return null;
    }

    @Override
    public boolean update(Articulo articulo) {
        String sql = "UPDATE articulos SET titulo = ?, descripcion = ?, precio = ?, categoria = ?, carrera = ?, lugar_encuentro = ?, estado = ? " +
                "WHERE id_articulo = ?";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, articulo.getTitulo());
            ps.setString(2, articulo.getDescripcion());
            ps.setBigDecimal(3, articulo.getPrecio());
            ps.setString(4, articulo.getCategoria());
            ps.setString(5, articulo.getCarrera());
            ps.setString(6, articulo.getLugarEncuentro());
            ps.setString(7, articulo.getEstado());
            ps.setInt(8, articulo.getIdArticulo());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("error al actualizar articulo: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "UPDATE articulos SET estado = 'inactivo' WHERE id_articulo = ?";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("error al eliminar articulo: " + e.getMessage());
            return false;
        }
    }

    private String obtenerImagenPrincipal(Connection con, int idArticulo) throws SQLException {
        String sql = "SELECT ruta_imagen " +
                "FROM articulo_imagenes " +
                "WHERE id_articulo = ? " +
                "ORDER BY es_principal DESC, id_imagen ASC " +
                "FETCH FIRST 1 ROWS ONLY";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idArticulo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("ruta_imagen");
                }
            }
        }

        return null;
    }

    private Articulo mapearArticulo(ResultSet rs) throws SQLException {
        Articulo articulo = new Articulo();

        articulo.setIdArticulo(rs.getInt("id_articulo"));
        articulo.setIdUsuario(rs.getInt("id_usuario"));
        articulo.setTitulo(rs.getString("titulo"));
        articulo.setDescripcion(rs.getString("descripcion"));
        articulo.setPrecio(rs.getBigDecimal("precio"));
        articulo.setCategoria(rs.getString("categoria"));
        articulo.setCarrera(rs.getString("carrera"));
        articulo.setLugarEncuentro(rs.getString("lugar_encuentro"));
        articulo.setEstado(rs.getString("estado"));
        articulo.setFechaPublicacion(rs.getTimestamp("fecha_publicacion"));

        return articulo;
    }
}