package com.unimarket.unimarketintegradora.model.dao;

import com.unimarket.unimarketintegradora.model.Transaccion;
import com.unimarket.unimarketintegradora.model.TransaccionDTO;
import com.unimarket.unimarketintegradora.utils.SQLConnector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Objeto de acceso a datos (DAO) de MUA para la entidad Transaccion.
 *
 * @author Sergio
 */

public class TransaccionDao implements Dao<Transaccion, String> {

/**
 * Registra una nueva entidad en la tabla correspondiente mediante una sentencia INSERT.
 * @param entidad Objeto de la entidad que contiene los datos que se almacenarán o actualizarán en la base de datos.
 * @return Devuelve `true` porque la sentencia SQL afectó al menos un registro, lo que indica que la operación se realizó correctamente. Devuelve `false` cuando no se modificó ningún registro o cuando ocurre una excepción de base de datos.
 */
    @Override
    public boolean create(Transaccion entidad) {
        String sql = "INSERT INTO transaccion (id_articulo_fk, MATRICULA_vendedor_fk, MATRICULA_comprador_fk, monto_final, fecha_transaccion) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, entidad.getIdArticuloFk());
            ps.setString(2, entidad.getIdUsuarioVendedorFk());
            ps.setString(3, entidad.getIdUsuarioCompradorFk());
            ps.setBigDecimal(4, entidad.getMontoFinal());
            ps.setDate(5, entidad.getFechaTransaccion());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al registrar transaccion: " + e.getMessage());
            return false;
        }
    }
    // 1. Crear la transacción con estado PENDIENTE (Sin SEQ_TRANSACCION)
/**
 * Registra una transacción con estado PENDIENTE para conservar la operación hasta que sea confirmada o cancelada.
 * @param idArticulo Identificador único del artículo sobre el que se realizará la consulta, validación, eliminación o modificación.
 * @param matriculaVendedor Matrícula del usuario que actúa como vendedor.
 * @param matriculaComprador Matrícula del usuario que actúa como comprador.
 * @param montoFinal Monto final de la transacción que se registrará como precio acordado entre comprador y vendedor.
 * @return Devuelve `true` porque la sentencia SQL afectó al menos un registro, lo que indica que la operación se realizó correctamente. Devuelve `false` cuando no se modificó ningún registro o cuando ocurre una excepción de base de datos.
 */
    public boolean crearTransaccionPendiente(int idArticulo, String matriculaVendedor, String matriculaComprador, java.math.BigDecimal montoFinal) {
        String sql = "INSERT INTO transaccion (id_transaccion, id_articulo_fk, MATRICULA_vendedor_fk, MATRICULA_comprador_fk, monto_final, fecha_transaccion, estado) " +
                "VALUES ((SELECT COALESCE(MAX(id_transaccion), 0) + 1 FROM transaccion), ?, ?, ?, ?, SYSDATE, 'PENDIENTE')";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idArticulo);
            ps.setString(2, matriculaVendedor);
            ps.setString(3, matriculaComprador);
            ps.setBigDecimal(4, montoFinal);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al registrar transacción pendiente: " + e.getMessage());
            return false;
        }
    }


/**
 * Consulta y obtiene todos los registros disponibles de la entidad, mapeando cada fila de la base de datos a su objeto correspondiente.
 * @return Devuelve una lista de objetos `Transaccion` construida a partir de los registros encontrados. Si no existen registros o ocurre un error durante la consulta, se conserva una lista vacía para evitar devolver `null`.
 */
    @Override
    public List<Transaccion> getAll() {
        List<Transaccion> lista = new ArrayList<>();
        String sql = "SELECT * FROM transaccion";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Transaccion t = new Transaccion(rs.getInt("id_articulo_fk"), rs.getString("id_usuario_vendedor_fk"), rs.getString("id_usuario_comprador_fk"), rs.getBigDecimal("monto_final"), rs.getDate("fecha_transaccion"));
                t.setIdTransaccion(rs.getInt("id_transaccion"));
                lista.add(t);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar transacciones: " + e.getMessage());
        }
        return lista;
    }

/**
 * Busca un registro específico utilizando su identificador y, si existe, lo convierte a la entidad correspondiente.
 * @param id Identificador único del registro que se desea consultar, actualizar o eliminar.
 * @return Devuelve un objeto `Transaccion` con los datos recuperados de la base de datos cuando existe un registro que coincide con el identificador o criterio recibido. Si no se encuentra ningún registro, devuelve `null`.
 */
    @Override
    public Transaccion getById(String id) {
        String sql = "SELECT * FROM transaccion WHERE id_transaccion = ?";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Transaccion t = new Transaccion(rs.getInt("id_articulo_fk"), rs.getString("id_usuario_vendedor_fk"), rs.getString("id_usuario_comprador_fk"), rs.getBigDecimal("monto_final"), rs.getDate("fecha_transaccion"));
                    t.setIdTransaccion(rs.getInt("id_transaccion"));
                    return t;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar transaccion: " + e.getMessage());
        }
        return null;
    }

/**
 * Actualiza los datos de una entidad existente utilizando su identificador como referencia.
 * @param entidad Objeto de la entidad que contiene los datos que se almacenarán o actualizarán en la base de datos.
 * @return Devuelve un valor booleano que indica si la operación se realizó correctamente; `true` representa éxito y `false` representa que no se pudo completar la operación.
 */
    @Override
    public boolean update(Transaccion entidad) { return false; } // Las transacciones son registros históricos, rara vez se actualizan.

/**
 * Elimina o realiza la baja lógica del registro identificado, de acuerdo con las reglas definidas para la entidad.
 * @param id Identificador único del registro que se desea consultar, actualizar o eliminar.
 * @return Devuelve un valor booleano que indica si la operación se realizó correctamente; `true` representa éxito y `false` representa que no se pudo completar la operación.
 */
    @Override
    public boolean delete(String id) { return false; } // Por auditoría, no se suelen borrar.
/**
 * Construye el historial de actividad del usuario, integrando las operaciones en las que participó como comprador o vendedor.
 * @param matriculaUsuario Matrícula del usuario cuyos artículos, historial o datos se desean consultar.
 * @return Devuelve una lista de objetos `TransaccionDTO` construida a partir de los registros encontrados. Si no existen registros o ocurre un error durante la consulta, se conserva una lista vacía para evitar devolver `null`.
 */
    public List<TransaccionDTO> obtenerHistorialActividad(String matriculaUsuario) {
        List<TransaccionDTO> historial = new ArrayList<>();

        String sql =
                // 1. OBTENER LAS COMPRAS DEL USUARIO
                "SELECT 'COMPRA' AS tipo, a.nombre AS tituloArticulo, t.monto_final AS precio, t.fecha_transaccion AS fecha, u.nombre AS contraparte " +
                        "FROM transaccion t " +
                        "JOIN articulo a ON t.id_articulo_fk = a.id_articulo " +
                        "JOIN usuario u ON t.MATRICULA_vendedor_fk = u.MATRICULA " +
                        "WHERE t.MATRICULA_comprador_fk = ? " +
                        "UNION ALL " +
                        // 2. OBTENER LAS VENTAS DEL USUARIO
                        "SELECT 'VENTA' AS tipo, a.nombre AS tituloArticulo, t.monto_final AS precio, t.fecha_transaccion AS fecha, u.nombre AS contraparte " +
                        "FROM transaccion t " +
                        "JOIN articulo a ON t.id_articulo_fk = a.id_articulo " +
                        "JOIN usuario u ON t.MATRICULA_comprador_fk = u.MATRICULA " +
                        "WHERE t.MATRICULA_vendedor_fk = ? " +
                        "ORDER BY fecha DESC";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Asignamos la matrícula para la consulta de COMPRAS
            ps.setString(1, matriculaUsuario);
            // Asignamos la misma matrícula para la consulta de VENTAS
            ps.setString(2, matriculaUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    TransaccionDTO transaccion = new TransaccionDTO(
                            rs.getString("tipo"),
                            rs.getString("tituloArticulo"),
                            rs.getBigDecimal("precio"),
                            rs.getTimestamp("fecha"),
                            rs.getString("contraparte")
                    );
                    historial.add(transaccion);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener historial de actividad: " + e.getMessage());
        }

        return historial;
    }
/**
 * Consulta el correo institucional asociado a la matrícula proporcionada.
 * @param matriculaVendedor Matrícula del usuario que actúa como vendedor.
 * @return Devuelve la cadena obtenida de la columna consultada para el registro indicado. Si no existe un registro que coincida o la consulta no puede obtener el dato, devuelve `null`.
 */
    public String obtenerCorreoVendedor(String matriculaVendedor) {
        String sql = "SELECT correo_institucional FROM usuario WHERE matricula = ?";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, matriculaVendedor);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String correo = rs.getString("correo_institucional");
                    return correo != null ? correo.trim() : null;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener correo del vendedor: " + e.getMessage());
        }
        return null;
    }

    // Igual que obtenerCorreoVendedor pero para el número de celular (usado para WhatsApp)
/**
 * Consulta el número telefónico registrado para la matrícula proporcionada.
 * @param matricula Matrícula del usuario al que corresponde la operación.
 * @return Devuelve la cadena obtenida de la columna consultada para el registro indicado. Si no existe un registro que coincida o la consulta no puede obtener el dato, devuelve `null`.
 */
    public String obtenerTelefonoUsuario(String matricula) {
        String sql = "SELECT numero_celular FROM usuario WHERE matricula = ?";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, matricula);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String telefono = rs.getString("numero_celular");
                    return telefono != null ? telefono.trim() : null;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener teléfono del usuario: " + e.getMessage());
        }
        return null;
    }
/**
 * Cuenta las transacciones completadas en las que el usuario indicado participa como vendedor.
 * @param matriculaVendedor Matrícula del usuario que actúa como vendedor.
 * @return Devuelve el número de registros que cumplen la condición de la consulta; si no existen registros que coincidan, el resultado es `0`.
 */
    public int contarVentasCompletadas(String matriculaVendedor) {
        String sql = "SELECT COUNT(*) FROM transaccion WHERE MATRICULA_vendedor_fk = ? AND UPPER(estado) = 'COMPLETADO'";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, matriculaVendedor);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error al contar ventas completadas: " + e.getMessage());
        }
        return 0;
    }
/**
 * Cuenta las transacciones completadas en las que el usuario indicado participa como comprador.
 * @param matriculaComprador Matrícula del usuario que actúa como comprador.
 * @return Devuelve el número de registros que cumplen la condición de la consulta; si no existen registros que coincidan, el resultado es `0`.
 */
    public int contarComprasCompletadas(String matriculaComprador) {
        String sql = "SELECT COUNT(*) FROM transaccion WHERE MATRICULA_comprador_fk = ? AND UPPER(estado) = 'COMPLETADO'";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, matriculaComprador);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error al contar compras completadas: " + e.getMessage());
        }
        return 0;
    }
/**
 * Comprueba si el artículo indicado tiene al menos una transacción con estado COMPLETADO.
 * @param idArticulo Identificador único del artículo sobre el que se realizará la consulta, validación, eliminación o modificación.
 * @return Devuelve un valor booleano que indica si la operación se realizó correctamente; `true` representa éxito y `false` representa que no se pudo completar la operación.
 */
    public boolean tieneTransaccionCompletada(int idArticulo) {
        String sql = "SELECT COUNT(*) FROM transaccion WHERE id_articulo_fk = ? AND UPPER(estado) = 'COMPLETADO'";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idArticulo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error en tieneTransaccionCompletada: " + e.getMessage());
        }
        return false;
    }
/**
 * Obtiene las matrículas de los compradores relacionados con transacciones del artículo que todavía no han sido completadas.
 * @param idArticulo Identificador único del artículo sobre el que se realizará la consulta, validación, eliminación o modificación.
 * @return Devuelve una lista de objetos `String` construida a partir de los registros encontrados. Si no existen registros o ocurre un error durante la consulta, se conserva una lista vacía para evitar devolver `null`.
 */
    public List<String> obtenerCompradoresTransaccionesIncompletas(int idArticulo) {
        List<String> compradores = new ArrayList<>();
        String sql = "SELECT MATRICULA_comprador_fk FROM transaccion WHERE id_articulo_fk = ? AND UPPER(estado) != 'COMPLETADO'";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idArticulo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) compradores.add(rs.getString(1));
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar afectados incompletos: " + e.getMessage());
        }
        return compradores;
    }
/**
 * Elimina las transacciones no completadas asociadas al artículo indicado para liberar la publicación.
 * @param idArticulo Identificador único del artículo sobre el que se realizará la consulta, validación, eliminación o modificación.
 * @return Devuelve `true` porque la sentencia SQL afectó al menos un registro, lo que indica que la operación se realizó correctamente. Devuelve `false` cuando no se modificó ningún registro o cuando ocurre una excepción de base de datos.
 */
    public boolean eliminarTransaccionesIncompletas(int idArticulo) {
        String sql = "DELETE FROM transaccion WHERE id_articulo_fk = ? AND UPPER(estado) != 'COMPLETADO'";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idArticulo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar transacciones incompletas: " + e.getMessage());
            return false;
        }
    }
}