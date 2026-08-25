package com.unimarket.unimarketintegradora.model.dao;
import java.util.List;

/**
 * Objeto de acceso a datos (DAO) de MUA para la entidad .
 *
 * @author Equipo UniMarket
 */

public interface Dao<T, K> {
    boolean create(T entidad);
    List<T> getAll();
    T getById(K id);
    boolean update(T entidad);
    boolean delete(K id);
}