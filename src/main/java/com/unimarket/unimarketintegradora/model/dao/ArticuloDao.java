package com.unimarket.unimarketintegradora.model.dao;

import com.unimarket.unimarketintegradora.model.Articulo;
import com.unimarket.unimarketintegradora.model.ImagenArticulo;
import com.unimarket.unimarketintegradora.utils.SQLConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Objeto de acceso a datos (DAO) de MUA para la entidad Articulo.
 *
 * @author Sergio
 */

public class ArticuloDao implements Dao<Articulo, String> {

/**
 * Registra una nueva entidad en la tabla correspondiente mediante una sentencia INSERT.
 * @param entidad Objeto de la entidad que contiene los datos que se almacenarán o actualizarán en la base de datos.
 * @return Devuelve `true` porque la sentencia SQL afectó al menos un registro, lo que indica que la operación se realizó correctamente. Devuelve `false` cuando no se modificó ningún registro o cuando ocurre una excepción de base de datos.
 */
    @Override
    public boolean create(Articulo entidad) {
        /*
         * IMPORTANTE:
         * Todo artículo nuevo comienza en estado "espera".
         * El administrador deberá aprobarlo desde:
         * Administración > Actividad reciente
         *
         * Una vez aprobado:
         * espera -> Activo
         */
        String sql =
                "INSERT INTO articulo " +
                        "(id_articulo, nombre, precio, id_categoria_fk, descripcion, MATRICULA_USUARIO_fk, estado) " +
                        "VALUES (SEQ_ARTICULO.NEXTVAL, ?, ?, ?, ?, ?, 'espera')";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, entidad.getNombre());
            ps.setBigDecimal(2, entidad.getPrecio());
            ps.setInt(3, entidad.getIdCategoriaFk());
            ps.setString(4, entidad.getDescripcion());
            ps.setString(5, entidad.getIdUsuarioFk());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al crear artículo: " + e.getMessage());
            return false;
        }
    }

/**
 * Consulta y obtiene todos los registros disponibles de la entidad, mapeando cada fila de la base de datos a su objeto correspondiente.
 * @return Devuelve una lista de objetos `Articulo` construida a partir de los registros encontrados. Si no existen registros o ocurre un error durante la consulta, se conserva una lista vacía para evitar devolver `null`.
 */
    @Override
    public List<Articulo> getAll() {

        List<Articulo> lista = new ArrayList<>();

        /*
         * Solo artículos ACTIVOS pueden aparecer en el catálogo.
         */
        String sql =
                "SELECT a.*, " +
                        "       u.nombre AS nombre_vendedor, " +
                        "       (SELECT URL_IMAGEN " +
                        "        FROM IMAGEN_ARTICULO ia " +
                        "        WHERE ia.id_articulo_fk = a.id_articulo " +
                        "        FETCH FIRST 1 ROWS ONLY) AS portada " +
                        "FROM ARTICULO a " +
                        "JOIN USUARIO u ON a.matricula_usuario_fk = u.matricula " +
                        "WHERE a.id_articulo NOT IN " +
                        "      (SELECT id_articulo_fk " +
                        "       FROM transaccion " +
                        "       WHERE estado = 'PENDIENTE') " +
                        "AND UPPER(a.estado) = 'ACTIVO'";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Articulo a = new Articulo(
                        rs.getString("nombre"),
                        rs.getBigDecimal("precio"),
                        rs.getInt("id_categoria_fk"),
                        rs.getString("descripcion"),
                        rs.getString("matricula_usuario_fk")
                );

                a.setIdArticulo(rs.getInt("id_articulo"));
                a.setImagenPrincipal(rs.getString("portada"));
                a.setNombreUsuario(rs.getString("nombre_vendedor"));

                lista.add(a);
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener artículos: " + e.getMessage());
        }

        return lista;
    }

/**
 * Busca un registro específico utilizando su identificador y, si existe, lo convierte a la entidad correspondiente.
 * @param id Identificador único del registro que se desea consultar, actualizar o eliminar.
 * @return Devuelve un objeto `Articulo` con los datos recuperados de la base de datos cuando existe un registro que coincide con el identificador o criterio recibido. Si no se encuentra ningún registro, devuelve `null`.
 */
    @Override
    public Articulo getById(String id) {

        String sql =
                "SELECT * " +
                        "FROM articulo " +
                        "WHERE id_articulo = ?";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Articulo a = new Articulo(
                            rs.getString("nombre"),
                            rs.getBigDecimal("precio"),
                            rs.getInt("id_categoria_fk"),
                            rs.getString("descripcion"),
                            rs.getString("matricula_usuario_fk")
                    );

                    a.setIdArticulo(rs.getInt("id_articulo"));

                    return a;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar artículo: " + e.getMessage());
        }

        return null;
    }

/**
 * Actualiza los datos de una entidad existente utilizando su identificador como referencia.
 * @param entidad Objeto de la entidad que contiene los datos que se almacenarán o actualizarán en la base de datos.
 * @return Devuelve `true` porque la sentencia SQL afectó al menos un registro, lo que indica que la operación se realizó correctamente. Devuelve `false` cuando no se modificó ningún registro o cuando ocurre una excepción de base de datos.
 */
    @Override
    public boolean update(Articulo entidad) {

        String sql =
                "UPDATE articulo " +
                        "SET nombre = ?, " +
                        "    precio = ?, " +
                        "    id_categoria_fk = ?, " +
                        "    descripcion = ? " +
                        "WHERE id_articulo = ?";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, entidad.getNombre());
            ps.setBigDecimal(2, entidad.getPrecio());
            ps.setInt(3, entidad.getIdCategoriaFk());
            ps.setString(4, entidad.getDescripcion());
            ps.setInt(5, entidad.getIdArticulo());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar artículo: " + e.getMessage());
            return false;
        }
    }

/**
 * Elimina o realiza la baja lógica del registro identificado, de acuerdo con las reglas definidas para la entidad.
 * @param id Identificador único del registro que se desea consultar, actualizar o eliminar.
 * @return Devuelve `true` porque la sentencia SQL afectó al menos un registro, lo que indica que la operación se realizó correctamente. Devuelve `false` cuando no se modificó ningún registro o cuando ocurre una excepción de base de datos.
 */
    @Override
    public boolean delete(String id) {

        String sql =
                "UPDATE articulo " +
                        "SET estado = 'ELIMINADO' " +
                        "WHERE id_articulo = ?";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar artículo: " + e.getMessage());
            return false;
        }
    }
/**
 * Obtiene el identificador del artículo más reciente publicado por el usuario indicado.
 * @param matriculaUsuario Matrícula del usuario cuyos artículos, historial o datos se desean consultar.
 * @return Devuelve el valor entero obtenido de la consulta, correspondiente al identificador o cantidad solicitada por la operación.
 */
    public int obtenerUltimoIdPorUsuario(String matriculaUsuario) {

        String sql =
                "SELECT MAX(id_articulo) " +
                        "FROM articulo " +
                        "WHERE matricula_usuario_fk = ?";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, matriculaUsuario);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener último ID de artículo: " + e.getMessage());
        }

        return -1;
    }

/**
 * Obtiene el artículo junto con la información relacionada necesaria para mostrar sus detalles completos.
 * @param idArticulo Identificador único del artículo sobre el que se realizará la consulta, validación, eliminación o modificación.
 * @return Devuelve un objeto `Articulo` con los datos recuperados de la base de datos cuando existe un registro que coincide con el identificador o criterio recibido. Si no se encuentra ningún registro, devuelve `null`.
 */
    public Articulo getDetallesCompletos(String idArticulo) {

        String sql =
                "SELECT a.*, " +
                        "       u.nombre AS nombre_vendedor, " +
                        "       u.foto_perfil, " +
                        "       (SELECT URL_IMAGEN " +
                        "        FROM IMAGEN_ARTICULO ia " +
                        "        WHERE ia.id_articulo_fk = a.id_articulo " +
                        "        FETCH FIRST 1 ROWS ONLY) AS portada " +
                        "FROM articulo a " +
                        "JOIN usuario u ON a.matricula_usuario_fk = u.matricula " +
                        "WHERE a.id_articulo = ? " +
                        "AND UPPER(a.estado) = 'ACTIVO'";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, idArticulo);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Articulo a = new Articulo(
                            rs.getString("nombre"),
                            rs.getBigDecimal("precio"),
                            rs.getInt("id_categoria_fk"),
                            rs.getString("descripcion"),
                            rs.getString("matricula_usuario_fk")
                    );

                    a.setIdArticulo(rs.getInt("id_articulo"));
                    a.setImagenPrincipal(rs.getString("portada"));
                    a.setNombreUsuario(rs.getString("nombre_vendedor"));
                    a.setFotoVendedor(rs.getString("foto_perfil"));

                    return a;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener detalles: " + e.getMessage());
        }

        return null;
    }

/**
 * Cuenta los artículos asociados al usuario indicado.
 * @param matriculaUsuario Matrícula del usuario cuyos artículos, historial o datos se desean consultar.
 * @return Devuelve el número de registros que cumplen la condición de la consulta; si no existen registros que coincidan, el resultado es `0`.
 */
    public int contarPorUsuario(String matriculaUsuario) {

        String sql =
                "SELECT COUNT(*) " +
                        "FROM articulo " +
                        "WHERE matricula_usuario_fk = ?";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, matriculaUsuario);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al contar artículos: " + e.getMessage());
        }

        return 0;
    }

/**
 * Consulta artículos aplicando los filtros opcionales de orden, categoría, división y rango de precio, además de las reglas de visibilidad del usuario.
 * @param orden Criterio de ordenamiento solicitado para organizar los artículos devueltos.
 * @param idCategoria Identificador de la categoría utilizado para filtrar, localizar o modificar la categoría correspondiente.
 * @param idDivision Identificador de la división académica utilizada como filtro de los artículos.
 * @param minPrecio Precio mínimo del rango de búsqueda; se utiliza para excluir artículos con un precio inferior cuando se proporciona.
 * @param maxPrecio Precio máximo del rango de búsqueda; se utiliza para excluir artículos con un precio superior cuando se proporciona.
 * @param matriculaUsuarioLogueado Matrícula del usuario que tiene la sesión iniciada; se utiliza para aplicar las reglas de visibilidad de sus propios artículos.
 * @return Devuelve una lista de objetos `Articulo` construida a partir de los registros encontrados. Si no existen registros o ocurre un error durante la consulta, se conserva una lista vacía para evitar devolver `null`.
 */
    public List<Articulo> filtrarArticulos(
            String orden,
            Integer idCategoria,
            Integer idDivision,
            java.math.BigDecimal minPrecio,
            java.math.BigDecimal maxPrecio,
            String matriculaUsuarioLogueado) {

        List<Articulo> lista = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT a.*, " +
                        "       u.nombre AS nombre_vendedor, " +
                        "       (SELECT URL_IMAGEN " +
                        "        FROM IMAGEN_ARTICULO ia " +
                        "        WHERE ia.id_articulo_fk = a.id_articulo " +
                        "        FETCH FIRST 1 ROWS ONLY) AS portada " +
                        "FROM ARTICULO a " +
                        "JOIN USUARIO u ON a.matricula_usuario_fk = u.matricula " +
                        "WHERE a.id_articulo NOT IN " +
                        "      (SELECT id_articulo_fk " +
                        "       FROM transaccion " +
                        "       WHERE estado = 'PENDIENTE') " +
                        "AND UPPER(a.estado) = 'ACTIVO' "
        );

        List<Object> parametros = new ArrayList<>();

        if (matriculaUsuarioLogueado != null &&
                !matriculaUsuarioLogueado.trim().isEmpty()) {

            sql.append(" AND a.matricula_usuario_fk != ? ");
            parametros.add(matriculaUsuarioLogueado);
        }

        if (idCategoria != null && idCategoria > 0) {

            sql.append(" AND a.id_categoria_fk = ? ");
            parametros.add(idCategoria);
        }

        if (idDivision != null && idDivision > 0) {

            sql.append(" AND u.id_division_academica_fk = ? ");
            parametros.add(idDivision);
        }

        if (minPrecio != null &&
                minPrecio.compareTo(java.math.BigDecimal.ZERO) >= 0) {

            sql.append(" AND a.precio >= ? ");
            parametros.add(minPrecio);
        }

        if (maxPrecio != null &&
                maxPrecio.compareTo(java.math.BigDecimal.ZERO) > 0) {

            sql.append(" AND a.precio <= ? ");
            parametros.add(maxPrecio);
        }

        if ("Precio menor".equalsIgnoreCase(orden)) {

            sql.append(" ORDER BY a.precio ASC");

        } else if ("Precio mayor".equalsIgnoreCase(orden)) {

            sql.append(" ORDER BY a.precio DESC");

        } else {

            sql.append(" ORDER BY a.id_articulo DESC");
        }

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            for (int i = 0; i < parametros.size(); i++) {
                ps.setObject(i + 1, parametros.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Articulo a = new Articulo(
                            rs.getString("nombre"),
                            rs.getBigDecimal("precio"),
                            rs.getInt("id_categoria_fk"),
                            rs.getString("descripcion"),
                            rs.getString("matricula_usuario_fk")
                    );

                    a.setIdArticulo(rs.getInt("id_articulo"));
                    a.setImagenPrincipal(rs.getString("portada"));
                    a.setNombreUsuario(rs.getString("nombre_vendedor"));

                    lista.add(a);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al filtrar artículos: " + e.getMessage());
        }

        return lista;
    }

    /*
     * ============================================================
     * ARTÍCULOS DEL USUARIO
     * ============================================================
     */
/**
 * Obtiene los artículos pertenecientes al usuario indicado, diferenciando si se encuentran o no en proceso.
 * @param matriculaUsuario Matrícula del usuario cuyos artículos, historial o datos se desean consultar.
 * @param enProceso Indicador que determina si se consultan artículos del usuario que están en proceso de venta o los que no están en ese estado.
 * @return Devuelve una lista de objetos `Articulo` construida a partir de los registros encontrados. Si no existen registros o ocurre un error durante la consulta, se conserva una lista vacía para evitar devolver `null`.
 */
    public List<Articulo> obtenerPorUsuarioYEstado(
            String matriculaUsuario,
            boolean enProceso) {

        List<Articulo> lista = new ArrayList<>();

        String sql;

        if (enProceso) {

            sql =
                    "SELECT a.*, " +
                            "(SELECT URL_IMAGEN " +
                            " FROM IMAGEN_ARTICULO ia " +
                            " WHERE ia.id_articulo_fk = a.id_articulo " +
                            " FETCH FIRST 1 ROWS ONLY) AS portada " +
                            "FROM ARTICULO a " +
                            "WHERE a.matricula_usuario_fk = ? " +
                            "AND a.id_articulo IN " +
                            "    (SELECT id_articulo_fk " +
                            "     FROM transaccion " +
                            "     WHERE estado = 'PENDIENTE') " +
                            "AND a.estado != 'ELIMINADO'";

        } else {

            sql =
                    "SELECT a.*, " +
                            "(SELECT URL_IMAGEN " +
                            " FROM IMAGEN_ARTICULO ia " +
                            " WHERE ia.id_articulo_fk = a.id_articulo " +
                            " FETCH FIRST 1 ROWS ONLY) AS portada " +
                            "FROM ARTICULO a " +
                            "WHERE a.matricula_usuario_fk = ? " +
                            "AND a.id_articulo NOT IN " +
                            "    (SELECT id_articulo_fk " +
                            "     FROM transaccion " +
                            "     WHERE estado = 'PENDIENTE') " +
                            "AND a.estado != 'ELIMINADO'";
        }

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, matriculaUsuario);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Articulo a = new Articulo(
                            rs.getString("nombre"),
                            rs.getBigDecimal("precio"),
                            rs.getInt("id_categoria_fk"),
                            rs.getString("descripcion"),
                            rs.getString("matricula_usuario_fk")
                    );

                    a.setIdArticulo(rs.getInt("id_articulo"));
                    a.setImagenPrincipal(rs.getString("portada"));

                    lista.add(a);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener artículos por estado: " + e.getMessage());
        }

        return lista;
    }

    /*
     * ============================================================
     * ARTÍCULOS PARA ADMINISTRACIÓN
     * ============================================================
     */
/**
 * Obtiene las publicaciones que el panel de administración necesita para su gestión y moderación.
 * @return Devuelve una lista de objetos `Articulo` construida a partir de los registros encontrados. Si no existen registros o ocurre un error durante la consulta, se conserva una lista vacía para evitar devolver `null`.
 */
    public List<Articulo> listarParaAdmin() {

        List<Articulo> lista = new ArrayList<>();

        String sql =
                "SELECT a.id_articulo, " +
                        "       a.nombre, " +
                        "       a.precio, " +
                        "       a.id_categoria_fk, " +
                        "       a.descripcion, " +
                        "       a.matricula_usuario_fk, " +
                        "       u.nombre AS nombre_vendedor, " +
                        "       u.foto_perfil, " +
                        "       c.categoria AS nombre_categoria, " +
                        "       (SELECT URL_IMAGEN " +
                        "        FROM IMAGEN_ARTICULO ia " +
                        "        WHERE ia.id_articulo_fk = a.id_articulo " +
                        "        FETCH FIRST 1 ROWS ONLY) AS portada " +
                        "FROM articulo a " +
                        "LEFT JOIN usuario u " +
                        "       ON a.matricula_usuario_fk = u.matricula " +
                        "LEFT JOIN categoria c " +
                        "       ON a.id_categoria_fk = c.id_categoria " +
                        "WHERE a.estado IS NULL " +
                        "   OR a.estado != 'ELIMINADO' " +
                        "ORDER BY a.id_articulo DESC";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Articulo a = new Articulo(
                        rs.getString("nombre"),
                        rs.getBigDecimal("precio"),
                        rs.getInt("id_categoria_fk"),
                        rs.getString("descripcion"),
                        rs.getString("matricula_usuario_fk")
                );

                a.setIdArticulo(rs.getInt("id_articulo"));
                a.setImagenPrincipal(rs.getString("portada"));
                a.setNombreUsuario(rs.getString("nombre_vendedor"));
                a.setFotoVendedor(rs.getString("foto_perfil"));
                a.setNombreCategoria(rs.getString("nombre_categoria"));

                lista.add(a);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar para Admin: " + e.getMessage());
        }

        return lista;
    }

    /*
     * ============================================================
     * ARTÍCULOS EN ESPERA DE VERIFICACIÓN
     * ============================================================
     */
/**
 * Obtiene las publicaciones que se encuentran pendientes de revisión o aprobación.
 * @return Devuelve una lista de objetos `Articulo` construida a partir de los registros encontrados. Si no existen registros o ocurre un error durante la consulta, se conserva una lista vacía para evitar devolver `null`.
 */
    public List<Articulo> obtenerArticulosEnEspera() {

        List<Articulo> lista = new ArrayList<>();
        ImagenArticuloDao imagenArticuloDao = new ImagenArticuloDao(); // <-- NUEVO

        String sql =
                "SELECT a.id_articulo, " +
                        "       a.nombre, " +
                        "       a.precio, " +
                        "       a.id_categoria_fk, " +
                        "       a.descripcion, " +
                        "       a.matricula_usuario_fk, " +
                        "       u.nombre AS nombre_vendedor, " +
                        "       c.categoria AS nombre_categoria, " +
                        "       (SELECT URL_IMAGEN " +
                        "        FROM IMAGEN_ARTICULO ia " +
                        "        WHERE ia.id_articulo_fk = a.id_articulo " +
                        "        FETCH FIRST 1 ROWS ONLY) AS portada " +
                        "FROM articulo a " +
                        "LEFT JOIN usuario u " +
                        "       ON a.matricula_usuario_fk = u.matricula " +
                        "LEFT JOIN categoria c " +
                        "       ON a.id_categoria_fk = c.id_categoria " +
                        "WHERE LOWER(a.estado) = 'espera' " +
                        "ORDER BY a.id_articulo DESC";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Articulo a = new Articulo(
                        rs.getString("nombre"),
                        rs.getBigDecimal("precio"),
                        rs.getInt("id_categoria_fk"),
                        rs.getString("descripcion"),
                        rs.getString("matricula_usuario_fk")
                );

                a.setIdArticulo(rs.getInt("id_articulo"));
                a.setImagenPrincipal(rs.getString("portada"));
                a.setNombreUsuario(rs.getString("nombre_vendedor"));
                a.setNombreCategoria(rs.getString("nombre_categoria"));

                // NUEVO: cargar TODAS las imágenes del artículo (no solo la portada)
                List<String> urlsImagenes = new ArrayList<>();
                for (ImagenArticulo img : imagenArticuloDao.obtenerPorArticulo(a.getIdArticulo())) {
                    urlsImagenes.add(img.getUrlImagen());
                }
                a.setImagenes(urlsImagenes);

                lista.add(a);
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener artículos en espera: " + e.getMessage());
        }

        return lista;
    }
/**
 * Actualiza el estado del registro indicado para reflejar su nueva situación dentro del flujo del sistema.
 * @param idArticulo Identificador único del artículo sobre el que se realizará la consulta, validación, eliminación o modificación.
 * @param nuevoEstado Nuevo estado que se asignará al registro para reflejar el resultado de la operación.
 * @return Devuelve `true` porque la sentencia SQL afectó al menos un registro, lo que indica que la operación se realizó correctamente. Devuelve `false` cuando no se modificó ningún registro o cuando ocurre una excepción de base de datos.
 */
    public boolean cambiarEstado(int idArticulo, String nuevoEstado) {

        String sql =
                "UPDATE articulo " +
                        "SET estado = ? " +
                        "WHERE id_articulo = ?";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevoEstado);
            ps.setInt(2, idArticulo);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al cambiar estado del artículo: " + e.getMessage());
            return false;
        }
    }
/**
 * Cambia el estado del artículo a Activo para permitir que la publicación sea visible según las reglas del sistema.
 * @param idArticulo Identificador único del artículo sobre el que se realizará la consulta, validación, eliminación o modificación.
 * @return Devuelve un valor booleano que indica si la operación se realizó correctamente; `true` representa éxito y `false` representa que no se pudo completar la operación.
 */
    public boolean verificarArticulo(int idArticulo) {

        return cambiarEstado(idArticulo, "Activo");
    }
/**
 * Cambia el estado del artículo a ELIMINADO para retirarlo de la publicación activa.
 * @param idArticulo Identificador único del artículo sobre el que se realizará la consulta, validación, eliminación o modificación.
 * @return Devuelve un valor booleano que indica si la operación se realizó correctamente; `true` representa éxito y `false` representa que no se pudo completar la operación.
 */
    public boolean rechazarArticulo(int idArticulo) {

        return cambiarEstado(idArticulo, "ELIMINADO");
    }
}