package com.unimarket.unimarketintegradora.model.dao;

import com.unimarket.unimarketintegradora.model.Notificacion;
import com.unimarket.unimarketintegradora.utils.SQLConnector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Objeto de acceso a datos (DAO) de MUA para la entidad Notificacion.
 *
 * @author Sergio
 */

public class NotificacionDao {

    // 1. Registrar una nueva notificación en la base de datos
/**
 * Registra una nueva notificación asociada a un usuario y la deja disponible para su consulta.
 * @param matriculaDestino Matrícula del usuario que recibirá la notificación.
 * @param mensaje Contenido textual de la notificación que se mostrará al usuario.
 * @param tipo Tipo de notificación utilizado para clasificar el aviso dentro del sistema.
 * @return Devuelve `true` porque la sentencia SQL afectó al menos un registro, lo que indica que la operación se realizó correctamente. Devuelve `false` cuando no se modificó ningún registro o cuando ocurre una excepción de base de datos.
 */
    public boolean crearNotificacion(String matriculaDestino, String mensaje, String tipo) {
        String sql = "INSERT INTO notificacion (id_notificacion, matricula_usuario_fk, mensaje, tipo, leida) " +
                "VALUES ((SELECT NVL(MAX(id_notificacion), 0) + 1 FROM notificacion), ?, ?, ?, 0)";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, matriculaDestino);
            ps.setString(2, mensaje);
            ps.setString(3, tipo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al crear notificación: " + e.getMessage());
            return false;
        }
    }

    // 2. Obtener las últimas 10 notificaciones NO LEÍDAS de un usuario
/**
 * Obtiene las notificaciones no leídas del usuario indicado, respetando el límite y orden definidos por la consulta.
 * @param matricula Matrícula del usuario al que corresponde la operación.
 * @return Devuelve una lista de objetos `Notificacion` construida a partir de los registros encontrados. Si no existen registros o ocurre un error durante la consulta, se conserva una lista vacía para evitar devolver `null`.
 */
    public List<Notificacion> obtenerNoLeidas(String matricula) {
        List<Notificacion> lista = new ArrayList<>();
        String sql = "SELECT * FROM notificacion " +
                "WHERE matricula_usuario_fk = ? AND leida = 0 " +
                "ORDER BY id_notificacion DESC FETCH FIRST 10 ROWS ONLY";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, matricula);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Notificacion n = new Notificacion();
                    n.setIdNotificacion(rs.getInt("id_notificacion"));
                    n.setMatriculaUsuarioFk(rs.getString("matricula_usuario_fk"));
                    n.setMensaje(rs.getString("mensaje"));
                    n.setTipo(rs.getString("tipo"));
                    n.setLeida(rs.getInt("leida"));
                    n.setTiempoTranscurrido("Reciente"); // Puedes formatear FECHA_CREACION si gustas
                    lista.add(n);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener notificaciones: " + e.getMessage());
        }
        return lista;
    }

    // 3. Marcar todas como leídas
/**
 * Actualiza las notificaciones pendientes del usuario para marcarlas como leídas.
 * @param matricula Matrícula del usuario al que corresponde la operación.
 * @return Devuelve `true` porque la sentencia SQL afectó al menos un registro, lo que indica que la operación se realizó correctamente. Devuelve `false` cuando no se modificó ningún registro o cuando ocurre una excepción de base de datos.
 */
    public boolean marcarTodasComoLeidas(String matricula) {
        String sql = "UPDATE notificacion SET leida = 1 WHERE matricula_usuario_fk = ? AND leida = 0";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, matricula);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al marcar como leídas: " + e.getMessage());
            return false;
        }
    }
}