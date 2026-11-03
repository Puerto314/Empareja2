package co.edu.unbosque.empareja2.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import co.edu.unbosque.empareja2.entity.Carrera;
import co.edu.unbosque.empareja2.entity.Estudio;
import co.edu.unbosque.empareja2.entity.Genero;
import co.edu.unbosque.empareja2.entity.RedSocial;
import co.edu.unbosque.empareja2.entity.Rol;
import co.edu.unbosque.empareja2.entity.Tiene;
import co.edu.unbosque.empareja2.entity.TipoReaccion;
import co.edu.unbosque.empareja2.entity.Universidad;
import co.edu.unbosque.empareja2.entity.Usuario;

/**
 * Acceso a datos (JDBC) del agregado USUARIO segun el modelo relacional:
 * usuario (datos comunes), administrador (subtipo), estudiante (subtipo con su
 * carrera, universidad y semestre), interes_estudiante y red_social.
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

	/** Fecha de nacimiento por defecto (columna NOT NULL) para cuentas que no la indican, p. ej. administradores. */
	static final LocalDate FECHA_NACIMIENTO_POR_DEFECTO = LocalDate.of(2000, 1, 1);

	private static final String SELECT = "SELECT u.id_usuario, u.correo, u.contrasena_hash, u.genero, "
			+ "CASE WHEN ad.id_usuario IS NULL THEN 'ESTUDIANTE' ELSE 'ADMIN' END AS rol, "
			+ "u.nombre, u.apellido, u.foto_perfil, u.fecha_nacimiento "
			+ "FROM usuario u LEFT JOIN administrador ad ON ad.id_usuario = u.id_usuario ";

	private static final RowMapper<Usuario> MAPPER = (rs, i) -> {
		Usuario u = new Usuario();
		u.setId(rs.getLong("id_usuario"));
		u.setCorreo(rs.getString("correo"));
		u.setContrasenaHash(rs.getString("contrasena_hash"));
		String genero = rs.getString("genero");
		u.setGenero(genero == null ? null : Genero.valueOf(genero));
		u.setRol(Rol.valueOf(rs.getString("rol")));
		u.setNombre(rs.getString("nombre"));
		u.setApellido(rs.getString("apellido"));
		u.setFotoPerfil(rs.getString("foto_perfil"));
		u.setFechaNacimiento(rs.getObject("fecha_nacimiento", LocalDate.class));
		return u;
	};

	private final JdbcTemplate jdbc;
	private final SimpleJdbcInsert insert;

	public UsuarioRepository(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
		this.insert = new SimpleJdbcInsert(jdbc).withTableName("usuario").usingGeneratedKeyColumns("id_usuario");
	}

	/** Condicion SQL (sobre los alias u y ad de SELECT) que corresponde a cada rol. */
	private static String filtroRol(Rol rol) {
		return rol == Rol.ADMIN ? "ad.id_usuario IS NOT NULL"
				: "ad.id_usuario IS NULL AND EXISTS (SELECT 1 FROM estudiante es0 WHERE es0.id_usuario = u.id_usuario)";
	}

	// ------------------------------------------------------------------
	// Consultas
	// ------------------------------------------------------------------

	public Optional<Usuario> findById(Long id) {
		return primero(jdbc.query(SELECT + "WHERE u.id_usuario = ?", MAPPER, id));
	}

	public Optional<Usuario> findByCorreo(String correo) {
		return primero(jdbc.query(SELECT + "WHERE u.correo = ?", MAPPER, correo));
	}

	public Optional<Usuario> findByIdAndRol(Long id, Rol rol) {
		return primero(jdbc.query(SELECT + "WHERE u.id_usuario = ? AND " + filtroRol(rol), MAPPER, id));
	}

	public List<Usuario> findAll() {
		return conColecciones(jdbc.query(SELECT + "ORDER BY u.id_usuario", MAPPER));
	}

	public boolean existsByCorreo(String correo) {
		return contar("SELECT COUNT(*) FROM usuario WHERE correo = ?", correo) > 0;
	}

	/** Igual que existsByCorreo pero ignorando al propio usuario (para editar sin falso duplicado). */
	public boolean existsByCorreoAndIdNot(String correo, Long id) {
		return contar("SELECT COUNT(*) FROM usuario WHERE correo = ? AND id_usuario <> ?", correo, id) > 0;
	}

	public long contarPorRol(Rol rol) {
		return contar("SELECT COUNT(*) FROM usuario u LEFT JOIN administrador ad ON ad.id_usuario = u.id_usuario WHERE "
				+ filtroRol(rol));
	}

	/** Catalogo completo: todos los usuarios con ese rol, excepto el indicado. */
	public List<Usuario> findByRolAndIdNotOrderByNombreAscApellidoAsc(Rol rol, Long idExcluido) {
		return conColecciones(jdbc.query(SELECT + "WHERE " + filtroRol(rol)
				+ " AND u.id_usuario <> ? ORDER BY u.nombre ASC, u.apellido ASC", MAPPER, idExcluido));
	}

	/**
	 * Catalogo filtrado: los que estudian en la universidad dada (excepto el
	 * indicado).
	 */
	public List<Usuario> buscarPorUniversidad(Rol rol, Long idExcluido, Long idUniversidad) {
		return conColecciones(jdbc.query(SELECT + "WHERE " + filtroRol(rol) + " AND u.id_usuario <> ? "
				+ "AND EXISTS (SELECT 1 FROM estudiante e WHERE e.id_usuario = u.id_usuario "
				+ "AND e.universidad_id_universidad = ?) " + "ORDER BY u.nombre ASC, u.apellido ASC", MAPPER,
				idExcluido, idUniversidad));
	}

	/**
	 * Ranking por me gusta recibidos: los usuarios con ese rol que tienen al menos
	 * una reaccion del tipo indicado, de mas a menos (empata por nombre y apellido).
	 * Si idUniversidad no es nulo, solo cuenta a quienes estudian en esa universidad.
	 * Incluye al usuario que consulta (es un ranking, no el catalogo).
	 */
	public List<Usuario> rankingPorMeGusta(Rol rol, TipoReaccion tipo, Long idUniversidad, int limite) {
		String filtro = idUniversidad == null ? ""
				: "AND EXISTS (SELECT 1 FROM estudiante e WHERE e.id_usuario = u.id_usuario "
						+ "AND e.universidad_id_universidad = ?) ";
		List<Object> args = new ArrayList<>();
		args.add(tipo.name());
		if (idUniversidad != null) {
			args.add(idUniversidad);
		}
		args.add(limite);
		return conColecciones(jdbc.query(SELECT
				+ "JOIN (SELECT estudiante_id_usuario1 AS id_destinatario, COUNT(*) AS total FROM reaccion "
				+ "WHERE tipo_reaccion = ? GROUP BY estudiante_id_usuario1) r ON r.id_destinatario = u.id_usuario "
				+ "WHERE " + filtroRol(rol) + " " + filtro
				+ "ORDER BY r.total DESC, u.nombre ASC, u.apellido ASC, u.id_usuario ASC LIMIT ?", MAPPER,
				args.toArray()));
	}

	// ------------------------------------------------------------------
	// Escritura
	// ------------------------------------------------------------------

	/**
	 * Inserta (id nulo) o actualiza el usuario, ajusta su rol (tablas
	 * administrador / estudiante) y REEMPLAZA sus intereses y redes por los que
	 * trae el objeto. Un estudiante tiene exactamente un estudio. Devuelve el
	 * usuario recargado desde la base de datos.
	 */
	public Usuario save(Usuario u) {
		if (u.getRol() == Rol.ESTUDIANTE && u.getEstudios().isEmpty()) {
			throw new IllegalStateException("Un estudiante necesita una carrera y una universidad");
		}
		String genero = u.getGenero() == null ? Genero.NO_ESPECIFICA.name() : u.getGenero().name();
		LocalDate nacimiento = u.getFechaNacimiento() != null ? u.getFechaNacimiento() : FECHA_NACIMIENTO_POR_DEFECTO;
		if (u.getId() == null) {
			Map<String, Object> valores = new HashMap<>();
			valores.put("correo", u.getCorreo());
			valores.put("contrasena_hash", u.getContrasenaHash());
			valores.put("genero", genero);
			valores.put("nombre", u.getNombre());
			valores.put("apellido", u.getApellido());
			valores.put("foto_perfil", u.getFotoPerfil());
			valores.put("fecha_nacimiento", nacimiento);
			u.setId(insert.executeAndReturnKey(valores).longValue());
		} else {
			jdbc.update("UPDATE usuario SET correo = ?, contrasena_hash = ?, genero = ?, nombre = ?, apellido = ?, "
					+ "foto_perfil = ?, fecha_nacimiento = ? WHERE id_usuario = ?", u.getCorreo(),
					u.getContrasenaHash(), genero, u.getNombre(), u.getApellido(), u.getFotoPerfil(), nacimiento,
					u.getId());
		}
		sincronizarRol(u);
		return findById(u.getId()).orElseThrow(() -> new IllegalStateException("No se pudo recargar el usuario"));
	}

	/**
	 * Elimina el usuario. Su fila de administrador/estudiante, intereses, redes,
	 * me gusta, comentarios y reportes se borran solos por las llaves foraneas
	 * ON DELETE CASCADE.
	 */
	public void delete(Usuario u) {
		jdbc.update("DELETE FROM usuario WHERE id_usuario = ?", u.getId());
	}

	private void sincronizarRol(Usuario u) {
		Long id = u.getId();
		if (u.getRol() == Rol.ADMIN) {
			// Un administrador no es un perfil: si antes era estudiante se borra esa fila
			// (y con ella sus intereses, redes, me gusta, comentarios y reportes).
			jdbc.update("DELETE FROM estudiante WHERE id_usuario = ?", id);
			jdbc.update("INSERT IGNORE INTO administrador (id_usuario) VALUES (?)", id);
			return;
		}
		jdbc.update("DELETE FROM administrador WHERE id_usuario = ?", id);
		Estudio e = u.getEstudios().get(0);
		Long idCarrera = e.getCarrera().getIdCarrera();
		Long idUniversidad = e.getUniversidad().getIdUniversidad();
		// Se actualiza la fila existente en lugar de borrarla y reinsertarla: borrarla dispararia el
		// ON DELETE CASCADE sobre las reacciones, comentarios y reportes del estudiante.
		if (contar("SELECT COUNT(*) FROM estudiante WHERE id_usuario = ?", id) > 0) {
			jdbc.update("UPDATE estudiante e JOIN universidad un ON un.id_universidad = ? "
					+ "SET e.carrera_id_carrera = ?, e.universidad_id_universidad = un.id_universidad, "
					+ "e.universidad_ciudad_id_ciudad = un.ciudad_id_ciudad, e.semestre = ? WHERE e.id_usuario = ?",
					idUniversidad, idCarrera, e.getSemestre(), id);
		} else {
			jdbc.update("INSERT INTO estudiante (id_usuario, carrera_id_carrera, universidad_id_universidad, "
					+ "universidad_ciudad_id_ciudad, semestre) SELECT ?, ?, un.id_universidad, un.ciudad_id_ciudad, ? "
					+ "FROM universidad un WHERE un.id_universidad = ?", id, idCarrera, e.getSemestre(),
					idUniversidad);
		}

		jdbc.update("DELETE FROM interes_estudiante WHERE estudiante_id_usuario = ?", id);
		for (String interes : u.getIntereses()) {
			jdbc.update("INSERT INTO interes_estudiante (interes, estudiante_id_usuario) VALUES (?, ?)", interes, id);
		}

		jdbc.update("DELETE FROM red_social WHERE estudiante_id_usuario = ?", id);
		for (Tiene t : u.getRedes()) {
			jdbc.update("INSERT INTO red_social (id_red_social, nombre_red_social, nombre_usuario, "
					+ "estudiante_id_usuario) VALUES (?, ?, ?, ?)", t.getRedSocial().getIdRedSocial(),
					t.getRedSocial().getNombreRedSocial(), t.getNombreUsuario(), id);
		}
	}

	// ------------------------------------------------------------------
	// Carga de colecciones (3 consultas para toda la lista, no 3 por usuario)
	// ------------------------------------------------------------------

	private Optional<Usuario> primero(List<Usuario> usuarios) {
		return conColecciones(usuarios).stream().findFirst();
	}

	private List<Usuario> conColecciones(List<Usuario> usuarios) {
		if (usuarios.isEmpty()) {
			return usuarios;
		}
		Map<Long, Usuario> porId = usuarios.stream().collect(Collectors.toMap(Usuario::getId, Function.identity()));
		Object[] ids = porId.keySet().toArray();
		String in = Sql.placeholders(ids.length);

		jdbc.query("SELECT estudiante_id_usuario AS id_usuario, interes FROM interes_estudiante "
				+ "WHERE estudiante_id_usuario IN (" + in + ")", (RowCallbackHandler) rs -> {
					porId.get(rs.getLong("id_usuario")).getIntereses().add(rs.getString("interes"));
				}, ids);

		jdbc.query("SELECT e.id_usuario, e.semestre, c.id_carrera, c.nombre_carrera AS carrera_nombre, "
				+ "un.id_universidad, un.nombre_universidad AS universidad_nombre, un.sigla "
				+ "FROM estudiante e JOIN carrera c ON c.id_carrera = e.carrera_id_carrera "
				+ "JOIN universidad un ON un.id_universidad = e.universidad_id_universidad "
				+ "AND un.ciudad_id_ciudad = e.universidad_ciudad_id_ciudad " + "WHERE e.id_usuario IN (" + in + ")",
				(RowCallbackHandler) rs -> {
					porId.get(rs.getLong("id_usuario")).getEstudios().add(mapearEstudio(rs));
				}, ids);

		jdbc.query("SELECT estudiante_id_usuario AS id_usuario, id_red_social, nombre_red_social, nombre_usuario "
				+ "FROM red_social WHERE estudiante_id_usuario IN (" + in + ")", (RowCallbackHandler) rs -> {
					porId.get(rs.getLong("id_usuario")).getRedes().add(mapearRed(rs));
				}, ids);
		return usuarios;
	}

	private static Estudio mapearEstudio(ResultSet rs) throws SQLException {
		Carrera carrera = new Carrera(rs.getString("carrera_nombre"));
		carrera.setIdCarrera(rs.getLong("id_carrera"));
		Universidad universidad = new Universidad();
		universidad.setIdUniversidad(rs.getLong("id_universidad"));
		universidad.setNombre(rs.getString("universidad_nombre"));
		universidad.setSigla(rs.getString("sigla"));
		return new Estudio(carrera, universidad, rs.getInt("semestre"));
	}

	private static Tiene mapearRed(ResultSet rs) throws SQLException {
		RedSocial red = new RedSocial(rs.getString("nombre_red_social"), null);
		red.setIdRedSocial(rs.getLong("id_red_social"));
		return new Tiene(red, rs.getString("nombre_usuario"));
	}

	private long contar(String sql, Object... args) {
		Long n = jdbc.queryForObject(sql, Long.class, args);
		return n == null ? 0 : n;
	}
}
