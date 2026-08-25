package com.unimarket.unimarketintegradora.model.dao;

import com.unimarket.unimarketintegradora.model.Comentario;
import com.unimarket.unimarketintegradora.utils.SQLConnector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Objeto de acceso a datos (DAO) de MUA para la entidad Comentario.
 *
 * @author Segio
 */

public class ComentarioDao implements Dao<Comentario, Integer> {

/**
 * Registra una nueva entidad en la tabla correspondiente mediante una sentencia INSERT.
 * @param c Objeto Comentario con la información del comentario que se desea registrar o actualizar.
 * @return Devuelve `true` porque la sentencia SQL afectó al menos un registro, lo que indica que la operación se realizó correctamente. Devuelve `false` cuando no se modificó ningún registro o cuando ocurre una excepción de base de datos.
 */
    @Override
    public boolean create(Comentario c) {
        // Usamos SEQ_COMENTARIO.NEXTVAL y SYSDATE, omitiendo totalmente el artículo
        String sql = "INSERT INTO comentario (id_comentario, comentario, calificacion, matricula_remitente_fk, matricula_receptor_fk, fecha_comentario) " +
                "VALUES (SEQ_COMENTARIO.NEXTVAL, ?, ?, ?, ?, SYSDATE)";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getComentario());
            ps.setInt(2, c.getCalificacion());
            ps.setString(3, c.getIdUsuarioRemitenteFk());
            ps.setString(4, c.getIdUsuarioReceptorFk());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error en create Comentario: " + e.getMessage());
            return false;
        }
    }

/**
 * Busca un registro específico utilizando su identificador y, si existe, lo convierte a la entidad correspondiente.
 * @param id Identificador único del registro que se desea consultar, actualizar o eliminar.
 * @return Devuelve un objeto `Comentario` con los datos recuperados de la base de datos cuando existe un registro que coincide con el identificador o criterio recibido. Si no se encuentra ningún registro, devuelve `null`.
 */
    @Override
    public Comentario getById(Integer id) {
        String sql = "SELECT * FROM comentario WHERE id_comentario = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearComentarioBasico(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en getById Comentario: " + e.getMessage());
        }
        return null;
    }

/**
 * Consulta y obtiene todos los registros disponibles de la entidad, mapeando cada fila de la base de datos a su objeto correspondiente.
 * @return Devuelve una lista de objetos `Comentario` construida a partir de los registros encontrados. Si no existen registros o ocurre un error durante la consulta, se conserva una lista vacía para evitar devolver `null`.
 */
    @Override
    public List<Comentario> getAll() {
        List<Comentario> lista = new ArrayList<>();
        String sql = "SELECT * FROM comentario ORDER BY id_comentario DESC";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearComentarioBasico(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error en getAll Comentario: " + e.getMessage());
        }
        return lista;
    }

/**
 * Actualiza los datos de una entidad existente utilizando su identificador como referencia.
 * @param c Objeto Comentario con la información del comentario que se desea registrar o actualizar.
 * @return Devuelve `true` porque la sentencia SQL afectó al menos un registro, lo que indica que la operación se realizó correctamente. Devuelve `false` cuando no se modificó ningún registro o cuando ocurre una excepción de base de datos.
 */
    @Override
    public boolean update(Comentario c) {
        String sql = "UPDATE comentario SET comentario = ?, calificacion = ?, matricula_remitente_fk = ?, matricula_receptor_fk = ? WHERE id_comentario = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getComentario());
            ps.setInt(2, c.getCalificacion());
            ps.setString(3, c.getIdUsuarioRemitenteFk());
            ps.setString(4, c.getIdUsuarioReceptorFk());
            ps.setInt(5, c.getIdComentario());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error en update Comentario: " + e.getMessage());
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
        String sql = "DELETE FROM comentario WHERE id_comentario = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error en delete Comentario: " + e.getMessage());
            return false;
        }
    }
/**
 * Obtiene los comentarios asociados al vendedor indicado para mostrar sus opiniones y calificaciones.
 * @param matriculaVendedor Matrícula del usuario que actúa como vendedor.
 * @return Devuelve una lista de objetos `Comentario` construida a partir de los registros encontrados. Si no existen registros o ocurre un error durante la consulta, se conserva una lista vacía para evitar devolver `null`.
 */
    public List<Comentario> obtenerPorVendedor(String matriculaVendedor) {
        List<Comentario> lista = new ArrayList<>();
        String sql = "SELECT c.*, u.nombre AS nombre_remitente, u.apellido_paterno, u.foto_perfil AS foto_remitente " +
                "FROM comentario c " +
                "JOIN usuario u ON c.matricula_remitente_fk = u.matricula " +
                "WHERE c.matricula_receptor_fk = ? ORDER BY c.fecha_comentario DESC";

        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, matriculaVendedor);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Comentario c = mapearComentarioBasico(rs);
                    c.setFechaComentario(rs.getTimestamp("fecha_comentario"));
                    c.setNombreRemitente(rs.getString("nombre_remitente") + " " + rs.getString("apellido_paterno"));
                    c.setFotoRemitente(rs.getString("foto_remitente"));
                    lista.add(c);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener comentarios: " + e.getMessage());
        }
        return lista;
    }
/**
 * Cuenta la cantidad de comentarios realizados por el usuario indicado.
 * @param matriculaRemitente Matrícula del usuario que realizó los comentarios.
 * @return Devuelve el número de registros que cumplen la condición de la consulta; si no existen registros que coincidan, el resultado es `0`.
 */
    public int contarComentariosRealizados(String matriculaRemitente) {
        String sql = "SELECT COUNT(*) FROM comentario WHERE matricula_remitente_fk = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, matriculaRemitente);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en contarComentariosRealizados: " + e.getMessage());
        }
        return 0;
    }

    private Comentario mapearComentarioBasico(ResultSet rs) throws SQLException {
        Comentario c = new Comentario(
                rs.getString("comentario"),
                rs.getInt("calificacion"),
                rs.getString("matricula_remitente_fk"),
                rs.getString("matricula_receptor_fk")
        );
        c.setIdComentario(rs.getInt("id_comentario"));
        try {
            c.setFechaComentario(rs.getTimestamp("fecha_comentario"));
        } catch (Exception ignored) {}
        return c;
    }
}