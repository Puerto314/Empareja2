package co.edu.unbosque.empareja2.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import co.edu.unbosque.empareja2.entity.Carrera;
import co.edu.unbosque.empareja2.entity.Estudio;
import co.edu.unbosque.empareja2.entity.Genero;
import co.edu.unbosque.empareja2.entity.RedSocial;
import co.edu.unbosque.empareja2.entity.Rol;
import co.edu.unbosque.empareja2.entity.Universidad;
import co.edu.unbosque.empareja2.entity.Usuario;
import co.edu.unbosque.empareja2.exception.LanzadorException;

/**
 * Acceso a datos (JDBC) del agregado USUARIO segun el modelo relacional:
 * usuario (datos comunes), administrador (subtipo), estudiante (subtipo con su
 * carrera, universidad y semestre), interes_estudiante y red_social (los perfiles
 * del estudiante en redes sociales: tipo de red y url a su perfil).
 *
 * El rol no se guarda como columna: es ADMIN si el usuario esta en la tabla
 * administrador y ESTUDIANTE si no esta ahi y tiene fila en estudiante.
 *
 * Los metodos de escritura deben ejecutarse dentro de una transaccion (los
 * servicios usan @Transactional) para que el usuario y sus colecciones se
 * guarden juntos o no se guarden.
 */
@Repository
public class UsuarioRepository {

    /**
     * Fecha de nacimiento por defecto (columna NOT NULL) para cuentas que no la
     * indican, por ejemplo los administradores.
     */
    static final LocalDate FECHA_NACIMIENTO_POR_DEFECTO = LocalDate.of(2000, 1, 1);

    /** Consulta base: el usuario con su rol (ADMIN si esta en la tabla administrador). */
    private static final String SELECT = "SELECT u.id_usuario, u.correo, u.contrasena_hash, u.genero, "
            + "CASE WHEN ad.id_usuario IS NULL THEN 'ESTUDIANTE' ELSE 'ADMIN' END AS rol, "
            + "u.nombre_usuario, u.apellido, u.foto_perfil, u.fecha_nacimiento "
            + "FROM usuario u LEFT JOIN administrador ad ON ad.id_usuario = u.id_usuario ";

    /** Orden del catalogo: por nombre y luego por apellido. */
    private static final String ORDEN_NOMBRE = "ORDER BY u.nombre_usuario ASC, u.apellido ASC";

    /** Herramienta de Spring para ejecutar sentencias SQL con JDBC. */
    private final JdbcTemplate jdbc;

    /** Inserta en la tabla usuario y devuelve el id que genera la base de datos. */
    private final SimpleJdbcInsert insertar;

    /**
     * Crea el repositorio.
     *
     * @param jdbc el JdbcTemplate conectado a la base de datos
     */
    public UsuarioRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
        this.insertar = new SimpleJdbcInsert(jdbc).withTableName("usuario").usingGeneratedKeyColumns("id_usuario");
    }

    /**
     * Convierte la fila actual del resultado en un Usuario (sin intereses,
     * estudios ni redes; esos se cargan aparte).
     *
     * @param rs   el resultado de la consulta, posicionado en una fila
     * @param fila el numero de la fila
     * @return el usuario de esa fila
     * @throws SQLException si falla la lectura de una columna
     */
    private Usuario mapear(ResultSet rs, int fila) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getLong("id_usuario"));
        usuario.setCorreo(rs.getString("correo"));
        usuario.setContrasenaHash(rs.getString("contrasena_hash"));
        String genero = rs.getString("genero");
        usuario.setGenero(genero == null ? null : Genero.valueOf(genero));
        usuario.setRol(Rol.valueOf(rs.getString("rol")));
        usuario.setNombre(rs.getString("nombre_usuario"));
        usuario.setApellido(rs.getString("apellido"));
        usuario.setFotoPerfil(rs.getString("foto_perfil"));
        usuario.setFechaNacimiento(rs.getObject("fecha_nacimiento", LocalDate.class));
        return usuario;
    }

    /**
     * Condicion SQL (sobre los alias u y ad de SELECT) que corresponde a cada rol.
     *
     * @param rol el rol buscado
     * @return la condicion para el WHERE
     */
    private String filtroRol(Rol rol) {
        if (rol == Rol.ADMIN) {
            return "ad.id_usuario IS NOT NULL";
        }
        return "ad.id_usuario IS NULL AND EXISTS (SELECT 1 FROM estudiante es0 WHERE es0.id_usuario = u.id_usuario)";
    }

    // ------------------------------------------------------------------
    // Consultas
    // ------------------------------------------------------------------

    /**
     * Busca un usuario por su id, con sus intereses, estudios y redes.
     *
     * @param id el identificador del usuario
     * @return el usuario, o vacio si no existe
     */
    public Optional<Usuario> buscarPorId(Long id) {
        List<Usuario> usuarios = jdbc.query(SELECT + "WHERE u.id_usuario = ?", this::mapear, id);
        return cargarColecciones(usuarios).stream().findFirst();
    }

    /**
     * Busca un usuario por su correo, con sus intereses, estudios y redes.
     *
     * @param correo el correo del usuario
     * @return el usuario, o vacio si no existe
     */
    public Optional<Usuario> buscarPorCorreo(String correo) {
        List<Usuario> usuarios = jdbc.query(SELECT + "WHERE u.correo = ?", this::mapear, correo);
        return cargarColecciones(usuarios).stream().findFirst();
    }

    /**
     * Busca un usuario por su id solo si tiene el rol indicado.
     *
     * @param id  el identificador del usuario
     * @param rol el rol que debe tener
     * @return el usuario, o vacio si no existe o tiene otro rol
     */
    public Optional<Usuario> buscarPorIdYRol(Long id, Rol rol) {
        List<Usuario> usuarios = jdbc.query(SELECT + "WHERE u.id_usuario = ? AND " + filtroRol(rol), this::mapear,
                id);
        return cargarColecciones(usuarios).stream().findFirst();
    }

    /**
     * Lista todos los usuarios (para administradores).
     *
     * @return todos los usuarios ordenados por id
     */
    public List<Usuario> listarTodos() {
        return cargarColecciones(jdbc.query(SELECT + "ORDER BY u.id_usuario", this::mapear));
    }

    /**
     * Catalogo completo: todos los usuarios con ese rol, excepto el indicado.
     *
     * @param rol         el rol de los usuarios a listar
     * @param idExcluido  el usuario que no debe aparecer (el que consulta)
     * @return los usuarios ordenados por nombre y apellido
     */
    public List<Usuario> listarPorRolExcepto(Rol rol, Long idExcluido) {
        String sql = SELECT + "WHERE " + filtroRol(rol) + " AND u.id_usuario <> ? " + ORDEN_NOMBRE;
        return cargarColecciones(jdbc.query(sql, this::mapear, idExcluido));
    }

    /**
     * Catalogo filtrado: los que estudian en la universidad dada (excepto el
     * indicado).
     *
     * @param rol           el rol de los usuarios a listar
     * @param idExcluido    el usuario que no debe aparecer (el que consulta)
     * @param idUniversidad la universidad donde deben estudiar
     * @return los usuarios ordenados por nombre y apellido
     */
    public List<Usuario> listarPorUniversidad(Rol rol, Long idExcluido, Long idUniversidad) {
        String sql = SELECT + "WHERE " + filtroRol(rol) + " AND u.id_usuario <> ? "
                + "AND EXISTS (SELECT 1 FROM estudiante e WHERE e.id_usuario = u.id_usuario "
                + "AND e.id_universidad = ?) " + ORDEN_NOMBRE;
        return cargarColecciones(jdbc.query(sql, this::mapear, idExcluido, idUniversidad));
    }

    /**
     * Ranking por reacciones recibidas: los usuarios con ese rol que tienen al
     * menos una reaccion, de mas a menos (empata por nombre y
     * apellido). Si idUniversidad no es nulo, solo cuenta a quienes estudian en
     * esa universidad. Incluye al usuario que consulta (es un ranking, no el
     * catalogo).
     *
     * @param rol           el rol de los usuarios
     * @param idUniversidad la universidad a filtrar (puede ser null)
     * @param limite        cuantos usuarios devolver como maximo
     * @return los usuarios del ranking
     */
    public List<Usuario> rankingPorMeGusta(Rol rol, Long idUniversidad, int limite) {
        String filtroUniversidad = "";
        List<Object> argumentos = new ArrayList<>();
        if (idUniversidad != null) {
            filtroUniversidad = "AND EXISTS (SELECT 1 FROM estudiante e WHERE e.id_usuario = u.id_usuario "
                    + "AND e.id_universidad = ?) ";
            argumentos.add(idUniversidad);
        }
        argumentos.add(limite);

        String sql = SELECT + "JOIN (SELECT id_destinatario, COUNT(*) AS total FROM reaccion "
                + "GROUP BY id_destinatario) r ON r.id_destinatario = u.id_usuario "
                + "WHERE " + filtroRol(rol) + " " + filtroUniversidad
                + "ORDER BY r.total DESC, u.nombre_usuario ASC, u.apellido ASC, u.id_usuario ASC LIMIT ?";
        return cargarColecciones(jdbc.query(sql, this::mapear, argumentos.toArray()));
    }

    /**
     * Indica si ya existe un usuario con ese correo.
     *
     * @param correo el correo a buscar
     * @return true si ya existe
     */
    public boolean existePorCorreo(String correo) {
        return Sql.contar(jdbc, "SELECT COUNT(*) FROM usuario WHERE correo = ?", correo) > 0;
    }

    /**
     * Igual que existePorCorreo pero ignorando al propio usuario (para editar sin
     * falso duplicado).
     *
     * @param correo el correo a buscar
     * @param id     el id del usuario que se ignora
     * @return true si otro usuario ya tiene ese correo
     */
    public boolean existePorCorreoExceptoId(String correo, Long id) {
        return Sql.contar(jdbc, "SELECT COUNT(*) FROM usuario WHERE correo = ? AND id_usuario <> ?", correo,
                id) > 0;
    }

    /**
     * Cuenta los usuarios que tienen un rol.
     *
     * @param rol el rol a contar
     * @return el numero de usuarios con ese rol
     */
    public long contarPorRol(Rol rol) {
        return Sql.contar(jdbc, "SELECT COUNT(*) FROM usuario u "
                + "LEFT JOIN administrador ad ON ad.id_usuario = u.id_usuario WHERE " + filtroRol(rol));
    }

    // ------------------------------------------------------------------
    // Escritura
    // ------------------------------------------------------------------

    /**
     * Inserta (id nulo) o actualiza el usuario, ajusta su rol (tablas
     * administrador / estudiante) y REEMPLAZA sus intereses y redes por los que
     * trae el objeto. Un estudiante tiene exactamente un estudio.
     *
     * @param usuario el usuario a guardar
     * @return el usuario recargado desde la base de datos
     */
    public Usuario guardar(Usuario usuario) {
        if (usuario.getRol() == Rol.ESTUDIANTE && usuario.getEstudios().isEmpty()) {
            LanzadorException.estudianteSinEstudio();
        }

        if (usuario.getId() == null) {
            insertarUsuario(usuario);
        } else {
            actualizarUsuario(usuario);
        }

        if (usuario.getRol() == Rol.ADMIN) {
            guardarComoAdministrador(usuario.getId());
        } else {
            guardarComoEstudiante(usuario);
        }

        Usuario recargado = buscarPorId(usuario.getId()).orElse(null);
        if (recargado == null) {
            LanzadorException.usuarioNoRecargado();
        }
        return recargado;
    }

    /**
     * Elimina el usuario. Su fila de administrador/estudiante, intereses, redes,
     * me gusta, comentarios y reportes se borran solos por las llaves foraneas
     * ON DELETE CASCADE.
     *
     * @param usuario el usuario a eliminar
     */
    public void eliminar(Usuario usuario) {
        jdbc.update("DELETE FROM usuario WHERE id_usuario = ?", usuario.getId());
    }

    /**
     * Inserta la fila de la tabla usuario y le asigna al objeto el id generado.
     *
     * @param usuario el usuario nuevo
     */
    private void insertarUsuario(Usuario usuario) {
        Map<String, Object> valores = new HashMap<>();
        valores.put("correo", usuario.getCorreo());
        valores.put("contrasena_hash", usuario.getContrasenaHash());
        valores.put("genero", generoParaGuardar(usuario));
        valores.put("nombre_usuario", usuario.getNombre());
        valores.put("apellido", usuario.getApellido());
        valores.put("foto_perfil", usuario.getFotoPerfil());
        valores.put("fecha_nacimiento", fechaNacimientoParaGuardar(usuario));
        usuario.setId(insertar.executeAndReturnKey(valores).longValue());
    }

    /**
     * Actualiza la fila de la tabla usuario.
     *
     * @param usuario el usuario con los datos nuevos
     */
    private void actualizarUsuario(Usuario usuario) {
        jdbc.update("UPDATE usuario SET correo = ?, contrasena_hash = ?, genero = ?, nombre_usuario = ?, apellido = ?, "
                + "foto_perfil = ?, fecha_nacimiento = ? WHERE id_usuario = ?", usuario.getCorreo(),
                usuario.getContrasenaHash(), generoParaGuardar(usuario), usuario.getNombre(), usuario.getApellido(),
                usuario.getFotoPerfil(), fechaNacimientoParaGuardar(usuario), usuario.getId());
    }

    /**
     * El genero que se guarda: NO_ESPECIFICA si el usuario no tiene.
     *
     * @param usuario el usuario a guardar
     * @return el nombre del genero
     */
    private String generoParaGuardar(Usuario usuario) {
        if (usuario.getGenero() == null) {
            return Genero.NO_ESPECIFICA.name();
        }
        return usuario.getGenero().name();
    }

    /**
     * La fecha de nacimiento que se guarda: la por defecto si el usuario no tiene.
     *
     * @param usuario el usuario a guardar
     * @return la fecha de nacimiento
     */
    private LocalDate fechaNacimientoParaGuardar(Usuario usuario) {
        if (usuario.getFechaNacimiento() == null) {
            return FECHA_NACIMIENTO_POR_DEFECTO;
        }
        return usuario.getFechaNacimiento();
    }

    /**
     * Deja al usuario como administrador. Un administrador no es un perfil: si
     * antes era estudiante se borra esa fila (y con ella sus intereses, redes, me
     * gusta, comentarios y reportes).
     *
     * @param id el identificador del usuario
     */
    private void guardarComoAdministrador(Long id) {
        jdbc.update("DELETE FROM estudiante WHERE id_usuario = ?", id);
        jdbc.update("INSERT IGNORE INTO administrador (id_usuario) VALUES (?)", id);
    }

    /**
     * Deja al usuario como estudiante: guarda su estudio, sus intereses y sus
     * redes sociales (reemplazando los anteriores).
     *
     * @param usuario el usuario a guardar
     */
    private void guardarComoEstudiante(Usuario usuario) {
        Long id = usuario.getId();
        jdbc.update("DELETE FROM administrador WHERE id_usuario = ?", id);

        Estudio estudio = usuario.getEstudios().get(0);
        Long idCarrera = estudio.getCarrera().getIdCarrera();
        Long idUniversidad = estudio.getUniversidad().getIdUniversidad();

        // Se actualiza la fila existente en lugar de borrarla y reinsertarla: borrarla dispararia el
        // ON DELETE CASCADE sobre las reacciones, comentarios y reportes del estudiante.
        if (Sql.contar(jdbc, "SELECT COUNT(*) FROM estudiante WHERE id_usuario = ?", id) > 0) {
            jdbc.update("UPDATE estudiante SET id_carrera = ?, id_universidad = ?, semestre = ? WHERE id_usuario = ?",
                    idCarrera, idUniversidad, estudio.getSemestre(), id);
        } else {
            jdbc.update("INSERT INTO estudiante (id_usuario, id_carrera, id_universidad, semestre) VALUES (?, ?, ?, ?)",
                    id, idCarrera, idUniversidad, estudio.getSemestre());
        }

        jdbc.update("DELETE FROM interes_estudiante WHERE id_estudiante = ?", id);
        for (String interes : usuario.getIntereses()) {
            jdbc.update("INSERT INTO interes_estudiante (id_estudiante, interes) VALUES (?, ?)", id, interes);
        }

        jdbc.update("DELETE FROM red_social WHERE id_usuario = ?", id);
        for (RedSocial red : usuario.getRedes()) {
            jdbc.update("INSERT INTO red_social (id_usuario, tipo_red_social, url_perfil) VALUES (?, ?, ?)", id,
                    red.getTipoRedSocial(), red.getUrlPerfil());
        }
    }

    // ------------------------------------------------------------------
    // Carga de colecciones (3 consultas para toda la lista, no 3 por usuario)
    // ------------------------------------------------------------------

    /**
     * Completa los usuarios con sus intereses, estudios y redes sociales. Hace
     * una consulta por colección para TODA la lista, no una por usuario.
     *
     * @param usuarios los usuarios leidos de la tabla usuario
     * @return la misma lista, con las colecciones cargadas
     */
    private List<Usuario> cargarColecciones(List<Usuario> usuarios) {
        if (usuarios.isEmpty()) {
            return usuarios;
        }
        Map<Long, Usuario> porId = new HashMap<>();
        for (Usuario usuario : usuarios) {
            porId.put(usuario.getId(), usuario);
        }
        Object[] ids = porId.keySet().toArray();
        String in = Sql.placeholders(ids.length);

        cargarIntereses(porId, in, ids);
        cargarEstudios(porId, in, ids);
        cargarRedes(porId, in, ids);
        return usuarios;
    }

    /**
     * Carga los intereses de los usuarios indicados.
     *
     * @param porId los usuarios, por su id
     * @param in    los signos de interrogacion del IN
     * @param ids   los ids de los usuarios
     */
    private void cargarIntereses(Map<Long, Usuario> porId, String in, Object[] ids) {
        String sql = "SELECT id_estudiante AS id_usuario, interes FROM interes_estudiante "
                + "WHERE id_estudiante IN (" + in + ")";
        RowCallbackHandler alLeerFila = rs -> {
            Usuario usuario = porId.get(rs.getLong("id_usuario"));
            usuario.getIntereses().add(rs.getString("interes"));
        };
        jdbc.query(sql, alLeerFila, ids);
    }

    /**
     * Carga el estudio (carrera, universidad y semestre) de los usuarios indicados.
     *
     * @param porId los usuarios, por su id
     * @param in    los signos de interrogacion del IN
     * @param ids   los ids de los usuarios
     */
    private void cargarEstudios(Map<Long, Usuario> porId, String in, Object[] ids) {
        String sql = "SELECT e.id_usuario, e.semestre, c.id_carrera, c.nombre_carrera AS carrera_nombre, "
                + "un.id_universidad, un.nombre_universidad AS universidad_nombre, un.sigla "
                + "FROM estudiante e JOIN carrera c ON c.id_carrera = e.id_carrera "
                + "JOIN universidad un ON un.id_universidad = e.id_universidad WHERE e.id_usuario IN (" + in + ")";
        RowCallbackHandler alLeerFila = rs -> {
            Usuario usuario = porId.get(rs.getLong("id_usuario"));
            usuario.getEstudios().add(mapearEstudio(rs));
        };
        jdbc.query(sql, alLeerFila, ids);
    }

    /**
     * Carga las redes sociales de los usuarios indicados.
     *
     * @param porId los usuarios, por su id
     * @param in    los signos de interrogacion del IN
     * @param ids   los ids de los usuarios
     */
    private void cargarRedes(Map<Long, Usuario> porId, String in, Object[] ids) {
        String sql = "SELECT id_usuario, tipo_red_social, url_perfil FROM red_social "
                + "WHERE id_usuario IN (" + in + ")";
        RowCallbackHandler alLeerFila = rs -> {
            Usuario usuario = porId.get(rs.getLong("id_usuario"));
            usuario.getRedes().add(mapearRed(rs));
        };
        jdbc.query(sql, alLeerFila, ids);
    }

    /**
     * Convierte la fila actual del resultado en un Estudio.
     *
     * @param rs el resultado de la consulta, posicionado en una fila
     * @return el estudio de esa fila
     * @throws SQLException si falla la lectura de una columna
     */
    private Estudio mapearEstudio(ResultSet rs) throws SQLException {
        Carrera carrera = new Carrera(rs.getString("carrera_nombre"));
        carrera.setIdCarrera(rs.getLong("id_carrera"));

        Universidad universidad = new Universidad();
        universidad.setIdUniversidad(rs.getLong("id_universidad"));
        universidad.setNombre(rs.getString("universidad_nombre"));
        universidad.setSigla(rs.getString("sigla"));

        return new Estudio(carrera, universidad, rs.getInt("semestre"));
    }

    /**
     * Convierte la fila actual del resultado en el perfil de una red social.
     *
     * @param rs el resultado de la consulta, posicionado en una fila
     * @return la red social con el tipo y la url del perfil
     * @throws SQLException si falla la lectura de una columna
     */
    private RedSocial mapearRed(ResultSet rs) throws SQLException {
        return new RedSocial(rs.getString("tipo_red_social"), rs.getString("url_perfil"));
    }
}
