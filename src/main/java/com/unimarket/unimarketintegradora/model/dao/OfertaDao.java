package com.unimarket.unimarketintegradora.model.dao;

import com.unimarket.unimarketintegradora.model.Oferta;
import com.unimarket.unimarketintegradora.utils.SQLConnector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OfertaDao implements Dao<Oferta, Integer> {

    // --- 1. CONSULTAS PERSONALIZADAS PARA EL PERFIL ---

    // Ofertas HECHAS POR MÍ (Mis propuestas a otros vendedores)
    public List<Oferta> obtenerOfertasHechasPorUsuario(String matricula) {
        List<Oferta> lista = new ArrayList<>();
        String sql = "SELECT o.*, a.nombre AS nom_articulo, u.nombre AS nom_dueno, " +
                "(SELECT URL_IMAGEN FROM IMAGEN_ARTICULO ia WHERE ia.id_articulo_fk = a.id_articulo FETCH FIRST 1 ROWS ONLY) AS img " +
                "FROM oferta o " +
                "JOIN articulo a ON o.id_articulo_fk = a.id_articulo " +
                "JOIN usuario u ON a.matricula_usuario_fk = u.matricula " +
                "WHERE o.matricula_usuario_fk = ? ORDER BY o.id_oferta DESC";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, matricula);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Oferta of = mapearOferta(rs);
                    of.setNombreUsuario(rs.getString("nom_dueno"));
                    lista.add(of);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en obtenerOfertasHechasPorUsuario: " + e.getMessage());
        }
        return lista;
    }

    // Ofertas RECIBIDAS (Compradores que ofertan en mis artículos)
    public List<Oferta> obtenerOfertasRecibidas(String matriculaVendedor) {
        List<Oferta> lista = new ArrayList<>();
        String sql = "SELECT o.*, a.nombre AS nom_articulo, u.nombre AS nom_comprador, " +
                "(SELECT URL_IMAGEN FROM IMAGEN_ARTICULO ia WHERE ia.id_articulo_fk = a.id_articulo FETCH FIRST 1 ROWS ONLY) AS img " +
                "FROM oferta o " +
                "JOIN articulo a ON o.id_articulo_fk = a.id_articulo " +
                "JOIN usuario u ON o.matricula_usuario_fk = u.matricula " +
                "WHERE a.matricula_usuario_fk = ? ORDER BY o.id_oferta DESC";

        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, matriculaVendedor);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Oferta of = mapearOferta(rs);
                    of.setNombreUsuario(rs.getString("nom_comprador"));
                    lista.add(of);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en obtenerOfertasRecibidas: " + e.getMessage());
        }
        return lista;
    }

    // --- 2. MÉTODOS BÁSICOS IMPLEMENTADOS DE LA INTERFAZ Dao<Oferta, Integer> ---

    @Override
    public boolean create(Oferta oferta) {
        String sql = "INSERT INTO oferta (id_articulo_fk, matricula_usuario_fk, monto, estado) VALUES (?, ?, ?, ?)";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, oferta.getIdArticuloFk());
            ps.setString(2, oferta.getMatriculaUsuarioFk());
            ps.setBigDecimal(3, oferta.getMonto());
            ps.setString(4, oferta.getEstado());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error en save Oferta: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Oferta getById(Integer id) {
        String sql = "SELECT * FROM oferta WHERE id_oferta = ?";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearOfertaBasica(rs);
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
                lista.add(mapearOfertaBasica(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error en getAll Oferta: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean update(Oferta oferta) {
        String sql = "UPDATE oferta SET id_articulo_fk = ?, matricula_usuario_fk = ?, monto = ?, estado = ? WHERE id_oferta = ?";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, oferta.getIdArticuloFk());
            ps.setString(2, oferta.getMatriculaUsuarioFk());
            ps.setBigDecimal(3, oferta.getMonto());
            ps.setString(4, oferta.getEstado());
            ps.setInt(5, oferta.getIdOferta());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error en update Oferta: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM oferta WHERE id_oferta = ?";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error en delete Oferta: " + e.getMessage());
            return false;
        }
    }

    // --- 3. MÉTODOS AUXILIARES DE MAPEO ---

    // Mapeo completo (usado en las listas del perfil con JOIN a artículo e imagen)
    private Oferta mapearOferta(ResultSet rs) throws SQLException {
        Oferta of = new Oferta(
                rs.getInt("id_articulo_fk"),
                rs.getString("matricula_usuario_fk"),
                rs.getBigDecimal("monto"),
                rs.getString("estado")
        );
        of.setIdOferta(rs.getInt("id_oferta"));
        of.setNombreArticulo(rs.getString("nom_articulo"));
        of.setImagenArticulo(rs.getString("img"));
        return of;
    }

    // Mapeo simple (usado en getById y getAll solo para la tabla OFERTA)
    private Oferta mapearOfertaBasica(ResultSet rs) throws SQLException {
        Oferta of = new Oferta(
                rs.getInt("id_articulo_fk"),
                rs.getString("matricula_usuario_fk"),
                rs.getBigDecimal("monto"),
                rs.getString("estado")
        );
        of.setIdOferta(rs.getInt("id_oferta"));
        return of;
    }
}