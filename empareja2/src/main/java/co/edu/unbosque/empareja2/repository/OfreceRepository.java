package co.edu.unbosque.empareja2.repository;

import java.util.List;

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

    private static final String SELECT = "SELECT o.facultad, c.id_carrera, c.nombre AS carrera_nombre, "
            + "u.id_universidad, u.nombre AS universidad_nombre, u.sigla "
            + "FROM ofrece o JOIN carrera c ON c.id_carrera = o.id_carrera "
            + "JOIN universidad u ON u.id_universidad = o.id_universidad ";

    private static final RowMapper<Ofrece> MAPPER = (rs, i) -> {
        Carrera carrera = new Carrera(rs.getString("carrera_nombre"));
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
        Integer n = jdbc.queryForObject("SELECT COUNT(*) FROM ofrece WHERE id_carrera = ? AND id_universidad = ?",
                Integer.class, idCarrera, idUniversidad);
        return n != null && n > 0;
    }

    public List<Ofrece> listarTodas() {
        return jdbc.query(SELECT + "ORDER BY u.nombre, c.nombre", MAPPER);
    }

    public List<Ofrece> listarPorUniversidad(Long idUniversidad) {
        return jdbc.query(SELECT + "WHERE o.id_universidad = ? ORDER BY c.nombre", MAPPER, idUniversidad);
    }

    public Ofrece save(Ofrece ofrece) {
        jdbc.update("INSERT INTO ofrece (id_carrera, id_universidad, facultad) VALUES (?, ?, ?)",
                ofrece.getCarrera().getIdCarrera(), ofrece.getUniversidad().getIdUniversidad(),
                ofrece.getFacultad());
        return ofrece;
    }
}
