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
 * Registra una nueva entidad en la tabla correspondiente mediante una sentencia INSERT.
 * @param u Objeto Usuario con los datos del usuario que se desea registrar o actualizar.
 * @return Devuelve `true` porque la sentencia SQL afectó al menos un registro, lo que indica que la operación se realizó correctamente. Devuelve `false` cuando no se modificó ningún registro o cuando ocurre una excepción de base de datos.
 */
    @Override
    public boolean create(Usuario u) {
        String sql = "INSERT INTO USUARIO (MATRICULA, NOMBRE, APELLIDO_PATERNO, APELLIDO_MATERNO, NUMERO_CELULAR, " +
                "ID_DIVISION_ACADEMICA_FK, FECHA_REGISTRO, CORREO_INSTITUCIONAL, ID_ROL_FK, ESTADO, FOTO_PERFIL, " +
                "FOTO_CREDENCIAL_FRENTE, FOTO_CREDENCIAL_REVERSO) " +
                "VALUES (?, ?, ?, ?, ?, ?, SYSDATE, ?, ?, 'unverificado', ?, ?, ?)";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
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
 * Consulta y obtiene todos los registros disponibles de la entidad, mapeando cada fila de la base de datos a su objeto correspondiente.
 * @return Devuelve una lista de objetos `Usuario` construida a partir de los registros encontrados. Si no existen registros o ocurre un error durante la consulta, se conserva una lista vacía para evitar devolver `null`.
 */
    @Override
    public List<Usuario> getAll() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM USUARIO";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearUsuario(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar usuarios: " + e.getMessage());
        }
        return lista;
    }

/**
 * Busca un registro específico utilizando su identificador y, si existe, lo convierte a la entidad correspondiente.
 * @param id Identificador único del registro que se desea consultar, actualizar o eliminar.
 * @return Devuelve un objeto `Usuario` con los datos recuperados de la base de datos cuando existe un registro que coincide con el identificador o criterio recibido. Si no se encuentra ningún registro, devuelve `null`.
 */
    @Override
    public Usuario getById(String id) {
        String sql = "SELECT * FROM USUARIO WHERE MATRICULA = ?";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
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
 * Actualiza los datos de una entidad existente utilizando su identificador como referencia.
 * @param u Objeto Usuario con los datos del usuario que se desea registrar o actualizar.
 * @return Devuelve `true` porque la sentencia SQL afectó al menos un registro, lo que indica que la operación se realizó correctamente. Devuelve `false` cuando no se modificó ningún registro o cuando ocurre una excepción de base de datos.
 */
    @Override
    public boolean update(Usuario u) {
        String sql = "UPDATE USUARIO SET NOMBRE = ?, APELLIDO_PATERNO = ?, APELLIDO_MATERNO = ?, NUMERO_CELULAR = ?, " +
                "ID_DIVISION_ACADEMICA_FK = ?, CORREO_INSTITUCIONAL = ?, ESTADO = ?, FOTO_PERFIL = ?, " +
                "FOTO_CREDENCIAL_FRENTE = ?, FOTO_CREDENCIAL_REVERSO = ? WHERE MATRICULA = ?";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
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
 * Elimina o realiza la baja lógica del registro identificado, de acuerdo con las reglas definidas para la entidad.
 * @param id Identificador único del registro que se desea consultar, actualizar o eliminar.
 * @return Devuelve un valor booleano que indica si la operación se realizó correctamente; `true` representa éxito y `false` representa que no se pudo completar la operación.
 */
    @Override
    public boolean delete(String id) {
        return rechazarUsuario(id);
    }

    // =========================================================================
    // MÉTODOS PARA EL LOGIN
    // =========================================================================
/**
 * Busca un usuario mediante su correo institucional y la contraseña proporcionada, comparando la contraseña transformada mediante hash.
 * @param correo Correo institucional del usuario utilizado para localizar su cuenta o validar sus credenciales.
 * @param contrasenaEnClaro Contraseña proporcionada por el usuario antes de aplicar el proceso de hash para compararla con la almacenada.
 * @return Devuelve un objeto `Usuario` con los datos recuperados de la base de datos cuando existe un registro que coincide con el identificador o criterio recibido. Si no se encuentra ningún registro, devuelve `null`.
 * @throws SQLException Se produce cuando ocurre un error al establecer la conexión, preparar o ejecutar la consulta SQL.
 */
    public Usuario buscarPorCorreoYContrasena(String correo, String contrasenaEnClaro) throws SQLException {
        String contrasenaHash = HashUtils.convertirSHA256(contrasenaEnClaro);

        String sql = "SELECT u.* FROM USUARIO u " +
                "INNER JOIN CONTRASENA_USUARIO cu ON u.MATRICULA = cu.MATRICULA_USUARIO_FK " +
                "WHERE u.CORREO_INSTITUCIONAL = ? AND cu.CONTRASENA_HASH = ?";

        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
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
 * Actualiza el estado del registro indicado para reflejar su nueva situación dentro del flujo del sistema.
 * @param matricula Matrícula del usuario al que corresponde la operación.
 * @param nuevoEstado Nuevo estado que se asignará al registro para reflejar el resultado de la operación.
 * @return Devuelve `true` porque la sentencia SQL afectó al menos un registro, lo que indica que la operación se realizó correctamente. Devuelve `false` cuando no se modificó ningún registro o cuando ocurre una excepción de base de datos.
 */
    public boolean cambiarEstado(String matricula, String nuevoEstado) {
        String sql = "UPDATE USUARIO SET ESTADO = ? WHERE MATRICULA = ?";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setString(2, matricula);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al cambiar estado del usuario: " + e.getMessage());
            return false;
        }
    }
/**
 * Actualiza el estado del usuario indicado a verificado para habilitar su cuenta.
 * @param matricula Matrícula del usuario al que corresponde la operación.
 * @return Devuelve `true` porque la sentencia SQL afectó al menos un registro, lo que indica que la operación se realizó correctamente. Devuelve `false` cuando no se modificó ningún registro o cuando ocurre una excepción de base de datos.
 */
    public boolean activarUsuario(String matricula) {
        String sql = "UPDATE USUARIO SET ESTADO = 'verificado' WHERE MATRICULA = ?";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, matricula);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al activar usuario: " + e.getMessage());
            return false;
        }
    }

    // =========================================================================
    // MÉTODOS PARA PETICIONES DEL ADMIN (REGISTROS NUEVOS)
    // =========================================================================
/**
 * Obtiene los usuarios que ya tienen su cuenta verificada.
 * @return Devuelve una lista de objetos `Usuario` construida a partir de los registros encontrados. Si no existen registros o ocurre un error durante la consulta, se conserva una lista vacía para evitar devolver `null`.
 */
    public List<Usuario> obtenerUsuariosVerificados() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM USUARIO WHERE ESTADO != 'unverificado'";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearUsuario(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar usuarios verificados: " + e.getMessage());
        }
        return lista;
    }
/**
 * Obtiene los usuarios que se encuentran pendientes de revisión o verificación por parte del administrador.
 * @return Devuelve una lista de objetos `Usuario` construida a partir de los registros encontrados. Si no existen registros o ocurre un error durante la consulta, se conserva una lista vacía para evitar devolver `null`.
 */
    public List<Usuario> obtenerPeticiones() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM USUARIO WHERE ESTADO = 'unverificado'";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearUsuario(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar peticiones: " + e.getMessage());
        }
        return lista;
    }
/**
 * Marca al usuario indicado como verificado después de completar el proceso de revisión.
 * @param matricula Matrícula del usuario al que corresponde la operación.
 * @return Devuelve un valor booleano que indica si la operación se realizó correctamente; `true` representa éxito y `false` representa que no se pudo completar la operación.
 */
    public boolean verificarUsuario(String matricula) {
        return activarUsuario(matricula);
    }
/**
 * Realiza la acción de rechazo definida para el usuario indicado, cambiando su estado según las reglas del sistema.
 * @param matricula Matrícula del usuario al que corresponde la operación.
 * @return Devuelve `true` porque la sentencia SQL afectó al menos un registro, lo que indica que la operación se realizó correctamente. Devuelve `false` cuando no se modificó ningún registro o cuando ocurre una excepción de base de datos.
 */
    public boolean rechazarUsuario(String matricula) {
        String sqlDeleteUsuario = "DELETE FROM USUARIO WHERE MATRICULA = ?";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sqlDeleteUsuario)) {
            ps.setString(1, matricula);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al rechazar usuario: " + e.getMessage());
            return false;
        }
    }

    // =========================================================================
    // MÉTODOS DE ACTUALIZACIONES DE PERFIL
    // =========================================================================
/**
 * Actualiza la ruta de la fotografía de perfil asociada al usuario.
 * @param matricula Matrícula del usuario al que corresponde la operación.
 * @param rutaFoto Ruta o ubicación de la fotografía de perfil que se asociará al usuario.
 * @return Devuelve `true` porque la sentencia SQL afectó al menos un registro, lo que indica que la operación se realizó correctamente. Devuelve `false` cuando no se modificó ningún registro o cuando ocurre una excepción de base de datos.
 */
    public boolean actualizarFotoPerfil(String matricula, String rutaFoto) {
        String sql = "UPDATE USUARIO SET FOTO_PERFIL = ? WHERE MATRICULA = ?";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
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
 * @param matricula Matrícula del usuario al que corresponde la operación.
 * @param nuevoTelefono Número telefónico que sustituirá al teléfono actualmente registrado del usuario.
 * @return Devuelve `true` porque la sentencia SQL afectó al menos un registro, lo que indica que la operación se realizó correctamente. Devuelve `false` cuando no se modificó ningún registro o cuando ocurre una excepción de base de datos.
 */
    public boolean actualizarTelefono(String matricula, String nuevoTelefono) {
        String sql = "UPDATE USUARIO SET NUMERO_CELULAR = ? WHERE MATRICULA = ?";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
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

    // Faltante agregado 1: Comprobar si un correo está registrado
/**
 * Comprueba si el correo institucional ya se encuentra registrado en la base de datos.
 * @param correo Correo institucional del usuario utilizado para localizar su cuenta o validar sus credenciales.
 * @return Devuelve un valor booleano que indica si la operación se realizó correctamente; `true` representa éxito y `false` representa que no se pudo completar la operación.
 */
    public boolean existeCorreo(String correo) {
        String sql = "SELECT 1 FROM USUARIO WHERE CORREO_INSTITUCIONAL = ?";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar si existe el correo: " + e.getMessage());
            return false;
        }
    }

    // Faltante agregado 2: Guardar el token de recuperación y darle tiempo de vida (15 minutos)
/**
 * Guarda el token de recuperación asociado al correo para permitir posteriormente la validación del proceso de recuperación.
 * @param correo Correo institucional del usuario utilizado para localizar su cuenta o validar sus credenciales.
 * @param token Token de recuperación o verificación asociado al correo del usuario.
 * @return Devuelve `true` porque la sentencia SQL afectó al menos un registro, lo que indica que la operación se realizó correctamente. Devuelve `false` cuando no se modificó ningún registro o cuando ocurre una excepción de base de datos.
 */
    public boolean guardarTokenRecuperacion(String correo, String token) {
        // En Oracle SYSTIMESTAMP + INTERVAL '15' MINUTE sirve para dar 15 min de vida
        String sql = "UPDATE USUARIO SET TOKEN_RECUPERACION = ?, TOKEN_EXPIRACION = SYSTIMESTAMP + INTERVAL '15' MINUTE WHERE CORREO_INSTITUCIONAL = ?";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, token);
            ps.setString(2, correo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al guardar el token de recuperación: " + e.getMessage());
            return false;
        }
    }
/**
 * Activa la cuenta asociada al correo cuando la operación de verificación es válida.
 * @param correo Correo institucional del usuario utilizado para localizar su cuenta o validar sus credenciales.
 * @return Devuelve `true` porque la sentencia SQL afectó al menos un registro, lo que indica que la operación se realizó correctamente. Devuelve `false` cuando no se modificó ningún registro o cuando ocurre una excepción de base de datos.
 */
    public boolean activarCuenta(String correo) {
        String sql = "UPDATE USUARIO SET ESTADO = 'verificado' WHERE CORREO_INSTITUCIONAL = ?";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, correo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al activar cuenta: " + e.getMessage());
            return false;
        }
    }
/**
 * Comprueba que el token proporcionado corresponda al correo y siga siendo válido.
 * @param correo Correo institucional del usuario utilizado para localizar su cuenta o validar sus credenciales.
 * @param token Token de recuperación o verificación asociado al correo del usuario.
 * @return Devuelve un valor booleano que indica si la operación se realizó correctamente; `true` representa éxito y `false` representa que no se pudo completar la operación.
 */
    public boolean validarToken(String correo, String token) {
        String sql = "SELECT * FROM USUARIO WHERE CORREO_INSTITUCIONAL = ? AND TOKEN_RECUPERACION = ? AND TOKEN_EXPIRACION > SYSTIMESTAMP";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
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
 * Busca y devuelve el usuario asociado al correo institucional proporcionado.
 * @param correo Correo institucional del usuario utilizado para localizar su cuenta o validar sus credenciales.
 * @return Devuelve un objeto `Usuario` con los datos recuperados de la base de datos cuando existe un registro que coincide con el identificador o criterio recibido. Si no se encuentra ningún registro, devuelve `null`.
 */
    public Usuario buscarPorCorreo(String correo) {
        String sql = "SELECT * FROM USUARIO WHERE CORREO_INSTITUCIONAL = ?";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
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
 * Elimina o limpia el token de recuperación almacenado para el correo indicado.
 * @param correo Correo institucional del usuario utilizado para localizar su cuenta o validar sus credenciales.
 * @return Devuelve `true` porque la sentencia SQL afectó al menos un registro, lo que indica que la operación se realizó correctamente. Devuelve `false` cuando no se modificó ningún registro o cuando ocurre una excepción de base de datos.
 */
    public boolean limpiarToken(String correo) {
        String sql = "UPDATE USUARIO SET TOKEN_RECUPERACION = NULL, TOKEN_EXPIRACION = NULL WHERE CORREO_INSTITUCIONAL = ?";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
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
 * Actualiza el rol o condición del usuario indicado para habilitar las funciones de vendedor.
 * @param matricula Matrícula del usuario al que corresponde la operación.
 * @return Devuelve `true` porque la sentencia SQL afectó al menos un registro, lo que indica que la operación se realizó correctamente. Devuelve `false` cuando no se modificó ningún registro o cuando ocurre una excepción de base de datos.
 */
    public boolean ascenderAVendedor(String matricula) {
        String sql = "UPDATE USUARIO SET ID_ROL_FK = 3 WHERE MATRICULA = ?";
        try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, matricula);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al ascender a vendedor: " + e.getMessage());
            return false;
        }
    }
}