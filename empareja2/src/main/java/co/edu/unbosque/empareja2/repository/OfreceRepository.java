package co.edu.unbosque.empareja2.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import co.edu.unbosque.empareja2.entity.Carrera;
import co.edu.unbosque.empareja2.entity.Ofrece;
import co.edu.unbosque.empareja2.entity.Universidad;

/**
 * Acceso a datos (JDBC) de la relacion ofrecer (carrera - universidad - facultad).
 */
@Repository
public class OfreceRepository {

    /** Consulta base: la oferta con los datos de su carrera y su universidad. */
    private static final String SELECT = "SELECT c.facultad, c.id_carrera, c.nombre_carrera AS carrera_nombre, "
            + "u.id_universidad, u.nombre_universidad AS universidad_nombre, u.sigla "
            + "FROM ofrecer o JOIN carrera c ON c.id_carrera = o.id_carrera "
            + "JOIN universidad u ON u.id_universidad = o.id_universidad ";

    /** Herramienta de Spring para ejecutar sentencias SQL con JDBC. */
    private final JdbcTemplate jdbc;

    /**
     * Crea el repositorio.
     *
     * @param jdbc el JdbcTemplate conectado a la base de datos
     */
    public OfreceRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /**
     * Convierte la fila actual del resultado en una Ofrece.
     *
     * @param rs   el resultado de la consulta, posicionado en una fila
     * @param fila el numero de la fila
     * @return la oferta de esa fila
     * @throws SQLException si falla la lectura de una columna
     */
    private Ofrece mapear(ResultSet rs, int fila) throws SQLException {
        Carrera carrera = new Carrera(rs.getString("carrera_nombre"), rs.getString("facultad"));
        carrera.setIdCarrera(rs.getLong("id_carrera"));

        Universidad universidad = new Universidad();
        universidad.setIdUniversidad(rs.getLong("id_universidad"));
        universidad.setNombre(rs.getString("universidad_nombre"));
        universidad.setSigla(rs.getString("sigla"));

        return new Ofrece(carrera, universidad, rs.getString("facultad"));
    }

    /**
     * Indica si una universidad ofrece una carrera.
     *
     * @param idCarrera     el identificador de la carrera
     * @param idUniversidad el identificador de la universidad
     * @return true si la oferta existe
     */
    public boolean existePorCarreraYUniversidad(Long idCarrera, Long idUniversidad) {
        return Sql.contar(jdbc, "SELECT COUNT(*) FROM ofrecer WHERE id_carrera = ? AND id_universidad = ?",
                idCarrera, idUniversidad) > 0;
    }

    /**
     * Lista toda la oferta academica ordenada por universidad y carrera.
     *
     * @return todas las ofertas
     */
    public List<Ofrece> listarTodas() {
        return jdbc.query(SELECT + "ORDER BY u.nombre_universidad, c.nombre_carrera", this::mapear);
    }

    /**
     * Lista las carreras que ofrece una universidad.
     *
     * @param idUniversidad el identificador de la universidad
     * @return las ofertas de esa universidad
     */
    public List<Ofrece> listarPorUniversidad(Long idUniversidad) {
        return jdbc.query(SELECT + "WHERE o.id_universidad = ? ORDER BY c.nombre_carrera", this::mapear, idUniversidad);
    }

    /**
     * Busca una oferta por su llave (carrera + universidad).
     *
     * @param idCarrera     el identificador de la carrera
     * @param idUniversidad el identificador de la universidad
     * @return la oferta, o vacio si no existe
     */
    public Optional<Ofrece> buscarPorIds(Long idCarrera, Long idUniversidad) {
        return jdbc.query(SELECT + "WHERE o.id_carrera = ? AND o.id_universidad = ?", this::mapear, idCarrera,
                idUniversidad).stream().findFirst();
    }

    /**
     * Cuantos usuarios estudian esa carrera en esa universidad (dependen de que la
     * oferta exista).
     *
     * @param idCarrera     el identificador de la carrera
     * @param idUniversidad el identificador de la universidad
     * @return el numero de estudiantes
     */
    public long contarEstudiantes(Long idCarrera, Long idUniversidad) {
        return Sql.contar(jdbc, "SELECT COUNT(*) FROM estudiante WHERE id_carrera = ? AND id_universidad = ?",
                idCarrera, idUniversidad);
    }

    /**
     * Registra la pareja carrera - universidad. La facultad es un atributo de la
     * carrera (no de la oferta): aqui solo se guarda la pareja.
     *
     * @param ofrece la oferta a guardar
     * @return la misma oferta
     */
    public Ofrece guardar(Ofrece ofrece) {
        jdbc.update("INSERT INTO ofrecer (id_universidad, id_carrera) SELECT un.id_universidad, ? FROM universidad un "
                + "WHERE un.id_universidad = ?", ofrece.getCarrera().getIdCarrera(),
                ofrece.getUniversidad().getIdUniversidad());
        return ofrece;
    }

    /**
     * Cambia la facultad de la CARRERA de la oferta (en el modelo la facultad es de
     * la carrera).
     *
     * @param ofrece la oferta con la facultad nueva
     */
    public void actualizar(Ofrece ofrece) {
        jdbc.update("UPDATE carrera SET facultad = ? WHERE id_carrera = ?", ofrece.getFacultad(),
                ofrece.getCarrera().getIdCarrera());
    }

    /**
     * Elimina una oferta.
     *
     * @param idCarrera     el identificador de la carrera
     * @param idUniversidad el identificador de la universidad
     */
    public void eliminar(Long idCarrera, Long idUniversidad) {
        jdbc.update("DELETE FROM ofrecer WHERE id_carrera = ? AND id_universidad = ?", idCarrera, idUniversidad);
    }

    /**
     * Se usa al eliminar una universidad: sus ofertas se van con ella.
     *
     * @param idUniversidad el identificador de la universidad
     */
    public void eliminarPorUniversidad(Long idUniversidad) {
        jdbc.update("DELETE FROM ofrecer WHERE id_universidad = ?", idUniversidad);
    }

    /**
     * Se usa al eliminar una carrera: sus ofertas se van con ella.
     *
     * @param idCarrera el identificador de la carrera
     */
    public void eliminarPorCarrera(Long idCarrera) {
        jdbc.update("DELETE FROM ofrecer WHERE id_carrera = ?", idCarrera);
    }
}
