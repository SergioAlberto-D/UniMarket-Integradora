package com.unimarket.unimarketintegradora.model.dao;

import com.unimarket.unimarketintegradora.model.Oferta;
import com.unimarket.unimarketintegradora.utils.SQLConnector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OfertaDao implements Dao<Oferta, Integer> {

    // --- 1. MÉTODOS DE LA INTERFAZ Dao<Oferta, Integer> ---

    @Override
    public boolean create(Oferta entidad) {
        String sql = "INSERT INTO oferta (id_oferta, id_articulo_fk, matricula_usuario_fk, monto, estado) " +
                "VALUES ((SELECT NVL(MAX(id_oferta), 0) + 1 FROM oferta), ?, ?, ?, ?)";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, entidad.getIdArticuloFk());
            ps.setString(2, entidad.getMatriculaUsuarioFk());
            ps.setBigDecimal(3, entidad.getMonto());
            ps.setString(4, entidad.getEstado());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al crear oferta: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Oferta getById(Integer id) {
        String sql = "SELECT * FROM oferta WHERE id_oferta = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearOferta(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en getById Oferta: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Oferta> getAll() {
        List<Oferta> lista = new ArrayList<>();
        String sql = "SELECT * FROM oferta ORDER BY id_oferta DESC";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearOferta(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error en getAll Oferta: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean update(Oferta entidad) {
        String sql = "UPDATE oferta SET id_articulo_fk = ?, matricula_usuario_fk = ?, monto = ?, estado = ? WHERE id_oferta = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, entidad.getIdArticuloFk());
            ps.setString(2, entidad.getMatriculaUsuarioFk());
            ps.setBigDecimal(3, entidad.getMonto());
            ps.setString(4, entidad.getEstado());
            ps.setInt(5, entidad.getIdOferta());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar oferta: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM oferta WHERE id_oferta = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar oferta: " + e.getMessage());
            return false;
        }
    }

    // --- 2. VALIDACIÓN REGLA DE ORO: UN USUARIO = UNA OFERTA POR ARTÍCULO ---
    public boolean existeOfertaPrevia(int idArticulo, String matriculaUsuario) {
        // Valida si el usuario ya tiene una oferta PENDIENTE para este mismo artículo
        String sql = "SELECT COUNT(*) FROM oferta WHERE id_articulo_fk = ? AND matricula_usuario_fk = ? AND UPPER(estado) = 'PENDIENTE'";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idArticulo);
            ps.setString(2, matriculaUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en existeOfertaPrevia: " + e.getMessage());
        }
        return false;
    }

    // --- 3. MAPEO AUXILIAR ---
    private Oferta mapearOferta(ResultSet rs) throws SQLException {
        Oferta o = new Oferta(
                rs.getInt("ID_ARTICULO_FK"),
                rs.getString("MATRICULA_USUARIO_FK"),
                rs.getBigDecimal("MONTO"),
                rs.getString("ESTADO")
        );
        o.setIdOferta(rs.getInt("ID_OFERTA"));
        return o;
    }
    public List<Oferta> obtenerOfertasHechasPorUsuario(String matriculaComprador) {
        List<Oferta> lista = new ArrayList<>();
        String sql = "SELECT o.*, " +
                "       a.nombre AS nombre_articulo, " +
                "       u.nombre AS nombre_vendedor, " +
                "       (SELECT URL_IMAGEN FROM IMAGEN_ARTICULO ia WHERE ia.id_articulo_fk = a.id_articulo FETCH FIRST 1 ROWS ONLY) AS portada " +
                "FROM OFERTA o " +
                "JOIN ARTICULO a ON o.id_articulo_fk = a.id_articulo " +
                "JOIN USUARIO u ON a.matricula_usuario_fk = u.matricula " +
                "WHERE o.matricula_usuario_fk = ? " +
                "AND UPPER(o.estado) = 'PENDIENTE' " +
                "ORDER BY o.id_oferta DESC";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, matriculaComprador);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Oferta of = mapearOferta(rs);
                    of.setNombreArticulo(rs.getString("nombre_articulo"));
                    of.setNombreUsuario(rs.getString("nombre_vendedor")); // Guardamos el nombre del vendedor
                    of.setImagenArticulo(rs.getString("portada"));
                    lista.add(of);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en obtenerOfertasHechasPorUsuario: " + e.getMessage());
        }
        return lista;
    }
    public List<Oferta> obtenerOfertasRecibidas(String matriculaVendedor) {
        List<Oferta> lista = new ArrayList<>();
        String sql = "SELECT o.*, " +
                "       a.nombre AS nombre_articulo, " +
                "       u.nombre AS nombre_comprador, " +
                "       (SELECT URL_IMAGEN FROM IMAGEN_ARTICULO ia WHERE ia.id_articulo_fk = a.id_articulo FETCH FIRST 1 ROWS ONLY) AS portada " +
                "FROM OFERTA o " +
                "JOIN ARTICULO a ON o.id_articulo_fk = a.id_articulo " +
                "JOIN USUARIO u ON o.matricula_usuario_fk = u.matricula " +
                "WHERE a.matricula_usuario_fk = ? " +
                "AND UPPER(o.estado) = 'PENDIENTE' " +
                "ORDER BY o.id_oferta DESC";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, matriculaVendedor);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Oferta of = mapearOferta(rs);
                    of.setNombreArticulo(rs.getString("nombre_articulo"));
                    of.setNombreUsuario(rs.getString("nombre_comprador")); // Guardamos el nombre de quien ofertó
                    of.setImagenArticulo(rs.getString("portada"));
                    lista.add(of);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en obtenerOfertasRecibidas: " + e.getMessage());
        }
        return lista;
    }
    // 1. Cambiar el estado de una oferta (ACEPTADA, RECHAZADA, CANCELADA)
    public boolean cambiarEstado(int idOferta, String nuevoEstado) {
        String sql = "UPDATE oferta SET estado = ? WHERE id_oferta = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idOferta);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al cambiar estado de oferta: " + e.getMessage());
            return false;
        }
    }

    // 2. Obtener los datos clave para armar la notificación (título, comprador y vendedor)
    public Oferta obtenerDetalleParaNotificacion(int idOferta) {
        String sql = "SELECT o.id_oferta, o.id_articulo_fk, o.monto, o.estado, o.matricula_usuario_fk AS comprador, " +
                "       a.nombre AS nombre_articulo, a.matricula_usuario_fk AS vendedor, " +
                "       (SELECT URL_IMAGEN FROM IMAGEN_ARTICULO ia WHERE ia.id_articulo_fk = a.id_articulo FETCH FIRST 1 ROWS ONLY) AS portada " +
                "FROM oferta o " +
                "JOIN articulo a ON o.id_articulo_fk = a.id_articulo " +
                "WHERE o.id_oferta = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idOferta);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Oferta of = new Oferta();
                    of.setIdOferta(rs.getInt("id_oferta"));
                    of.setIdArticuloFk(rs.getInt("id_articulo_fk"));
                    of.setMonto(rs.getBigDecimal("monto"));
                    of.setEstado(rs.getString("estado"));
                    of.setMatriculaUsuarioFk(rs.getString("comprador"));
                    of.setNombreArticulo(rs.getString("nombre_articulo"));
                    of.setNombreUsuario(rs.getString("vendedor")); // Usamos este campo para guardar temporalmente al vendedor
                    of.setImagenArticulo(rs.getString("portada"));
                    return of;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en obtenerDetalleParaNotificacion: " + e.getMessage());
        }
        return null;
    }
    public boolean eliminarOferta(int idOferta) {
        String sql = "DELETE FROM oferta WHERE id_oferta = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idOferta);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar oferta: " + e.getMessage());
            return false;
        }
    }
    public List<String> obtenerCompradoresOfertasActivas(int idArticulo) {
        List<String> compradores = new ArrayList<>();
        String sql = "SELECT matricula_usuario_fk FROM oferta WHERE id_articulo_fk = ? AND UPPER(estado) IN ('PENDIENTE', 'ACEPTADA')";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idArticulo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) compradores.add(rs.getString(1));
            }
        } catch (SQLException e) {
            System.out.println("Error en obtenerCompradoresOfertasActivas: " + e.getMessage());
        }
        return compradores;
    }

    public boolean eliminarOfertasNoCompletadas(int idArticulo) {
        String sql = "DELETE FROM oferta WHERE id_articulo_fk = ? AND UPPER(estado) IN ('PENDIENTE', 'ACEPTADA')";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idArticulo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar ofertas no completadas: " + e.getMessage());
            return false;
        }
    }
}