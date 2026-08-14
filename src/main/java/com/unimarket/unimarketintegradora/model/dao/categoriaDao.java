package com.unimarket.unimarketintegradora.model.dao;

import com.unimarket.unimarketintegradora.model.categoria;
import com.unimarket.unimarketintegradora.utils.SQLConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class categoriaDao {

    public List<categoria> listarCategorias() throws SQLException {
        List<categoria> lista = new ArrayList<>();
        String sql = "SELECT ID_CATEGORIA, CATEGORIA FROM CATEGORIA ORDER BY ID_CATEGORIA ASC";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                categoria cat = new categoria();
                cat.setIdCategoria(rs.getInt("ID_CATEGORIA"));
                cat.setCategoria(rs.getString("CATEGORIA"));
                lista.add(cat);
            }
        }
        return lista;
    }

    public void agregarCategoria(categoria categoria) throws SQLException {

        String sqlMaxId = "SELECT NVL(MAX(ID_CATEGORIA), 0) + 1 AS SIGUIENTE_ID FROM CATEGORIA";
        String sqlInsert = "INSERT INTO CATEGORIA (ID_CATEGORIA, CATEGORIA) VALUES (?, ?)";

        try (Connection con = SQLConnector.getConnection()) {
            int siguienteId = 1;

            try (PreparedStatement psId = con.prepareStatement(sqlMaxId);
                 ResultSet rs = psId.executeQuery()) {
                if (rs.next()) {
                    siguienteId = rs.getInt("SIGUIENTE_ID");
                }
            }

            try (PreparedStatement psInsert = con.prepareStatement(sqlInsert)) {
                psInsert.setInt(1, siguienteId);
                psInsert.setString(2, categoria.getCategoria());
                psInsert.executeUpdate();
            }
        }
    }

    public void eliminarCategoria(int idCategoria) throws SQLException {
        String sql = "DELETE FROM CATEGORIA WHERE ID_CATEGORIA = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCategoria);
            ps.executeUpdate();
        }
    }

    public void editarCategoria(categoria categoria) throws SQLException {
        String sql = "UPDATE CATEGORIA SET CATEGORIA = ? WHERE ID_CATEGORIA = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, categoria.getCategoria());
            ps.setInt(2, categoria.getIdCategoria());
            ps.executeUpdate();
        }
    }

    public void eliminarCategoria(int idCategoria) throws SQLException {
        String sqlBuscarOtros = "SELECT ID_CATEGORIA FROM CATEGORIA WHERE UPPER(CATEGORIA) = 'OTROS'";
        String sqlDelete = "DELETE FROM CATEGORIA WHERE ID_CATEGORIA = ?";

        try (Connection con = SQLConnector.getConnection()) {
            con.setAutoCommit(false); // Iniciar transacción para seguridad

            try {
                int idOtros = -1;

                // 1. Buscar si ya existe la categoría "Otros"
                try (PreparedStatement psBuscar = con.prepareStatement(sqlBuscarOtros);
                     ResultSet rs = psBuscar.executeQuery()) {
                    if (rs.next()) {
                        idOtros = rs.getInt("ID_CATEGORIA");
                    }
                }

                // Si "Otros" no existe y no estamos eliminando la misma "Otros", la creamos
                if (idOtros == -1 && idCategoria != idOtros) {
                    // Usamos la secuencia o MAX+1 según el estándar de tu BD
                    String sqlMaxId = "SELECT NVL(MAX(ID_CATEGORIA), 0) + 1 AS SIGUIENTE_ID FROM CATEGORIA";
                    String sqlInsertOtros = "INSERT INTO CATEGORIA (ID_CATEGORIA, CATEGORIA) VALUES (?, 'Otros')";

                    int nuevoIdOtros = 1;
                    try (PreparedStatement psId = con.prepareStatement(sqlMaxId);
                         ResultSet rsId = psId.executeQuery()) {
                        if (rsId.next()) {
                            nuevoIdOtros = rsId.getInt("SIGUIENTE_ID");
                        }
                    }

                    try (PreparedStatement psIns = con.prepareStatement(sqlInsertOtros)) {
                        psIns.setInt(1, nuevoIdOtros);
                        psIns.executeUpdate();
                    }
                    idOtros = nuevoIdOtros;
                }

                // 2. Reasignar todos los artículos a la categoría "Otros" (usando id_categoria_fk)
                if (idCategoria != idOtros) {
                    String sqlReasignar = "UPDATE articulo SET id_categoria_fk = ? WHERE id_categoria_fk = ?";
                    try (PreparedStatement psReasignar = con.prepareStatement(sqlReasignar)) {
                        psReasignar.setInt(1, idOtros);
                        psReasignar.setInt(2, idCategoria);
                        psReasignar.executeUpdate();
                    } catch (SQLException e) {
                        System.out.println("Aviso reasignación artículos: " + e.getMessage());
                    }
                }

                // 3. Eliminar la categoría deseada de la tabla CATEGORIA
                try (PreparedStatement psDelete = con.prepareStatement(sqlDelete)) {
                    psDelete.setInt(1, idCategoria);
                    psDelete.executeUpdate();
                }

                con.commit(); // Confirmar la transacción

            } catch (SQLException e) {
                con.rollback(); // Cancelar cambios si ocurre un error inesperado
                throw e;
            }
        }
    }
}