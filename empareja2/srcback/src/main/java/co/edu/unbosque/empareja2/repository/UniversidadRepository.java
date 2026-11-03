package co.edu.unbosque.empareja2.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import co.edu.unbosque.empareja2.entity.Ciudad;
import co.edu.unbosque.empareja2.entity.Direccion;
import co.edu.unbosque.empareja2.entity.Universidad;

/**
 * Acceso a datos (JDBC) de la tabla universidad (con su ciudad).
 */
@Repository
public class UniversidadRepository {

    private static final String SELECT = "SELECT u.id_universidad, u.nombre_universidad, u.sigla, u.calle, u.barrio, "
            + "c.id_ciudad, c.nombre_ciudad, c.departamento "
            + "FROM universidad u JOIN ciudad c ON c.id_ciudad = u.ciudad_id_ciudad ";

    private static final RowMapper<Universidad> MAPPER = (rs, i) -> {
        Ciudad ciudad = new Ciudad(rs.getString("nombre_ciudad"), rs.getString("departamento"));
        ciudad.setIdCiudad(rs.getLong("id_ciudad"));
        Universidad u = new Universidad(rs.getString("nombre_universidad"), rs.getString("sigla"),
                new Direccion(rs.getString("calle"), rs.getString("barrio")),
                ciudad);
        u.setIdUniversidad(rs.getLong("id_universidad"));
        return u;
    };

    private final JdbcTemplate jdbc;
    private final SimpleJdbcInsert insert;

    public UniversidadRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
        this.insert = new SimpleJdbcInsert(jdbc).withTableName("universidad")
                .usingGeneratedKeyColumns("id_universidad");
    }

    public Optional<Universidad> findById(Long id) {
        return jdbc.query(SELECT + "WHERE u.id_universidad = ?", MAPPER, id).stream().findFirst();
    }

    public boolean existsById(Long id) {
        Integer n = jdbc.queryForObject("SELECT COUNT(*) FROM universidad WHERE id_universidad = ?", Integer.class,
                id);
        return n != null && n > 0;
    }

    public long count() {
        Long n = jdbc.queryForObject("SELECT COUNT(*) FROM universidad", Long.class);
        return n == null ? 0 : n;
    }

    public boolean existsByNombreIgnoreCase(String nombre) {
        Integer n = jdbc.queryForObject("SELECT COUNT(*) FROM universidad WHERE LOWER(nombre_universidad) = LOWER(?)",
                Integer.class, nombre);
        return n != null && n > 0;
    }

    public boolean existsBySiglaIgnoreCase(String sigla) {
        Integer n = jdbc.queryForObject("SELECT COUNT(*) FROM universidad WHERE LOWER(sigla) = LOWER(?)",
                Integer.class, sigla);
        return n != null && n > 0;
    }

    public List<Universidad> listarTodas() {
        return jdbc.query(SELECT + "ORDER BY u.nombre_universidad", MAPPER);
    }

    /** Busca por nombre o sigla (contiene el texto, sin distinguir mayusculas). */
    public List<Universidad> buscar(String texto) {
        String patron = "%" + texto.toLowerCase() + "%";
        return jdbc.query(SELECT + "WHERE LOWER(u.nombre_universidad) LIKE ? OR LOWER(u.sigla) LIKE ? ORDER BY u.nombre_universidad", MAPPER,
                patron, patron);
    }

    public Universidad save(Universidad u) {
        Direccion d = u.getDireccion() != null ? u.getDireccion() : new Direccion();
        Map<String, Object> valores = new HashMap<>();
        valores.put("nombre_universidad", u.getNombre());
        valores.put("sigla", u.getSigla());
        valores.put("calle", d.getCalle());
        valores.put("barrio", d.getBarrio());
        valores.put("ciudad_id_ciudad", u.getCiudad().getIdCiudad());
        Number id = insert.executeAndReturnKey(valores);
        u.setIdUniversidad(id.longValue());
        return u;
    }

    public List<Universidad> saveAll(List<Universidad> universidades) {
        List<Universidad> guardadas = new ArrayList<>();
        for (Universidad u : universidades) {
            guardadas.add(save(u));
        }
        return guardadas;
    }

    public boolean existsByNombreAndIdNot(String nombre, Long id) {
        Integer n = jdbc.queryForObject(
                "SELECT COUNT(*) FROM universidad WHERE LOWER(nombre_universidad) = LOWER(?) AND id_universidad <> ?",
                Integer.class, nombre, id);
        return n != null && n > 0;
    }

    public boolean existsBySiglaAndIdNot(String sigla, Long id) {
        Integer n = jdbc.queryForObject(
                "SELECT COUNT(*) FROM universidad WHERE LOWER(sigla) = LOWER(?) AND id_universidad <> ?",
                Integer.class, sigla, id);
        return n != null && n > 0;
    }

    /** Cuantas universidades hay en la ciudad. */
    public long contarPorCiudad(Long idCiudad) {
        Long n = jdbc.queryForObject("SELECT COUNT(*) FROM universidad WHERE ciudad_id_ciudad = ?", Long.class, idCiudad);
        return n == null ? 0 : n;
    }

    /** Cuantos usuarios estudian en la universidad. */
    public long contarEstudiantes(Long idUniversidad) {
        Long n = jdbc.queryForObject("SELECT COUNT(*) FROM estudiante WHERE universidad_id_universidad = ?", Long.class,
                idUniversidad);
        return n == null ? 0 : n;
    }

    public void update(Universidad u) {
        Direccion d = u.getDireccion() != null ? u.getDireccion() : new Direccion();
        jdbc.update("UPDATE universidad SET nombre_universidad = ?, sigla = ?, calle = ?, barrio = ?, "
                + "ciudad_id_ciudad = ? WHERE id_universidad = ?", u.getNombre(), u.getSigla(), d.getCalle(), d.getBarrio(),
                u.getCiudad().getIdCiudad(), u.getIdUniversidad());
    }

    public void deleteById(Long id) {
        jdbc.update("DELETE FROM universidad WHERE id_universidad = ?", id);
    }
}
