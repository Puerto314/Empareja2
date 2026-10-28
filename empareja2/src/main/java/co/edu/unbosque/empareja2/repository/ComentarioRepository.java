package co.edu.unbosque.empareja2.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import co.edu.unbosque.empareja2.entity.Comentario;
import co.edu.unbosque.empareja2.entity.Usuario;

/**
 * Acceso a datos (JDBC) de la tabla comentario (entidad debil: llave
 * (id_autor, numero_comentario)).
 */
@Repository
public class ComentarioRepository {

    private static final String SELECT = "SELECT c.id_autor, c.numero_comentario, c.id_destinatario, c.contenido, "
            + "c.fecha_hora, a.nombre AS autor_nombre, a.apellido AS autor_apellido, "
            + "d.nombre AS dest_nombre, d.apellido AS dest_apellido FROM comentario c "
            + "JOIN usuario a ON a.id_usuario = c.id_autor JOIN usuario d ON d.id_usuario = c.id_destinatario ";

    private static final RowMapper<Comentario> MAPPER = (rs, i) -> {
        Usuario autor = new Usuario();
        autor.setId(rs.getLong("id_autor"));
        autor.setNombre(rs.getString("autor_nombre"));
        autor.setApellido(rs.getString("autor_apellido"));
        Usuario destinatario = new Usuario();
        destinatario.setId(rs.getLong("id_destinatario"));
        destinatario.setNombre(rs.getString("dest_nombre"));
        destinatario.setApellido(rs.getString("dest_apellido"));

        Comentario c = new Comentario(autor, destinatario, rs.getInt("numero_comentario"),
                rs.getString("contenido"));
        c.setFechaHora(rs.getObject("fecha_hora", LocalDateTime.class));
        return c;
    };

    private final JdbcTemplate jdbc;

    public ComentarioRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /** Bandeja de entrada: SOLO los comentarios dirigidos a ese usuario. */
    public List<Comentario> listarRecibidos(Long idUsuario) {
        return jdbc.query(SELECT + "WHERE c.id_destinatario = ? ORDER BY c.fecha_hora DESC, c.numero_comentario DESC",
                MAPPER, idUsuario);
    }

    public List<Comentario> listarEnviados(Long idUsuario) {
        return jdbc.query(SELECT + "WHERE c.id_autor = ? ORDER BY c.fecha_hora DESC, c.numero_comentario DESC",
                MAPPER, idUsuario);
    }

    /** Todos los comentarios (solo para moderacion por un administrador). */
    public List<Comentario> listarTodos() {
        return jdbc.query(SELECT + "ORDER BY c.fecha_hora DESC, c.id_autor, c.numero_comentario DESC", MAPPER);
    }

    /** Elimina un comentario por su llave (id_autor, numero_comentario); devuelve cuantas filas borro (0 o 1). */
    public int eliminar(Long idAutor, Integer numero) {
        return jdbc.update("DELETE FROM comentario WHERE id_autor = ? AND numero_comentario = ?", idAutor, numero);
    }

    /** Mayor numero_comentario usado por el autor (0 si no tiene ninguno). */
    public Integer maximoNumero(Long idAutor) {
        return jdbc.queryForObject("SELECT COALESCE(MAX(numero_comentario), 0) FROM comentario WHERE id_autor = ?",
                Integer.class, idAutor);
    }

    public Comentario save(Comentario c) {
        jdbc.update("INSERT INTO comentario (id_autor, numero_comentario, id_destinatario, contenido, fecha_hora) "
                + "VALUES (?, ?, ?, ?, ?)", c.getAutor().getId(), c.getNumeroComentario(),
                c.getDestinatario().getId(), c.getContenido(), c.getFechaHora());
        return c;
    }
}
