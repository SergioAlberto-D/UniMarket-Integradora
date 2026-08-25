package com.unimarket.unimarketintegradora.model.dao;

import com.unimarket.unimarketintegradora.model.Usuario;
import com.unimarket.unimarketintegradora.utils.HashUtils;
import com.unimarket.unimarketintegradora.utils.SQLConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Objeto de acceso a datos (DAO) de MUA para la entidad Usuario.
 *
 * @author Sergio
 */
public class UsuarioDao implements Dao<Usuario, String> {

    // =========================================================================
    // MÉTODOS OBLIGATORIOS DE LA INTERFAZ DAO (CRUD ESTÁNDAR)
    // =========================================================================

    /**
     * Registra una nueva entidad Usuario en la tabla USUARIO mediante una
     * sentencia INSERT.
     *
     * @param u Objeto Usuario que contiene los datos personales, académicos,
     *          de contacto, rol y fotografías que serán almacenados.
     * @return true si la sentencia INSERT afecta al menos un registro,
     *         indicando que el usuario fue registrado correctamente;
     *         false si no se insertó ningún registro o si ocurre un error
     *         durante la operación con la base de datos.
     */
    @Override
    public boolean create(Usuario u) {
        String sql = "INSERT INTO USUARIO (MATRICULA, NOMBRE, APELLIDO_PATERNO, APELLIDO_MATERNO, NUMERO_CELULAR, " +
                "ID_DIVISION_ACADEMICA_FK, FECHA_REGISTRO, CORREO_INSTITUCIONAL, ID_ROL_FK, ESTADO, FOTO_PERFIL, " +
                "FOTO_CREDENCIAL_FRENTE, FOTO_CREDENCIAL_REVERSO) " +
                "VALUES (?, ?, ?, ?, ?, ?, SYSDATE, ?, ?, 'unverificado', ?, ?, ?)";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, u.getMatricula());
            ps.setString(2, u.getNombre());
            ps.setString(3, u.getApellidoPaterno());
            ps.setString(4, u.getApellidoMaterno());
            ps.setString(5, u.getNumeroCelular());
            ps.setInt(6, u.getIdDivisionAcademicaFk());
            ps.setString(7, u.getCorreoInstitucional());
            ps.setInt(8, u.getIdRolFk());
            ps.setString(9, u.getFotoPerfil());
            ps.setString(10, u.getFotoCredencialFrente());
            ps.setString(11, u.getFotoCredencialReverso());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al crear usuario: " + e.getMessage());
            return false;
        }
    }

    /**
     * Consulta todos los usuarios registrados en la base de datos y convierte
     * cada registro obtenido en un objeto Usuario.
     *
     * @return Lista de objetos Usuario correspondientes a los registros
     *         encontrados. Si no existen usuarios o ocurre un error durante
     *         la consulta, devuelve una lista vacía en lugar de null.
     */
    @Override
    public List<Usuario> getAll() {
        List<Usuario> lista = new ArrayList<>();

        String sql = "SELECT * FROM USUARIO";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearUsuario(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error al listar usuarios: " + e.getMessage());
        }

        return lista;
    }

    /**
     * Obtiene los usuarios que pueden ser mostrados en el panel de
     * administración, excluyendo aquellos cuyo estado sea INACTIVO o
     * ELIMINADO.
     *
     * @return Lista de usuarios activos o disponibles para administración,
     *         ordenados desde el registro más reciente hasta el más antiguo.
     *         Si no existen registros o ocurre un error, devuelve una lista
     *         vacía.
     */
    public List<Usuario> getUsuariosActivosParaAdmin() {
        List<Usuario> lista = new ArrayList<>();

        String sql = "SELECT * FROM usuario " +
                "WHERE UPPER(estado) NOT IN ('INACTIVO', 'ELIMINADO') " +
                "ORDER BY fecha_registro DESC";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario u = new Usuario(
                        rs.getString("nombre"),
                        rs.getString("apellido_paterno"),
                        rs.getString("apellido_materno"),
                        rs.getString("numero_celular"),
                        rs.getInt("id_division_academica_fk"),
                        rs.getDate("fecha_registro"),
                        rs.getString("correo_institucional"),
                        rs.getInt("id_rol_fk"),
                        rs.getString("estado")
                );

                u.setMatricula(rs.getString("MATRICULA"));

                try {
                    u.setFotoPerfil(rs.getString("foto_perfil"));
                } catch (Exception ignored) {
                    // La fotografía no impide cargar al usuario.
                }

                lista.add(u);
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener usuarios activos: " + e.getMessage());
        }

        return lista;
    }

    /**
     * Busca un usuario mediante su matrícula.
     *
     * @param id Matrícula única del usuario que se desea consultar.
     * @return Objeto Usuario construido con la información encontrada en la
     *         base de datos. Devuelve null cuando no existe un usuario con
     *         la matrícula proporcionada o cuando ocurre un error durante
     *         la consulta.
     */
    @Override
    public Usuario getById(String id) {
        String sql = "SELECT * FROM USUARIO WHERE MATRICULA = ?";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar usuario por id: " + e.getMessage());
        }

        return null;
    }

    /**
     * Actualiza los datos principales de un usuario existente.
     *
     * @param u Objeto Usuario que contiene los nuevos datos que serán
     *          almacenados. La matrícula del objeto se utiliza para localizar
     *          el registro que será actualizado.
     * @return true si la sentencia UPDATE modifica al menos un registro,
     *         indicando que los datos fueron actualizados correctamente;
     *         false si no se modificó ningún registro o si ocurre un error
     *         durante la operación con la base de datos.
     */
    @Override
    public boolean update(Usuario u) {
        String sql = "UPDATE USUARIO SET NOMBRE = ?, APELLIDO_PATERNO = ?, APELLIDO_MATERNO = ?, NUMERO_CELULAR = ?, " +
                "ID_DIVISION_ACADEMICA_FK = ?, CORREO_INSTITUCIONAL = ?, ESTADO = ?, FOTO_PERFIL = ?, " +
                "FOTO_CREDENCIAL_FRENTE = ?, FOTO_CREDENCIAL_REVERSO = ? WHERE MATRICULA = ?";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, u.getNombre());
            ps.setString(2, u.getApellidoPaterno());
            ps.setString(3, u.getApellidoMaterno());
            ps.setString(4, u.getNumeroCelular());
            ps.setInt(5, u.getIdDivisionAcademicaFk());
            ps.setString(6, u.getCorreoInstitucional());
            ps.setString(7, u.getEstado());
            ps.setString(8, u.getFotoPerfil());
            ps.setString(9, u.getFotoCredencialFrente());
            ps.setString(10, u.getFotoCredencialReverso());
            ps.setString(11, u.getMatricula());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }

    /**
     * Realiza la baja lógica del usuario cambiando su estado a INACTIVO.
     *
     * @param id Matrícula del usuario que se desea dar de baja.
     * @return true si el usuario fue encontrado y su estado fue actualizado;
     *         false si no se modificó ningún registro o si ocurrió un error
     *         durante la operación.
     */
    @Override
    public boolean delete(String id) {
        return rechazarUsuario(id);
    }

    // =========================================================================
    // MÉTODOS PARA EL LOGIN
    // =========================================================================

    /**
     * Busca un usuario mediante su correo institucional y contraseña.
     * La contraseña recibida se transforma mediante SHA-256 y se compara
     * con el hash almacenado en CONTRASENA_USUARIO.
     *
     * @param correo Correo institucional utilizado para identificar la cuenta
     *               del usuario.
     * @param contrasenaEnClaro Contraseña introducida por el usuario antes
     *                          de aplicar el proceso de hash.
     * @return Objeto Usuario correspondiente a las credenciales proporcionadas
     *         cuando el correo y la contraseña son válidos. Devuelve null
     *         cuando no existe una coincidencia.
     * @throws SQLException Si ocurre un error al conectarse, preparar o
     *                      ejecutar la consulta SQL.
     */
    public Usuario buscarPorCorreoYContrasena(
            String correo,
            String contrasenaEnClaro) throws SQLException {

        String contrasenaHash =
                HashUtils.convertirSHA256(contrasenaEnClaro);

        String sql = "SELECT u.* FROM USUARIO u " +
                "INNER JOIN CONTRASENA_USUARIO cu " +
                "ON u.MATRICULA = cu.MATRICULA_USUARIO_FK " +
                "WHERE u.CORREO_INSTITUCIONAL = ? " +
                "AND cu.CONTRASENA_HASH = ?";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, correo);
            ps.setString(2, contrasenaHash);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }
        }

        return null;
    }

    // =========================================================================
    // MÉTODOS PARA PANEL DE USUARIOS DEL ADMIN
    // =========================================================================

    /**
     * Cambia el estado de un usuario.
     *
     * @param matricula Matrícula del usuario cuyo estado será modificado.
     * @param nuevoEstado Nuevo estado que se asignará al usuario.
     * @return true si el UPDATE modificó al menos un registro, indicando que
     *         el estado fue actualizado; false si no se modificó ningún
     *         registro o si ocurre un error de base de datos.
     */
    public boolean cambiarEstado(String matricula, String nuevoEstado) {
        String sql = "UPDATE USUARIO SET ESTADO = ? WHERE MATRICULA = ?";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevoEstado);
            ps.setString(2, matricula);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al cambiar estado del usuario: " + e.getMessage());
            return false;
        }
    }

    /**
     * Activa un usuario estableciendo su estado como verificado.
     *
     * @param matricula Matrícula del usuario que será activado.
     * @return true si el estado del usuario fue actualizado correctamente;
     *         false si no se modificó ningún registro o si ocurre un error.
     */
    public boolean activarUsuario(String matricula) {
        String sql = "UPDATE USUARIO SET ESTADO = 'verificado' WHERE MATRICULA = ?";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, matricula);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al activar usuario: " + e.getMessage());
            return false;
        }
    }

    // =========================================================================
    // MÉTODOS PARA PETICIONES DEL ADMIN
    // =========================================================================

    /**
     * Obtiene los usuarios cuyo estado es diferente de unverificado.
     *
     * @return Lista de usuarios que ya no se encuentran en estado
     *         unverificado. Si no existen usuarios o ocurre un error,
     *         devuelve una lista vacía.
     */
    public List<Usuario> obtenerUsuariosVerificados() {
        List<Usuario> lista = new ArrayList<>();

        String sql = "SELECT * FROM USUARIO WHERE ESTADO != 'unverificado'";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearUsuario(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error al listar usuarios verificados: " + e.getMessage());
        }

        return lista;
    }

    /**
     * Obtiene los usuarios que todavía no han sido verificados por el
     * administrador.
     *
     * @return Lista de usuarios cuyo estado es unverificado. Si no existen
     *         peticiones o ocurre un error durante la consulta, devuelve
     *         una lista vacía.
     */
    public List<Usuario> obtenerPeticiones() {
        List<Usuario> lista = new ArrayList<>();

        String sql = "SELECT * FROM USUARIO WHERE ESTADO = 'unverificado'";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearUsuario(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error al listar peticiones: " + e.getMessage());
        }

        return lista;
    }

    /**
     * Verifica un usuario delegando la operación al método activarUsuario.
     *
     * @param matricula Matrícula del usuario que será verificado.
     * @return true si el usuario fue activado correctamente; false si no
     *         se pudo actualizar su estado.
     */
    public boolean verificarUsuario(String matricula) {
        return activarUsuario(matricula);
    }

    /**
     * Rechaza un usuario eliminando físicamente su registro de la tabla
     * USUARIO.
     *
     * @param matricula Matrícula del usuario que será rechazado.
     * @return true si el registro fue eliminado correctamente; false si no
     *         se eliminó ningún registro o si ocurre un error.
     */
    public boolean rechazarUsuario(String matricula) {
        String sqlDeleteUsuario =
                "DELETE FROM USUARIO WHERE MATRICULA = ?";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sqlDeleteUsuario)) {

            ps.setString(1, matricula);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al rechazar usuario: " + e.getMessage());
            return false;
        }
    }

    // =========================================================================
    // MÉTODOS DE ACTUALIZACIÓN DE PERFIL
    // =========================================================================

    /**
     * Actualiza la fotografía de perfil del usuario.
     *
     * @param matricula Matrícula del usuario cuya fotografía será actualizada.
     * @param rutaFoto Ruta relativa o ubicación donde se encuentra almacenada
     *                 la nueva fotografía.
     * @return true si la fotografía fue actualizada correctamente; false si
     *         no se modificó ningún registro o si ocurrió un error.
     */
    public boolean actualizarFotoPerfil(String matricula, String rutaFoto) {
        String sql = "UPDATE USUARIO SET FOTO_PERFIL = ? WHERE MATRICULA = ?";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, rutaFoto);
            ps.setString(2, matricula);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar foto de perfil: " + e.getMessage());
            return false;
        }
    }

    /**
     * Actualiza el número telefónico asociado al usuario.
     *
     * @param matricula Matrícula del usuario cuyo teléfono será actualizado.
     * @param nuevoTelefono Nuevo número telefónico que sustituirá al
     *                      registrado actualmente.
     * @return true si el teléfono fue actualizado correctamente; false si
     *         no se modificó ningún registro o si ocurrió un error.
     */
    public boolean actualizarTelefono(String matricula, String nuevoTelefono) {
        String sql = "UPDATE USUARIO SET NUMERO_CELULAR = ? WHERE MATRICULA = ?";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevoTelefono);
            ps.setString(2, matricula);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar telefono: " + e.getMessage());
            return false;
        }
    }

    // =========================================================================
    // MÉTODOS PARA RECUPERACIÓN DE CONTRASEÑA Y TOKEN
    // =========================================================================

    /**
     * Comprueba si existe un usuario registrado con el correo proporcionado.
     *
     * @param correo Correo institucional que se desea comprobar.
     * @return true si existe al menos un usuario con ese correo; false si no
     *         existe o si ocurre un error durante la consulta.
     */
    public boolean existeCorreo(String correo) {
        String sql = "SELECT 1 FROM USUARIO WHERE CORREO_INSTITUCIONAL = ?";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, correo);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.out.println("Error al verificar si existe el correo: " + e.getMessage());
            return false;
        }
    }

    /**
     * Guarda un token de recuperación y establece su fecha de expiración
     * 15 minutos después de su creación.
     *
     * @param correo Correo institucional del usuario al que pertenece el
     *               token.
     * @param token Token generado para validar el proceso de recuperación
     *              de contraseña.
     * @return true si el token fue almacenado correctamente; false si no se
     *         actualizó ningún usuario o si ocurre un error de base de datos.
     */
    public boolean guardarTokenRecuperacion(String correo, String token) {
        String sql = "UPDATE USUARIO SET " +
                "TOKEN_RECUPERACION = ?, " +
                "TOKEN_EXPIRACION = SYSTIMESTAMP + INTERVAL '15' MINUTE " +
                "WHERE CORREO_INSTITUCIONAL = ?";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, token);
            ps.setString(2, correo);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al guardar el token de recuperación: " + e.getMessage());
            return false;
        }
    }

    /**
     * Activa la cuenta del usuario asociado al correo indicado.
     *
     * @param correo Correo institucional del usuario cuya cuenta será
     *               activada.
     * @return true si el estado de la cuenta fue actualizado a verificado;
     *         false si no se modificó ningún registro o si ocurrió un error.
     */
    public boolean activarCuenta(String correo) {
        String sql = "UPDATE USUARIO SET ESTADO = 'verificado' " +
                "WHERE CORREO_INSTITUCIONAL = ?";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, correo);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al activar cuenta: " + e.getMessage());
            return false;
        }
    }

    /**
     * Valida que un token corresponda al correo indicado y que todavía no
     * haya superado su fecha de expiración.
     *
     * @param correo Correo institucional asociado al token.
     * @param token Token de recuperación que se desea validar.
     * @return true si existe un registro cuyo correo y token coinciden y cuya
     *         fecha de expiración es posterior al momento actual; false si
     *         el token no coincide, está expirado o ocurre un error.
     */
    public boolean validarToken(String correo, String token) {
        String sql = "SELECT * FROM USUARIO " +
                "WHERE CORREO_INSTITUCIONAL = ? " +
                "AND TOKEN_RECUPERACION = ? " +
                "AND TOKEN_EXPIRACION > SYSTIMESTAMP";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, correo);
            ps.setString(2, token);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.out.println("Error al validar token: " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca un usuario utilizando su correo institucional.
     *
     * @param correo Correo institucional del usuario que se desea localizar.
     * @return Objeto Usuario correspondiente al correo proporcionado cuando
     *         existe un registro; null cuando no existe coincidencia o
     *         cuando ocurre un error durante la consulta.
     */
    public Usuario buscarPorCorreo(String correo) {
        String sql = "SELECT * FROM USUARIO WHERE CORREO_INSTITUCIONAL = ?";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, correo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar usuario por correo: " + e.getMessage());
        }

        return null;
    }

    /**
     * Elimina el token de recuperación y su fecha de expiración del usuario.
     *
     * @param correo Correo institucional del usuario cuyo token será
     *               eliminado.
     * @return true si los datos del token fueron limpiados correctamente;
     *         false si no se modificó ningún registro o si ocurrió un error.
     */
    public boolean limpiarToken(String correo) {
        String sql = "UPDATE USUARIO SET " +
                "TOKEN_RECUPERACION = NULL, " +
                "TOKEN_EXPIRACION = NULL " +
                "WHERE CORREO_INSTITUCIONAL = ?";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, correo);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al limpiar token: " + e.getMessage());
            return false;
        }
    }

    // =========================================================================
    // MÉTODO AUXILIAR
    // =========================================================================

    /**
     * Convierte un registro obtenido mediante ResultSet en un objeto Usuario.
     *
     * @param rs ResultSet que contiene la fila de la tabla USUARIO que será
     *           convertida en una entidad.
     * @return Objeto Usuario construido con los valores almacenados en la
     *         fila actual del ResultSet.
     * @throws SQLException Si ocurre un error al intentar obtener alguno de
     *                      los valores de las columnas del ResultSet.
     */
    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();

        u.setMatricula(rs.getString("MATRICULA"));
        u.setNombre(rs.getString("NOMBRE"));
        u.setApellidoPaterno(rs.getString("APELLIDO_PATERNO"));
        u.setApellidoMaterno(rs.getString("APELLIDO_MATERNO"));
        u.setNumeroCelular(rs.getString("NUMERO_CELULAR"));
        u.setIdDivisionAcademicaFk(rs.getInt("ID_DIVISION_ACADEMICA_FK"));
        u.setFechaRegistro(rs.getDate("FECHA_REGISTRO"));
        u.setCorreoInstitucional(rs.getString("CORREO_INSTITUCIONAL"));
        u.setIdRolFk(rs.getInt("ID_ROL_FK"));
        u.setEstado(rs.getString("ESTADO"));
        u.setTokenRecuperacion(rs.getString("TOKEN_RECUPERACION"));
        u.setTokenExpiracion(rs.getTimestamp("TOKEN_EXPIRACION"));
        u.setFotoPerfil(rs.getString("FOTO_PERFIL"));
        u.setFotoCredencialFrente(rs.getString("FOTO_CREDENCIAL_FRENTE"));
        u.setFotoCredencialReverso(rs.getString("FOTO_CREDENCIAL_REVERSO"));

        return u;
    }

    /**
     * Cambia el rol del usuario a vendedor asignándole el identificador
     * de rol 3.
     *
     * @param matricula Matrícula del usuario cuyo rol será modificado.
     * @return true si el rol fue actualizado correctamente; false si no se
     *         modificó ningún registro o si ocurrió un error de base de datos.
     */
    public boolean ascenderAVendedor(String matricula) {
        String sql = "UPDATE USUARIO SET ID_ROL_FK = 3 WHERE MATRICULA = ?";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, matricula);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al ascender a vendedor: " + e.getMessage());
            return false;
        }
    }
}