package com.unimarket.unimarketintegradora.model.dao;

import com.unimarket.unimarketintegradora.model.Transaccion;
import com.unimarket.unimarketintegradora.model.TransaccionDTO;
import com.unimarket.unimarketintegradora.utils.SQLConnector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransaccionDao implements Dao<Transaccion, String> {

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

    @Override
    public boolean update(Transaccion entidad) { return false; } // Las transacciones son registros históricos, rara vez se actualizan.

    @Override
    public boolean delete(String id) { return false; } // Por auditoría, no se suelen borrar.

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