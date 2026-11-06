package co.edu.unbosque.empareja2.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import co.edu.unbosque.empareja2.dto.ConteoReaccionesDTO;
import co.edu.unbosque.empareja2.entity.Reaccion;
import co.edu.unbosque.empareja2.entity.Usuario;

/**
 * Acceso a datos (JDBC) de la tabla reaccion (entidad debil: llave
 * (id_autor, numero_reaccion)).
 */
@Repository
public class ReaccionRepository {

    /** Consulta base: la reaccion con el nombre y la foto de su autor. */
    private static final String SELECT = "SELECT r.id_autor, r.numero_reaccion, r.id_destinatario, "
            + "r.fecha_hora_reaccion, a.nombre_usuario AS autor_nombre, "
            + "a.apellido AS autor_apellido, a.foto_perfil AS autor_foto "
            + "FROM reaccion r JOIN usuario a ON a.id_usuario = r.id_autor ";

    /** Consulta para administradores: ademas incluye el nombre del destinatario. */
    private static final String SELECT_ADMIN = "SELECT r.id_autor, r.numero_reaccion, r.id_destinatario, "
            + "r.fecha_hora_reaccion, a.nombre_usuario AS autor_nombre, "
            + "a.apellido AS autor_apellido, a.foto_perfil AS autor_foto, d.nombre_usuario AS dest_nombre, "
            + "d.apellido AS dest_apellido FROM reaccion r "
            + "JOIN usuario a ON a.id_usuario = r.id_autor "
            + "JOIN usuario d ON d.id_usuario = r.id_destinatario ";

    /** Herramienta de Spring para ejecutar sentencias SQL con JDBC. */
    private final JdbcTemplate jdbc;

    /**
     * Crea el repositorio.
     *
     * @param jdbc el JdbcTemplate conectado a la base de datos
     */
    public ReaccionRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /**
     * Convierte la fila actual del resultado en una Reaccion (con el autor completo
     * y solo el id del destinatario).
     *
     * @param rs   el resultado de la consulta, posicionado en una fila
     * @param fila el numero de la fila
     * @return la reaccion de esa fila
     * @throws SQLException si falla la lectura de una columna
     */
    private Reaccion mapear(ResultSet rs, int fila) throws SQLException {
        Usuario autor = new Usuario();
        autor.setId(rs.getLong("id_autor"));
        autor.setNombre(rs.getString("autor_nombre"));
        autor.setApellido(rs.getString("autor_apellido"));
        autor.setFotoPerfil(rs.getString("autor_foto"));

        Usuario destinatario = new Usuario();
        destinatario.setId(rs.getLong("id_destinatario"));

        Reaccion reaccion = new Reaccion(autor, destinatario, rs.getInt("numero_reaccion"));
        reaccion.setFechaHora(rs.getObject("fecha_hora_reaccion", LocalDateTime.class));
        return reaccion;
    }

    /**
     * Igual que mapear, pero ademas lee el nombre del destinatario (consulta de
     * administradores).
     *
     * @param rs   el resultado de la consulta, posicionado en una fila
     * @param fila el numero de la fila
     * @return la reaccion de esa fila, con el nombre del destinatario
     * @throws SQLException si falla la lectura de una columna
     */
    private Reaccion mapearParaAdmin(ResultSet rs, int fila) throws SQLException {
        Reaccion reaccion = mapear(rs, fila);
        reaccion.getDestinatario().setNombre(rs.getString("dest_nombre"));
        reaccion.getDestinatario().setApellido(rs.getString("dest_apellido"));
        return reaccion;
    }

    /**
     * Convierte la fila actual del resultado en un conteo de reacciones.
     *
     * @param rs   el resultado de la consulta, posicionado en una fila
     * @param fila el numero de la fila
     * @return el conteo de esa fila
     * @throws SQLException si falla la lectura de una columna
     */
    private ConteoReaccionesDTO mapearConteo(ResultSet rs, int fila) throws SQLException {
        return new ConteoReaccionesDTO(rs.getLong("id_destinatario"), rs.getLong("total"));
    }

    /**
     * Busca la reaccion que un autor dio a un destinatario.
     *
     * @param idAutor        el identificador del autor
     * @param idDestinatario el identificador del destinatario
     * @return la reaccion, o vacio si no existe
     */
    public Optional<Reaccion> buscar(Long idAutor, Long idDestinatario) {
        return jdbc.query(SELECT + "WHERE r.id_autor = ? AND r.id_destinatario = ?", this::mapear, idAutor,
                idDestinatario).stream().findFirst();
    }

    /**
     * Cuenta las reacciones que recibio un usuario.
     *
     * @param idDestinatario el identificador del destinatario
     * @return el total de reacciones
     */
    public long contar(Long idDestinatario) {
        return Sql.contar(jdbc, "SELECT COUNT(*) FROM reaccion WHERE id_destinatario = ?", idDestinatario);
    }

    /**
     * Quienes dieron la reaccion al perfil, la mas reciente primero (con nombre y
     * foto del autor).
     *
     * @param idDestinatario el identificador del perfil
     * @return las reacciones recibidas
     */
    public List<Reaccion> listarPorDestinatario(Long idDestinatario) {
        return jdbc.query(SELECT + "WHERE r.id_destinatario = ? "
                + "ORDER BY r.fecha_hora_reaccion DESC, r.numero_reaccion DESC", this::mapear, idDestinatario);
    }

    /**
     * Todas las reacciones con el nombre de autor y destinatario (solo para
     * administradores).
     *
     * @return todas las reacciones, de la mas reciente a la mas antigua
     */
    public List<Reaccion> listarTodas() {
        return jdbc.query(SELECT_ADMIN + "ORDER BY r.fecha_hora_reaccion DESC, r.id_autor, r.numero_reaccion DESC",
                this::mapearParaAdmin);
    }

    /**
     * Mayor numero de reaccion usado por el autor.
     *
     * @param idAutor el identificador del autor
     * @return el mayor numero (0 si no tiene ninguna)
     */
    public Integer maximoNumero(Long idAutor) {
        return jdbc.queryForObject("SELECT COALESCE(MAX(numero_reaccion), 0) FROM reaccion WHERE id_autor = ?",
                Integer.class, idAutor);
    }

    /**
     * Cuantas reacciones recibio cada uno de los usuarios indicados (los que
     * tienen 0 no aparecen).
     *
     * @param ids  los identificadores de los usuarios
     * @return un conteo por cada usuario que recibio al menos una reaccion
     */
    public List<ConteoReaccionesDTO> contarPorDestinatarios(Collection<Long> ids) {
        if (ids.isEmpty()) {
            return List.of();
        }
        return jdbc.query("SELECT id_destinatario, COUNT(*) AS total FROM reaccion "
                + "WHERE id_destinatario IN (" + Sql.placeholders(ids.size()) + ") GROUP BY id_destinatario",
                this::mapearConteo, ids.toArray());
    }

    /**
     * De los usuarios indicados, a cuales les dio la reaccion este autor.
     *
     * @param idAutor el identificador del autor
     * @param ids     los identificadores de los usuarios a revisar
     * @return los identificadores de los usuarios a los que el autor reacciono
     */
    public List<Long> destinatariosReaccionados(Long idAutor, Collection<Long> ids) {
        if (ids.isEmpty()) {
            return List.of();
        }
        List<Object> argumentos = new ArrayList<>();
        argumentos.add(idAutor);
        argumentos.addAll(ids);

        return jdbc.query("SELECT id_destinatario FROM reaccion WHERE id_autor = ? "
                + "AND id_destinatario IN (" + Sql.placeholders(ids.size()) + ")",
                (rs, fila) -> rs.getLong("id_destinatario"), argumentos.toArray());
    }

    /**
     * Inserta una reaccion nueva.
     *
     * @param reaccion la reaccion a guardar
     * @return la misma reaccion
     */
    public Reaccion guardar(Reaccion reaccion) {
        jdbc.update("INSERT INTO reaccion (id_autor, numero_reaccion, id_destinatario, fecha_hora_reaccion) "
                + "VALUES (?, ?, ?, ?)", reaccion.getAutor().getId(), reaccion.getNumeroReaccion(),
                reaccion.getDestinatario().getId(), reaccion.getFechaHora());
        return reaccion;
    }

    /**
     * Elimina una reaccion por su llave (id_autor, numero_reaccion).
     *
     * @param idAutor el identificador del autor
     * @param numero  el numero de la reaccion
     * @return cuantas filas borro (0 o 1)
     */
    public int eliminar(Long idAutor, Integer numero) {
        return jdbc.update("DELETE FROM reaccion WHERE id_autor = ? AND numero_reaccion = ?", idAutor, numero);
    }
}
