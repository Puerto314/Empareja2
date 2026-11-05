package co.edu.unbosque.empareja2.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
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

    /** Consulta base: la universidad con los datos de su ciudad. */
    private static final String SELECT = "SELECT u.id_universidad, u.nombre AS nombre_universidad, u.sigla, "
            + "u.calle, u.barrio, c.id_ciudad, c.nombre AS nombre_ciudad, c.departamento "
            + "FROM universidad u JOIN ciudad c ON c.id_ciudad = u.id_ciudad ";

    /** Herramienta de Spring para ejecutar sentencias SQL con JDBC. */
    private final JdbcTemplate jdbc;

    /** Inserta en la tabla universidad y devuelve el id que genera la base de datos. */
    private final SimpleJdbcInsert insertar;

    /**
     * Crea el repositorio.
     *
     * @param jdbc el JdbcTemplate conectado a la base de datos
     */
    public UniversidadRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
        this.insertar = new SimpleJdbcInsert(jdbc).withTableName("universidad")
                .usingGeneratedKeyColumns("id_universidad");
    }

    /**
     * Convierte la fila actual del resultado en una Universidad.
     *
     * @param rs   el resultado de la consulta, posicionado en una fila
     * @param fila el numero de la fila
     * @return la universidad de esa fila, con su direccion y su ciudad
     * @throws SQLException si falla la lectura de una columna
     */
    private Universidad mapear(ResultSet rs, int fila) throws SQLException {
        Ciudad ciudad = new Ciudad(rs.getString("nombre_ciudad"), rs.getString("departamento"));
        ciudad.setIdCiudad(rs.getLong("id_ciudad"));

        Direccion direccion = new Direccion(rs.getString("calle"), rs.getString("barrio"));
        Universidad universidad = new Universidad(rs.getString("nombre_universidad"), rs.getString("sigla"),
                direccion, ciudad);
        universidad.setIdUniversidad(rs.getLong("id_universidad"));
        return universidad;
    }

    /**
     * Busca una universidad por su id.
     *
     * @param id el identificador de la universidad
     * @return la universidad, o vacio si no existe
     */
    public Optional<Universidad> buscarPorId(Long id) {
        return jdbc.query(SELECT + "WHERE u.id_universidad = ?", this::mapear, id).stream().findFirst();
    }

    /**
     * Lista todas las universidades ordenadas por nombre.
     *
     * @return las universidades
     */
    public List<Universidad> listarTodas() {
        return jdbc.query(SELECT + "ORDER BY u.nombre", this::mapear);
    }

    /**
     * Busca por nombre o sigla (contiene el texto, sin distinguir mayusculas).
     *
     * @param texto el texto a buscar
     * @return las universidades que coinciden
     */
    public List<Universidad> listarPorTexto(String texto) {
        String patron = "%" + texto.toLowerCase() + "%";
        return jdbc.query(SELECT + "WHERE LOWER(u.nombre) LIKE ? OR LOWER(u.sigla) LIKE ? ORDER BY u.nombre",
                this::mapear, patron, patron);
    }

    /**
     * Indica si existe una universidad con ese id.
     *
     * @param id el identificador de la universidad
     * @return true si existe
     */
    public boolean existePorId(Long id) {
        return Sql.contar(jdbc, "SELECT COUNT(*) FROM universidad WHERE id_universidad = ?", id) > 0;
    }

    /**
     * Indica si ya existe una universidad con ese nombre (sin distinguir mayusculas).
     *
     * @param nombre el nombre de la universidad
     * @return true si ya existe
     */
    public boolean existePorNombre(String nombre) {
        return Sql.contar(jdbc, "SELECT COUNT(*) FROM universidad WHERE LOWER(nombre) = LOWER(?)", nombre) > 0;
    }

    /**
     * Indica si ya existe una universidad con esa sigla (sin distinguir mayusculas).
     *
     * @param sigla la sigla de la universidad
     * @return true si ya existe
     */
    public boolean existePorSigla(String sigla) {
        return Sql.contar(jdbc, "SELECT COUNT(*) FROM universidad WHERE LOWER(sigla) = LOWER(?)", sigla) > 0;
    }

    /**
     * Igual que existePorNombre pero ignorando una universidad (para editar sin
     * falso duplicado).
     *
     * @param nombre el nombre de la universidad
     * @param id     el id de la universidad que se ignora
     * @return true si existe otra universidad con ese nombre
     */
    public boolean existePorNombreExceptoId(String nombre, Long id) {
        return Sql.contar(jdbc,
                "SELECT COUNT(*) FROM universidad WHERE LOWER(nombre) = LOWER(?) AND id_universidad <> ?", nombre,
                id) > 0;
    }

    /**
     * Igual que existePorSigla pero ignorando una universidad (para editar sin
     * falso duplicado).
     *
     * @param sigla la sigla de la universidad
     * @param id    el id de la universidad que se ignora
     * @return true si existe otra universidad con esa sigla
     */
    public boolean existePorSiglaExceptoId(String sigla, Long id) {
        return Sql.contar(jdbc,
                "SELECT COUNT(*) FROM universidad WHERE LOWER(sigla) = LOWER(?) AND id_universidad <> ?", sigla,
                id) > 0;
    }

    /**
     * Cuenta todas las universidades.
     *
     * @return el numero de universidades
     */
    public long contar() {
        return Sql.contar(jdbc, "SELECT COUNT(*) FROM universidad");
    }

    /**
     * Cuantas universidades hay en la ciudad.
     *
     * @param idCiudad el identificador de la ciudad
     * @return el numero de universidades
     */
    public long contarPorCiudad(Long idCiudad) {
        return Sql.contar(jdbc, "SELECT COUNT(*) FROM universidad WHERE id_ciudad = ?", idCiudad);
    }

    /**
     * Cuantos usuarios estudian en la universidad.
     *
     * @param idUniversidad el identificador de la universidad
     * @return el numero de estudiantes
     */
    public long contarEstudiantes(Long idUniversidad) {
        return Sql.contar(jdbc, "SELECT COUNT(*) FROM estudiante WHERE id_universidad = ?", idUniversidad);
    }

    /**
     * Inserta una universidad nueva y le asigna el id que genero la base de datos.
     *
     * @param universidad la universidad a guardar
     * @return la misma universidad, ya con su id
     */
    public Universidad guardar(Universidad universidad) {
        Direccion direccion = universidad.getDireccion() != null ? universidad.getDireccion() : new Direccion();
        Map<String, Object> valores = new HashMap<>();
        valores.put("nombre", universidad.getNombre());
        valores.put("sigla", universidad.getSigla());
        valores.put("calle", direccion.getCalle());
        valores.put("barrio", direccion.getBarrio());
        valores.put("id_ciudad", universidad.getCiudad().getIdCiudad());
        Number id = insertar.executeAndReturnKey(valores);
        universidad.setIdUniversidad(id.longValue());
        return universidad;
    }

    /**
     * Inserta varias universidades, una por una.
     *
     * @param universidades las universidades a guardar
     * @return las universidades guardadas, ya con su id
     */
    public List<Universidad> guardarTodas(List<Universidad> universidades) {
        List<Universidad> guardadas = new ArrayList<>();
        for (Universidad universidad : universidades) {
            guardadas.add(guardar(universidad));
        }
        return guardadas;
    }

    /**
     * Actualiza los datos de una universidad existente.
     *
     * @param universidad la universidad con los datos nuevos
     */
    public void actualizar(Universidad universidad) {
        Direccion direccion = universidad.getDireccion() != null ? universidad.getDireccion() : new Direccion();
        jdbc.update("UPDATE universidad SET nombre = ?, sigla = ?, calle = ?, barrio = ?, "
                + "id_ciudad = ? WHERE id_universidad = ?", universidad.getNombre(), universidad.getSigla(),
                direccion.getCalle(), direccion.getBarrio(), universidad.getCiudad().getIdCiudad(),
                universidad.getIdUniversidad());
    }

    /**
     * Elimina una universidad.
     *
     * @param id el identificador de la universidad
     */
    public void eliminar(Long id) {
        jdbc.update("DELETE FROM universidad WHERE id_universidad = ?", id);
    }
}
