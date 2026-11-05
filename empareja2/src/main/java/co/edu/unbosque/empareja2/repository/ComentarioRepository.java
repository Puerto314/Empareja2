package co.edu.unbosque.empareja2.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import co.edu.unbosque.empareja2.entity.Comentario;
import co.edu.unbosque.empareja2.entity.Usuario;

/**
 * Acceso a datos (JDBC) de la tabla comentario (entidad debil: llave
 * (id_autor, numero_comentario)).
 */
@Repository
public class ComentarioRepository {

    /** Consulta base: el comentario con el nombre de su autor y de su destinatario. */
    private static final String SELECT = "SELECT c.id_autor, c.numero AS numero_comentario, c.id_destinatario, "
            + "c.contenido, c.fecha_hora, a.nombre AS autor_nombre, a.apellido AS autor_apellido, "
            + "d.nombre AS dest_nombre, d.apellido AS dest_apellido FROM comentario c "
            + "JOIN usuario a ON a.id_usuario = c.id_autor "
            + "JOIN usuario d ON d.id_usuario = c.id_destinatario ";

    /** Herramienta de Spring para ejecutar sentencias SQL con JDBC. */
    private final JdbcTemplate jdbc;

    /**
     * Crea el repositorio.
     *
     * @param jdbc el JdbcTemplate conectado a la base de datos
     */
    public ComentarioRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /**
     * Convierte la fila actual del resultado en un Comentario.
     *
     * @param rs   el resultado de la consulta, posicionado en una fila
     * @param fila el numero de la fila
     * @return el comentario de esa fila, con su autor y destinatario (id y nombre)
     * @throws SQLException si falla la lectura de una columna
     */
    private Comentario mapear(ResultSet rs, int fila) throws SQLException {
        Usuario autor = new Usuario();
        autor.setId(rs.getLong("id_autor"));
        autor.setNombre(rs.getString("autor_nombre"));
        autor.setApellido(rs.getString("autor_apellido"));

        Usuario destinatario = new Usuario();
        destinatario.setId(rs.getLong("id_destinatario"));
        destinatario.setNombre(rs.getString("dest_nombre"));
        destinatario.setApellido(rs.getString("dest_apellido"));

        Comentario comentario = new Comentario(autor, destinatario, rs.getInt("numero_comentario"),
                rs.getString("contenido"));
        comentario.setFechaHora(rs.getObject("fecha_hora", LocalDateTime.class));
        return comentario;
    }

    /**
     * Bandeja de entrada: SOLO los comentarios dirigidos a ese usuario, del mas
     * reciente al mas antiguo.
     *
     * @param idUsuario el identificador del destinatario
     * @return los comentarios recibidos
     */
    public List<Comentario> listarRecibidos(Long idUsuario) {
        return jdbc.query(SELECT + "WHERE c.id_destinatario = ? ORDER BY c.fecha_hora DESC, c.numero DESC",
                this::mapear, idUsuario);
    }

    /**
     * Lista los comentarios que escribio un usuario, del mas reciente al mas antiguo.
     *
     * @param idUsuario el identificador del autor
     * @return los comentarios enviados
     */
    public List<Comentario> listarEnviados(Long idUsuario) {
        return jdbc.query(SELECT + "WHERE c.id_autor = ? ORDER BY c.fecha_hora DESC, c.numero DESC", this::mapear,
                idUsuario);
    }

    /**
     * Todos los comentarios (solo para moderacion por un administrador).
     *
     * @return todos los comentarios, del mas reciente al mas antiguo
     */
    public List<Comentario> listarTodos() {
        return jdbc.query(SELECT + "ORDER BY c.fecha_hora DESC, c.id_autor, c.numero DESC", this::mapear);
    }

    /**
     * Mayor numero de comentario usado por el autor.
     *
     * @param idAutor el identificador del autor
     * @return el mayor numero (0 si no tiene ningun comentario)
     */
    public Integer maximoNumero(Long idAutor) {
        return jdbc.queryForObject("SELECT COALESCE(MAX(numero), 0) FROM comentario WHERE id_autor = ?",
                Integer.class, idAutor);
    }

    /**
     * Inserta un comentario nuevo.
     *
     * @param comentario el comentario a guardar
     * @return el mismo comentario
     */
    public Comentario guardar(Comentario comentario) {
        jdbc.update("INSERT INTO comentario (id_autor, numero, id_destinatario, contenido, fecha_hora) "
                + "VALUES (?, ?, ?, ?, ?)", comentario.getAutor().getId(), comentario.getNumeroComentario(),
                comentario.getDestinatario().getId(), comentario.getContenido(), comentario.getFechaHora());
        return comentario;
    }

    /**
     * Elimina un comentario por su llave (id_autor, numero_comentario).
     *
     * @param idAutor el identificador del autor
     * @param numero  el numero del comentario
     * @return cuantas filas borro (0 o 1)
     */
    public int eliminar(Long idAutor, Integer numero) {
        return jdbc.update("DELETE FROM comentario WHERE id_autor = ? AND numero = ?", idAutor, numero);
    }
}
