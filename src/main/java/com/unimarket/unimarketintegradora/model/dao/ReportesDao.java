package com.unimarket.unimarketintegradora.model.dao;

import com.unimarket.unimarketintegradora.model.Reportes;
import com.unimarket.unimarketintegradora.utils.SQLConnector; // Ajusta a la ruta exacta de tu conexion

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Objeto de acceso a datos (DAO) de MUA para la entidad Reportes.
 *
 * @author Equipo UniMarket
 */

public class ReportesDao {

    // Método para obtener todos los reportes
/**
 * Consulta los reportes registrados y los ordena de acuerdo con el criterio definido en la consulta.
 * @return Devuelve una lista de objetos `Reportes` construida a partir de los registros encontrados. Si no existen registros o ocurre un error durante la consulta, se conserva una lista vacía para evitar devolver `null`.
 * @throws SQLException Se produce cuando ocurre un error al establecer la conexión, preparar o ejecutar la consulta SQL.
 */
    public List<Reportes> listarReportes() throws SQLException {
        List<Reportes> lista = new ArrayList<>();
        // Ajusta el nombre de la tabla y columnas si varían en tu BD de MySQL
        String sql = "SELECT * FROM reportes ORDER BY idReporte DESC";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Reportes rep = new Reportes();
                rep.setIdReporte(rs.getInt("idReporte"));
                rep.setMotivo(rs.getString("motivo"));
                rep.setIdUsuarioDenuncianteFk(rs.getString("idUsuarioDenuncianteFk"));
                rep.setIdUsuarioDenunciadoFk(rs.getString("idUsuarioDenunciadoFk"));
                rep.setEstadoReporte(rs.getString("estadoReporte"));

                lista.add(rep);
            }
        }
        return lista;
    }

    // Método para cambiar el estado
/**
 * Actualiza el estado del reporte indicado para reflejar el avance de su atención.
 * @param idReporte Identificador único del reporte cuyo estado se desea actualizar.
 * @param nuevoEstado Nuevo estado que se asignará al registro para reflejar el resultado de la operación.
 * @return No devuelve un valor. La operación modifica la base de datos y, si ocurre un problema de acceso a datos, se propaga la excepción `SQLException` declarada por el método.
 * @throws SQLException Se produce cuando ocurre un error al establecer la conexión, preparar o ejecutar la consulta SQL.
 */
    public void actualizarEstadoReporte(int idReporte, String nuevoEstado) throws SQLException {
        String sql = "UPDATE reportes SET estadoReporte = ? WHERE idReporte = ?";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevoEstado);
            ps.setInt(2, idReporte);
            ps.executeUpdate();
        }
    }
}