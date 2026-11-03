package co.edu.unbosque.empareja2.repository;

import java.util.List;

import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import co.edu.unbosque.empareja2.entity.Carrera;
import co.edu.unbosque.empareja2.entity.Ofrece;
import co.edu.unbosque.empareja2.entity.Universidad;

/**
 * Acceso a datos (JDBC) de la relacion ofrece (carrera - universidad - facultad).
 */
@Repository
public class OfreceRepository {

    private static final String SELECT = "SELECT c.facultad, c.id_carrera, c.nombre_carrera AS carrera_nombre, "
            + "u.id_universidad, u.nombre_universidad AS universidad_nombre, u.sigla "
            + "FROM ofrecer o JOIN carrera c ON c.id_carrera = o.carrera_id_carrera "
            + "JOIN universidad u ON u.id_universidad = o.universidad_id_universidad "
            + "AND u.ciudad_id_ciudad = o.universidad_ciudad_id_ciudad ";

    private static final RowMapper<Ofrece> MAPPER = (rs, i) -> {
        Carrera carrera = new Carrera(rs.getString("carrera_nombre"), rs.getString("facultad"));
        carrera.setIdCarrera(rs.getLong("id_carrera"));
        Universidad universidad = new Universidad();
        universidad.setIdUniversidad(rs.getLong("id_universidad"));
        universidad.setNombre(rs.getString("universidad_nombre"));
        universidad.setSigla(rs.getString("sigla"));
        return new Ofrece(carrera, universidad, rs.getString("facultad"));
    };

    private final JdbcTemplate jdbc;

    public OfreceRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public boolean existsByCarreraAndUniversidad(Long idCarrera, Long idUniversidad) {
        Integer n = jdbc.queryForObject(
                "SELECT COUNT(*) FROM ofrecer WHERE carrera_id_carrera = ? AND universidad_id_universidad = ?",
                Integer.class, idCarrera, idUniversidad);
        return n != null && n > 0;
    }

    public List<Ofrece> listarTodas() {
        return jdbc.query(SELECT + "ORDER BY u.nombre_universidad, c.nombre_carrera", MAPPER);
    }

    public List<Ofrece> listarPorUniversidad(Long idUniversidad) {
        return jdbc.query(SELECT + "WHERE o.universidad_id_universidad = ? ORDER BY c.nombre_carrera", MAPPER,
                idUniversidad);
    }

    /** La facultad es un atributo de la carrera (no de la oferta): aqui solo se registra la pareja. */
    public Ofrece save(Ofrece ofrece) {
        jdbc.update("INSERT INTO ofrecer (universidad_id_universidad, universidad_ciudad_id_ciudad, "
                + "carrera_id_carrera) SELECT un.id_universidad, un.ciudad_id_ciudad, ? FROM universidad un "
                + "WHERE un.id_universidad = ?", ofrece.getCarrera().getIdCarrera(),
                ofrece.getUniversidad().getIdUniversidad());
        return ofrece;
    }

    public Optional<Ofrece> findByIds(Long idCarrera, Long idUniversidad) {
        return jdbc.query(SELECT + "WHERE o.carrera_id_carrera = ? AND o.universidad_id_universidad = ?", MAPPER,
                idCarrera, idUniversidad).stream().findFirst();
    }

    /** Cuantos usuarios estudian esa carrera en esa universidad (dependen de que la oferta exista). */
    public long contarEstudiantes(Long idCarrera, Long idUniversidad) {
        Long n = jdbc.queryForObject(
                "SELECT COUNT(*) FROM estudiante WHERE carrera_id_carrera = ? AND universidad_id_universidad = ?",
                Long.class, idCarrera, idUniversidad);
        return n == null ? 0 : n;
    }

    /** Cambia la facultad de la CARRERA de la oferta (en el modelo la facultad es de la carrera). */
    public void update(Ofrece ofrece) {
        jdbc.update("UPDATE carrera SET facultad = ? WHERE id_carrera = ?", ofrece.getFacultad(),
                ofrece.getCarrera().getIdCarrera());
    }

    public void delete(Long idCarrera, Long idUniversidad) {
        jdbc.update("DELETE FROM ofrecer WHERE carrera_id_carrera = ? AND universidad_id_universidad = ?", idCarrera,
                idUniversidad);
    }

    /** Se usa al eliminar una universidad: sus ofertas se van con ella. */
    public void eliminarPorUniversidad(Long idUniversidad) {
        jdbc.update("DELETE FROM ofrecer WHERE universidad_id_universidad = ?", idUniversidad);
    }

    /** Se usa al eliminar una carrera: sus ofertas se van con ella. */
    public void eliminarPorCarrera(Long idCarrera) {
        jdbc.update("DELETE FROM ofrecer WHERE carrera_id_carrera = ?", idCarrera);
    }
}
