package co.edu.unbosque.empareja2.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
import co.edu.unbosque.empareja2.entity.TipoDocumento;
import co.edu.unbosque.empareja2.entity.Universidad;
import co.edu.unbosque.empareja2.entity.Usuario;

/**
 * Acceso a datos (JDBC) del agregado USUARIO: la fila de la tabla usuario mas
 * sus intereses (usuario_interes), estudios (estudio) y redes sociales (tiene).
 *
 * Los metodos de escritura deben ejecutarse dentro de una transaccion (los
 * servicios usan @Transactional) para que el usuario y sus colecciones se
 * guarden juntos o no se guarden.
 */
@Repository
public class UsuarioRepository {

	private static final String SELECT = "SELECT u.id_usuario, u.tipo_documento, u.documento, u.correo, "
			+ "u.contrasena_hash, u.genero, u.rol, u.nombre, u.apellido, u.foto_perfil, u.fecha_nacimiento, "
			+ "u.fecha_creacion FROM usuario u ";

	private static final RowMapper<Usuario> MAPPER = (rs, i) -> {
		Usuario u = new Usuario();
		u.setId(rs.getLong("id_usuario"));
		u.setTipoDocumento(TipoDocumento.valueOf(rs.getString("tipo_documento")));
		u.setDocumento(rs.getString("documento"));
		u.setCorreo(rs.getString("correo"));
		u.setContrasenaHash(rs.getString("contrasena_hash"));
		String genero = rs.getString("genero");
		u.setGenero(genero == null ? null : Genero.valueOf(genero));
		u.setRol(Rol.valueOf(rs.getString("rol")));
		u.setNombre(rs.getString("nombre"));
		u.setApellido(rs.getString("apellido"));
		u.setFotoPerfil(rs.getString("foto_perfil"));
		u.setFechaNacimiento(rs.getObject("fecha_nacimiento", LocalDate.class));
		u.setFechaCreacion(rs.getObject("fecha_creacion", LocalDateTime.class));
		return u;
	};

	private final JdbcTemplate jdbc;
	private final SimpleJdbcInsert insert;

	public UsuarioRepository(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
		this.insert = new SimpleJdbcInsert(jdbc).withTableName("usuario").usingGeneratedKeyColumns("id_usuario");
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
		return primero(jdbc.query(SELECT + "WHERE u.id_usuario = ? AND u.rol = ?", MAPPER, id, rol.name()));
	}

	public List<Usuario> findAll() {
		return conColecciones(jdbc.query(SELECT + "ORDER BY u.id_usuario", MAPPER));
	}

	public boolean existsByCorreo(String correo) {
		return contar("SELECT COUNT(*) FROM usuario WHERE correo = ?", correo) > 0;
	}

	public boolean existsByTipoDocumentoAndDocumento(TipoDocumento tipoDocumento, String documento) {
		return contar("SELECT COUNT(*) FROM usuario WHERE tipo_documento = ? AND documento = ?", tipoDocumento.name(),
				documento) > 0;
	}

	/** Igual que existsByCorreo pero ignorando al propio usuario (para editar sin falso duplicado). */
	public boolean existsByCorreoAndIdNot(String correo, Long id) {
		return contar("SELECT COUNT(*) FROM usuario WHERE correo = ? AND id_usuario <> ?", correo, id) > 0;
	}

	public boolean existsByTipoDocumentoAndDocumentoAndIdNot(TipoDocumento tipoDocumento, String documento, Long id) {
		return contar("SELECT COUNT(*) FROM usuario WHERE tipo_documento = ? AND documento = ? AND id_usuario <> ?",
				tipoDocumento.name(), documento, id) > 0;
	}

	public long contarPorRol(Rol rol) {
		return contar("SELECT COUNT(*) FROM usuario WHERE rol = ?", rol.name());
	}

	/** Catalogo completo: todos los usuarios con ese rol, excepto el indicado. */
	public List<Usuario> findByRolAndIdNotOrderByNombreAscApellidoAsc(Rol rol, Long idExcluido) {
		return conColecciones(
				jdbc.query(SELECT + "WHERE u.rol = ? AND u.id_usuario <> ? ORDER BY u.nombre ASC, u.apellido ASC",
						MAPPER, rol.name(), idExcluido));
	}

	/**
	 * Catalogo filtrado: los que estudian en la universidad dada (excepto el
	 * indicado).
	 */
	public List<Usuario> buscarPorUniversidad(Rol rol, Long idExcluido, Long idUniversidad) {
		return conColecciones(jdbc.query(SELECT + "WHERE u.rol = ? AND u.id_usuario <> ? "
				+ "AND EXISTS (SELECT 1 FROM estudio e WHERE e.id_usuario = u.id_usuario AND e.id_universidad = ?) "
				+ "ORDER BY u.nombre ASC, u.apellido ASC", MAPPER, rol.name(), idExcluido, idUniversidad));
	}

	// ------------------------------------------------------------------
	// Escritura
	// ------------------------------------------------------------------

	/**
	 * Inserta (id nulo) o actualiza el usuario y REEMPLAZA sus intereses, estudios
	 * y redes por los que trae el objeto. Devuelve el usuario recargado desde la
	 * base de datos.
	 */
	public Usuario save(Usuario u) {
		if (u.getId() == null) {
			Map<String, Object> valores = new HashMap<>();
			valores.put("tipo_documento", u.getTipoDocumento().name());
			valores.put("documento", u.getDocumento());
			valores.put("correo", u.getCorreo());
			valores.put("contrasena_hash", u.getContrasenaHash());
			valores.put("genero", u.getGenero() == null ? null : u.getGenero().name());
			valores.put("rol", u.getRol().name());
			valores.put("nombre", u.getNombre());
			valores.put("apellido", u.getApellido());
			valores.put("foto_perfil", u.getFotoPerfil());
			valores.put("fecha_nacimiento", u.getFechaNacimiento());
			valores.put("fecha_creacion", u.getFechaCreacion() != null ? u.getFechaCreacion() : LocalDateTime.now());
			u.setId(insert.executeAndReturnKey(valores).longValue());
		} else {
			jdbc.update(
					"UPDATE usuario SET tipo_documento = ?, documento = ?, correo = ?, contrasena_hash = ?, "
							+ "genero = ?, rol = ?, nombre = ?, apellido = ?, foto_perfil = ?, fecha_nacimiento = ? "
							+ "WHERE id_usuario = ?",
					u.getTipoDocumento().name(), u.getDocumento(), u.getCorreo(), u.getContrasenaHash(),
					u.getGenero() == null ? null : u.getGenero().name(), u.getRol().name(), u.getNombre(),
					u.getApellido(), u.getFotoPerfil(), u.getFechaNacimiento(), u.getId());
		}
		reemplazarColecciones(u);
		return findById(u.getId()).orElseThrow(() -> new IllegalStateException("No se pudo recargar el usuario"));
	}

	/**
	 * Elimina el usuario. Sus intereses, estudios, redes, me gusta, comentarios y
	 * reportes se borran solos por las llaves foraneas ON DELETE CASCADE.
	 */
	public void delete(Usuario u) {
		jdbc.update("DELETE FROM usuario WHERE id_usuario = ?", u.getId());
	}

	private void reemplazarColecciones(Usuario u) {
		Long id = u.getId();

		jdbc.update("DELETE FROM usuario_interes WHERE id_usuario = ?", id);
		for (String interes : u.getIntereses()) {
			jdbc.update("INSERT INTO usuario_interes (id_usuario, interes) VALUES (?, ?)", id, interes);
		}

		jdbc.update("DELETE FROM estudio WHERE id_usuario = ?", id);
		for (Estudio e : u.getEstudios()) {
			jdbc.update("INSERT INTO estudio (id_usuario, id_carrera, id_universidad, semestre) VALUES (?, ?, ?, ?)",
					id, e.getCarrera().getIdCarrera(), e.getUniversidad().getIdUniversidad(), e.getSemestre());
		}

		jdbc.update("DELETE FROM tiene WHERE id_usuario = ?", id);
		for (Tiene t : u.getRedes()) {
			jdbc.update("INSERT INTO tiene (id_usuario, id_red_social, url_perfil, alias) VALUES (?, ?, ?, ?)", id,
					t.getRedSocial().getIdRedSocial(), t.getUrlPerfil(), t.getAlias());
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

		jdbc.query("SELECT id_usuario, interes FROM usuario_interes WHERE id_usuario IN (" + in + ")",
				(RowCallbackHandler) rs -> {
					porId.get(rs.getLong("id_usuario")).getIntereses().add(rs.getString("interes"));
				}, ids);

		jdbc.query("SELECT e.id_usuario, e.semestre, c.id_carrera, c.nombre AS carrera_nombre, "
				+ "un.id_universidad, un.nombre AS universidad_nombre, un.sigla "
				+ "FROM estudio e JOIN carrera c ON c.id_carrera = e.id_carrera "
				+ "JOIN universidad un ON un.id_universidad = e.id_universidad " + "WHERE e.id_usuario IN (" + in + ")",
				(RowCallbackHandler) rs -> {
					porId.get(rs.getLong("id_usuario")).getEstudios().add(mapearEstudio(rs));
				}, ids);

		jdbc.query("SELECT t.id_usuario, t.url_perfil, t.alias, r.id_red_social, r.nombre_red_social, r.url "
				+ "FROM tiene t JOIN red_social r ON r.id_red_social = t.id_red_social " + "WHERE t.id_usuario IN ("
				+ in + ")", (RowCallbackHandler) rs -> {
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
		RedSocial red = new RedSocial(rs.getString("nombre_red_social"), rs.getString("url"));
		red.setIdRedSocial(rs.getLong("id_red_social"));
		return new Tiene(red, rs.getString("url_perfil"), rs.getString("alias"));
	}

	private long contar(String sql, Object... args) {
		Long n = jdbc.queryForObject(sql, Long.class, args);
		return n == null ? 0 : n;
	}
}
