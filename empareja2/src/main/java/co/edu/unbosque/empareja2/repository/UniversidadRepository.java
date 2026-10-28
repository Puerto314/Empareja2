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

    private static final String SELECT = "SELECT u.id_universidad, u.nombre, u.sigla, u.calle, u.barrio, "
            + "u.numero_direccion, c.id_ciudad, c.nombre AS ciudad_nombre, c.departamento "
            + "FROM universidad u JOIN ciudad c ON c.id_ciudad = u.id_ciudad ";

    private static final RowMapper<Universidad> MAPPER = (rs, i) -> {
        Ciudad ciudad = new Ciudad(rs.getString("ciudad_nombre"), rs.getString("departamento"));
        ciudad.setIdCiudad(rs.getLong("id_ciudad"));
        Universidad u = new Universidad(rs.getString("nombre"), rs.getString("sigla"),
                new Direccion(rs.getString("calle"), rs.getString("barrio"), rs.getString("numero_direccion")),
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
        Integer n = jdbc.queryForObject("SELECT COUNT(*) FROM universidad WHERE LOWER(nombre) = LOWER(?)",
                Integer.class, nombre);
        return n != null && n > 0;
    }

    public boolean existsBySiglaIgnoreCase(String sigla) {
        Integer n = jdbc.queryForObject("SELECT COUNT(*) FROM universidad WHERE LOWER(sigla) = LOWER(?)",
                Integer.class, sigla);
        return n != null && n > 0;
    }

    public List<Universidad> listarTodas() {
        return jdbc.query(SELECT + "ORDER BY u.nombre", MAPPER);
    }

    /** Busca por nombre o sigla (contiene el texto, sin distinguir mayusculas). */
    public List<Universidad> buscar(String texto) {
        String patron = "%" + texto.toLowerCase() + "%";
        return jdbc.query(SELECT + "WHERE LOWER(u.nombre) LIKE ? OR LOWER(u.sigla) LIKE ? ORDER BY u.nombre", MAPPER,
                patron, patron);
    }

    public Universidad save(Universidad u) {
        Direccion d = u.getDireccion() != null ? u.getDireccion() : new Direccion();
        Map<String, Object> valores = new HashMap<>();
        valores.put("nombre", u.getNombre());
        valores.put("sigla", u.getSigla());
        valores.put("calle", d.getCalle());
        valores.put("barrio", d.getBarrio());
        valores.put("numero_direccion", d.getNumeroDireccion());
        valores.put("id_ciudad", u.getCiudad().getIdCiudad());
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
                "SELECT COUNT(*) FROM universidad WHERE LOWER(nombre) = LOWER(?) AND id_universidad <> ?",
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
        Long n = jdbc.queryForObject("SELECT COUNT(*) FROM universidad WHERE id_ciudad = ?", Long.class, idCiudad);
        return n == null ? 0 : n;
    }

    /** Cuantos usuarios estudian en la universidad. */
    public long contarEstudiantes(Long idUniversidad) {
        Long n = jdbc.queryForObject("SELECT COUNT(*) FROM estudio WHERE id_universidad = ?", Long.class,
                idUniversidad);
        return n == null ? 0 : n;
    }

    public void update(Universidad u) {
        Direccion d = u.getDireccion() != null ? u.getDireccion() : new Direccion();
        jdbc.update("UPDATE universidad SET nombre = ?, sigla = ?, calle = ?, barrio = ?, numero_direccion = ?, "
                + "id_ciudad = ? WHERE id_universidad = ?", u.getNombre(), u.getSigla(), d.getCalle(), d.getBarrio(),
                d.getNumeroDireccion(), u.getCiudad().getIdCiudad(), u.getIdUniversidad());
    }

    public void deleteById(Long id) {
        jdbc.update("DELETE FROM universidad WHERE id_universidad = ?", id);
    }
}
