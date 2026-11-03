package co.edu.unbosque.empareja2.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import co.edu.unbosque.empareja2.entity.Carrera;

/**
 * Acceso a datos (JDBC) de la tabla carrera.
 */
@Repository
public class CarreraRepository {

    private static final RowMapper<Carrera> MAPPER = (rs, i) -> {
        Carrera c = new Carrera(rs.getString("nombre_carrera"), rs.getString("facultad"));
        c.setIdCarrera(rs.getLong("id_carrera"));
        return c;
    };

    private final JdbcTemplate jdbc;
    private final SimpleJdbcInsert insert;

    public CarreraRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
        this.insert = new SimpleJdbcInsert(jdbc).withTableName("carrera").usingGeneratedKeyColumns("id_carrera");
    }

    public Optional<Carrera> findById(Long id) {
        return jdbc.query("SELECT id_carrera, nombre_carrera, facultad FROM carrera WHERE id_carrera = ?", MAPPER, id).stream()
                .findFirst();
    }

    public boolean existsByNombreIgnoreCase(String nombre) {
        Integer n = jdbc.queryForObject("SELECT COUNT(*) FROM carrera WHERE LOWER(nombre_carrera) = LOWER(?)",
                Integer.class, nombre);
        return n != null && n > 0;
    }

    public List<Carrera> findAllByOrderByNombreAsc() {
        return jdbc.query("SELECT id_carrera, nombre_carrera, facultad FROM carrera ORDER BY nombre_carrera", MAPPER);
    }

    public Carrera save(Carrera carrera) {
        Number id = insert.executeAndReturnKey(Map.of("nombre_carrera", carrera.getNombre(), "facultad", carrera.getFacultad()));
        carrera.setIdCarrera(id.longValue());
        return carrera;
    }

    public List<Carrera> saveAll(List<Carrera> carreras) {
        List<Carrera> guardadas = new ArrayList<>();
        for (Carrera carrera : carreras) {
            guardadas.add(save(carrera));
        }
        return guardadas;
    }

    public boolean existsByNombreAndIdNot(String nombre, Long id) {
        Integer n = jdbc.queryForObject("SELECT COUNT(*) FROM carrera WHERE LOWER(nombre_carrera) = LOWER(?) AND id_carrera <> ?",
                Integer.class, nombre, id);
        return n != null && n > 0;
    }

    /** Cuantos usuarios estudian esta carrera (en cualquier universidad). */
    public long contarEstudiantes(Long idCarrera) {
        Long n = jdbc.queryForObject("SELECT COUNT(*) FROM estudiante WHERE carrera_id_carrera = ?", Long.class, idCarrera);
        return n == null ? 0 : n;
    }

    public void update(Carrera carrera) {
        jdbc.update("UPDATE carrera SET nombre_carrera = ?, facultad = ? WHERE id_carrera = ?", carrera.getNombre(),
                carrera.getFacultad(), carrera.getIdCarrera());
    }

    public void deleteById(Long id) {
        jdbc.update("DELETE FROM carrera WHERE id_carrera = ?", id);
    }
}
