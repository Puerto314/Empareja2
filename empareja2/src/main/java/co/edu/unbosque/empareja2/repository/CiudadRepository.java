package co.edu.unbosque.empareja2.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import co.edu.unbosque.empareja2.entity.Ciudad;

/**
 * Acceso a datos (JDBC) de la tabla ciudad.
 */
@Repository
public class CiudadRepository {

    private static final RowMapper<Ciudad> MAPPER = (rs, i) -> {
        Ciudad c = new Ciudad(rs.getString("nombre"), rs.getString("departamento"));
        c.setIdCiudad(rs.getLong("id_ciudad"));
        return c;
    };

    private final JdbcTemplate jdbc;
    private final SimpleJdbcInsert insert;

    public CiudadRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
        this.insert = new SimpleJdbcInsert(jdbc).withTableName("ciudad").usingGeneratedKeyColumns("id_ciudad");
    }

    public Optional<Ciudad> findById(Long id) {
        return jdbc.query("SELECT id_ciudad, nombre, departamento FROM ciudad WHERE id_ciudad = ?", MAPPER, id)
                .stream().findFirst();
    }

    public boolean existsByNombreIgnoreCaseAndDepartamentoIgnoreCase(String nombre, String departamento) {
        Integer n = jdbc.queryForObject(
                "SELECT COUNT(*) FROM ciudad WHERE LOWER(nombre) = LOWER(?) AND LOWER(departamento) = LOWER(?)",
                Integer.class, nombre, departamento);
        return n != null && n > 0;
    }

    public List<Ciudad> findAllByOrderByNombreAsc() {
        return jdbc.query("SELECT id_ciudad, nombre, departamento FROM ciudad ORDER BY nombre", MAPPER);
    }

    public Ciudad save(Ciudad ciudad) {
        Number id = insert.executeAndReturnKey(
                Map.of("nombre", ciudad.getNombre(), "departamento", ciudad.getDepartamento()));
        ciudad.setIdCiudad(id.longValue());
        return ciudad;
    }

    public boolean existsByNombreAndDepartamentoAndIdNot(String nombre, String departamento, Long id) {
        Integer n = jdbc.queryForObject("SELECT COUNT(*) FROM ciudad WHERE LOWER(nombre) = LOWER(?) "
                + "AND LOWER(departamento) = LOWER(?) AND id_ciudad <> ?", Integer.class, nombre, departamento, id);
        return n != null && n > 0;
    }

    public void update(Ciudad ciudad) {
        jdbc.update("UPDATE ciudad SET nombre = ?, departamento = ? WHERE id_ciudad = ?", ciudad.getNombre(),
                ciudad.getDepartamento(), ciudad.getIdCiudad());
    }

    public void deleteById(Long id) {
        jdbc.update("DELETE FROM ciudad WHERE id_ciudad = ?", id);
    }
}
