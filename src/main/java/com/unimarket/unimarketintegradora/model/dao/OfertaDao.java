package com.unimarket.unimarketintegradora.model.dao;

import com.unimarket.unimarketintegradora.model.Oferta;
import com.unimarket.unimarketintegradora.utils.SQLConnector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OfertaDao implements Dao<Oferta, String> {

    @Override
    public boolean create(Oferta entidad) {
        String sql = "INSERT INTO oferta (id_articulo_fk, MATRICULA_USUARIO_fk, monto, estado) VALUES (?, ?, ?, ?)";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, entidad.getIdArticuloFk());
            ps.setString(2, entidad.getIdUsuarioFk());
            ps.setBigDecimal(3, entidad.getMonto());
            ps.setString(4, entidad.getEstado());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al registrar oferta: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Oferta> getAll() {
        List<Oferta> lista = new ArrayList<>();
        String sql = "SELECT * FROM oferta";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Oferta o = new Oferta(rs.getInt("id_articulo_fk"), rs.getString("id_usuario_fk"), rs.getBigDecimal("monto"), rs.getString("estado"));
                o.setIdOferta(rs.getInt("id_oferta"));
                lista.add(o);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar ofertas: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public Oferta getById(String id) {
        String sql = "SELECT * FROM oferta WHERE id_oferta = ?";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Oferta o = new Oferta(rs.getInt("id_articulo_fk"), rs.getString("id_usuario_fk"), rs.getBigDecimal("monto"), rs.getString("estado"));
                    o.setIdOferta(rs.getInt("id_oferta"));
                    return o;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar oferta: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean update(Oferta entidad) {
        String sql = "UPDATE oferta SET estado = ? WHERE id_oferta = ?";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, entidad.getEstado());
            ps.setInt(2, entidad.getIdOferta());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar oferta: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        String sql = "DELETE FROM oferta WHERE id_oferta = ?";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al borrar oferta: " + e.getMessage());
            return false;
        }
    }
}