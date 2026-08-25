package com.unimarket.unimarketintegradora.model.dao;

import com.unimarket.unimarketintegradora.model.Oferta;
import com.unimarket.unimarketintegradora.utils.SQLConnector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Objeto de acceso a datos (DAO) de MUA para la entidad Oferta.
 *
 * @author Sergio
 */

public class OfertaDao implements Dao<Oferta, Integer> {

    // --- 1. MÉTODOS DE LA INTERFAZ Dao<Oferta, Integer> ---

/**
 * Registra una nueva entidad en la tabla correspondiente mediante una sentencia INSERT.
 * @param entidad Objeto de la entidad que contiene los datos que se almacenarán o actualizarán en la base de datos.
 * @return Devuelve `true` porque la sentencia SQL afectó al menos un registro, lo que indica que la operación se realizó correctamente. Devuelve `false` cuando no se modificó ningún registro o cuando ocurre una excepción de base de datos.
 */
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

/**
 * Busca un registro específico utilizando su identificador y, si existe, lo convierte a la entidad correspondiente.
 * @param id Identificador único del registro que se desea consultar, actualizar o eliminar.
 * @return Devuelve un objeto `Oferta` con los datos recuperados de la base de datos cuando existe un registro que coincide con el identificador o criterio recibido. Si no se encuentra ningún registro, devuelve `null`.
 */
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

/**
 * Consulta y obtiene todos los registros disponibles de la entidad, mapeando cada fila de la base de datos a su objeto correspondiente.
 * @return Devuelve una lista de objetos `Oferta` construida a partir de los registros encontrados. Si no existen registros o ocurre un error durante la consulta, se conserva una lista vacía para evitar devolver `null`.
 */
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

/**
 * Actualiza los datos de una entidad existente utilizando su identificador como referencia.
 * @param entidad Objeto de la entidad que contiene los datos que se almacenarán o actualizarán en la base de datos.
 * @return Devuelve `true` porque la sentencia SQL afectó al menos un registro, lo que indica que la operación se realizó correctamente. Devuelve `false` cuando no se modificó ningún registro o cuando ocurre una excepción de base de datos.
 */
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

/**
 * Elimina o realiza la baja lógica del registro identificado, de acuerdo con las reglas definidas para la entidad.
 * @param id Identificador único del registro que se desea consultar, actualizar o eliminar.
 * @return Devuelve `true` porque la sentencia SQL afectó al menos un registro, lo que indica que la operación se realizó correctamente. Devuelve `false` cuando no se modificó ningún registro o cuando ocurre una excepción de base de datos.
 */
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
/**
 * Comprueba si el usuario ya tiene una oferta registrada para el artículo indicado.
 * @param idArticulo Identificador único del artículo sobre el que se realizará la consulta, validación, eliminación o modificación.
 * @param matriculaUsuario Matrícula del usuario cuyos artículos, historial o datos se desean consultar.
 * @return Devuelve un valor booleano que indica si la operación se realizó correctamente; `true` representa éxito y `false` representa que no se pudo completar la operación.
 */
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
/**
 * Obtiene las ofertas realizadas por el usuario como comprador.
 * @param matriculaComprador Matrícula del usuario que actúa como comprador.
 * @return Devuelve una lista de objetos `Oferta` construida a partir de los registros encontrados. Si no existen registros o ocurre un error durante la consulta, se conserva una lista vacía para evitar devolver `null`.
 */
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
/**
 * Obtiene las ofertas recibidas por el usuario como vendedor.
 * @param matriculaVendedor Matrícula del usuario que actúa como vendedor.
 * @return Devuelve una lista de objetos `Oferta` construida a partir de los registros encontrados. Si no existen registros o ocurre un error durante la consulta, se conserva una lista vacía para evitar devolver `null`.
 */
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
/**
 * Actualiza el estado del registro indicado para reflejar su nueva situación dentro del flujo del sistema.
 * @param idOferta Identificador único de la oferta que se desea consultar o modificar.
 * @param nuevoEstado Nuevo estado que se asignará al registro para reflejar el resultado de la operación.
 * @return Devuelve `true` porque la sentencia SQL afectó al menos un registro, lo que indica que la operación se realizó correctamente. Devuelve `false` cuando no se modificó ningún registro o cuando ocurre una excepción de base de datos.
 */
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
/**
 * Obtiene los datos de la oferta necesarios para construir una notificación relacionada con ella.
 * @param idOferta Identificador único de la oferta que se desea consultar o modificar.
 * @return Devuelve un objeto `Oferta` con los datos recuperados de la base de datos cuando existe un registro que coincide con el identificador o criterio recibido. Si no se encuentra ningún registro, devuelve `null`.
 */
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
/**
 * Elimina la oferta indicada de la base de datos.
 * @param idOferta Identificador único de la oferta que se desea consultar o modificar.
 * @return Devuelve `true` porque la sentencia SQL afectó al menos un registro, lo que indica que la operación se realizó correctamente. Devuelve `false` cuando no se modificó ningún registro o cuando ocurre una excepción de base de datos.
 */
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
/**
 * Obtiene las matrículas de los usuarios que tienen ofertas activas sobre el artículo indicado.
 * @param idArticulo Identificador único del artículo sobre el que se realizará la consulta, validación, eliminación o modificación.
 * @return Devuelve una lista de objetos `String` construida a partir de los registros encontrados. Si no existen registros o ocurre un error durante la consulta, se conserva una lista vacía para evitar devolver `null`.
 */
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
/**
 * Elimina las ofertas que todavía no han sido completadas para el artículo indicado.
 * @param idArticulo Identificador único del artículo sobre el que se realizará la consulta, validación, eliminación o modificación.
 * @return Devuelve `true` porque la sentencia SQL afectó al menos un registro, lo que indica que la operación se realizó correctamente. Devuelve `false` cuando no se modificó ningún registro o cuando ocurre una excepción de base de datos.
 */
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