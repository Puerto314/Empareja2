package co.edu.unbosque.empareja2.repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import co.edu.unbosque.empareja2.entity.Reaccion;
import co.edu.unbosque.empareja2.entity.TipoReaccion;
import co.edu.unbosque.empareja2.entity.Usuario;

/**
 * Acceso a datos (JDBC) de la tabla reaccion (entidad debil: llave
 * (id_autor, numero_reaccion)).
 */
@Repository
public class ReaccionRepository {

    private static final String SELECT = "SELECT r.id_autor, r.numero_reaccion, r.id_destinatario, "
            + "r.tipo_reaccion, r.fecha_hora, a.nombre AS autor_nombre, a.apellido AS autor_apellido, "
            + "a.foto_perfil AS autor_foto FROM reaccion r JOIN usuario a ON a.id_usuario = r.id_autor ";

    private static final RowMapper<Reaccion> MAPPER = (rs, i) -> {
        Usuario autor = new Usuario();
        autor.setId(rs.getLong("id_autor"));
        autor.setNombre(rs.getString("autor_nombre"));
        autor.setApellido(rs.getString("autor_apellido"));
        autor.setFotoPerfil(rs.getString("autor_foto"));
        Usuario destinatario = new Usuario();
        destinatario.setId(rs.getLong("id_destinatario"));

        Reaccion r = new Reaccion(autor, destinatario, rs.getInt("numero_reaccion"),
                TipoReaccion.valueOf(rs.getString("tipo_reaccion")));
        r.setFechaHora(rs.getObject("fecha_hora", LocalDateTime.class));
        return r;
    };

    private final JdbcTemplate jdbc;

    public ReaccionRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Optional<Reaccion> buscar(Long idAutor, Long idDestinatario, TipoReaccion tipo) {
        return jdbc.query(SELECT + "WHERE r.id_autor = ? AND r.id_destinatario = ? AND r.tipo_reaccion = ?", MAPPER,
                idAutor, idDestinatario, tipo.name()).stream().findFirst();
    }

    public long contar(Long idDestinatario, TipoReaccion tipo) {
        Long n = jdbc.queryForObject("SELECT COUNT(*) FROM reaccion WHERE id_destinatario = ? AND tipo_reaccion = ?",
                Long.class, idDestinatario, tipo.name());
        return n == null ? 0 : n;
    }

    /** Quienes dieron la reaccion al perfil, la mas reciente primero (con nombre y foto del autor). */
    public List<Reaccion> listarPorDestinatario(Long idDestinatario, TipoReaccion tipo) {
        return jdbc.query(SELECT + "WHERE r.id_destinatario = ? AND r.tipo_reaccion = ? "
                + "ORDER BY r.fecha_hora DESC, r.numero_reaccion DESC", MAPPER, idDestinatario, tipo.name());
    }

    /** Mayor numero_reaccion usado por el autor (0 si no tiene ninguna). */
    public Integer maximoNumero(Long idAutor) {
        return jdbc.queryForObject("SELECT COALESCE(MAX(numero_reaccion), 0) FROM reaccion WHERE id_autor = ?",
                Integer.class, idAutor);
    }

    /** Cuantas reacciones recibio cada uno de los usuarios indicados (los que tienen 0 no aparecen). */
    public List<ConteoReacciones> contarPorDestinatarios(Collection<Long> ids, TipoReaccion tipo) {
        if (ids.isEmpty()) {
            return List.of();
        }
        Object[] args = new Object[ids.size() + 1];
        args[0] = tipo.name();
        int k = 1;
        for (Long id : ids) {
            args[k++] = id;
        }
        return jdbc.query("SELECT id_destinatario, COUNT(*) AS total FROM reaccion WHERE tipo_reaccion = ? "
                + "AND id_destinatario IN (" + Sql.placeholders(ids.size()) + ") GROUP BY id_destinatario",
                (rs, i) -> new ConteoReacciones(rs.getLong("id_destinatario"), rs.getLong("total")), args);
    }

    /** De los usuarios indicados, a cuales les dio la reaccion este autor. */
    public List<Long> destinatariosReaccionados(Long idAutor, TipoReaccion tipo, Collection<Long> ids) {
        if (ids.isEmpty()) {
            return List.of();
        }
        Object[] args = new Object[ids.size() + 2];
        args[0] = idAutor;
        args[1] = tipo.name();
        int k = 2;
        for (Long id : ids) {
            args[k++] = id;
        }
        return jdbc.query("SELECT id_destinatario FROM reaccion WHERE id_autor = ? AND tipo_reaccion = ? "
                + "AND id_destinatario IN (" + Sql.placeholders(ids.size()) + ")",
                (rs, i) -> rs.getLong("id_destinatario"), args);
    }

    public Reaccion save(Reaccion r) {
        jdbc.update("INSERT INTO reaccion (id_autor, numero_reaccion, id_destinatario, tipo_reaccion, fecha_hora) "
                + "VALUES (?, ?, ?, ?, ?)", r.getAutor().getId(), r.getNumeroReaccion(),
                r.getDestinatario().getId(), r.getTipoReaccion().name(), r.getFechaHora());
        return r;
    }

    public void delete(Reaccion r) {
        jdbc.update("DELETE FROM reaccion WHERE id_autor = ? AND numero_reaccion = ?", r.getAutor().getId(),
                r.getNumeroReaccion());
    }
}
