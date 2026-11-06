package co.edu.unbosque.empareja2.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import co.edu.unbosque.empareja2.entity.Carrera;

/**
 * Acceso a datos (JDBC) de la tabla carrera.
 */
@Repository
public class CarreraRepository {

    /** Consulta base: las columnas de la tabla carrera. */
    private static final String SELECT = "SELECT id_carrera, nombre_carrera, facultad FROM carrera ";

    /** Herramienta de Spring para ejecutar sentencias SQL con JDBC. */
    private final JdbcTemplate jdbc;

    /** Inserta en la tabla carrera y devuelve el id que genera la base de datos. */
    private final SimpleJdbcInsert insertar;

    /**
     * Crea el repositorio.
     *
     * @param jdbc el JdbcTemplate conectado a la base de datos
     */
    public CarreraRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
        this.insertar = new SimpleJdbcInsert(jdbc).withTableName("carrera").usingGeneratedKeyColumns("id_carrera");
    }

    /**
     * Convierte la fila actual del resultado en una Carrera.
     *
     * @param rs   el resultado de la consulta, posicionado en una fila
     * @param fila el numero de la fila
     * @return la carrera de esa fila
     * @throws SQLException si falla la lectura de una columna
     */
    private Carrera mapear(ResultSet rs, int fila) throws SQLException {
        Carrera carrera = new Carrera(rs.getString("nombre_carrera"), rs.getString("facultad"));
        carrera.setIdCarrera(rs.getLong("id_carrera"));
        return carrera;
    }

    /**
     * Busca una carrera por su id.
     *
     * @param id el identificador de la carrera
     * @return la carrera, o vacio si no existe
     */
    public Optional<Carrera> buscarPorId(Long id) {
        return jdbc.query(SELECT + "WHERE id_carrera = ?", this::mapear, id).stream().findFirst();
    }

    /**
     * Lista todas las carreras ordenadas por nombre.
     *
     * @return las carreras
     */
    public List<Carrera> listarTodas() {
        return jdbc.query(SELECT + "ORDER BY nombre_carrera", this::mapear);
    }

    /**
     * Indica si ya existe una carrera con ese nombre (sin distinguir mayusculas).
     *
     * @param nombre el nombre de la carrera
     * @return true si ya existe
     */
    public boolean existePorNombre(String nombre) {
        return Sql.contar(jdbc, "SELECT COUNT(*) FROM carrera WHERE LOWER(nombre_carrera) = LOWER(?)", nombre) > 0;
    }

    /**
     * Igual que existePorNombre pero ignorando una carrera (para editar sin falso
     * duplicado).
     *
     * @param nombre el nombre de la carrera
     * @param id     el id de la carrera que se ignora
     * @return true si existe otra carrera con ese nombre
     */
    public boolean existePorNombreExceptoId(String nombre, Long id) {
        return Sql.contar(jdbc, "SELECT COUNT(*) FROM carrera WHERE LOWER(nombre_carrera) = LOWER(?) AND id_carrera <> ?",
                nombre, id) > 0;
    }

    /**
     * Cuantos usuarios estudian esta carrera (en cualquier universidad).
     *
     * @param idCarrera el identificador de la carrera
     * @return el numero de estudiantes
     */
    public long contarEstudiantes(Long idCarrera) {
        return Sql.contar(jdbc, "SELECT COUNT(*) FROM estudiante WHERE id_carrera = ?", idCarrera);
    }

    /**
     * Inserta una carrera nueva y le asigna el id que genero la base de datos.
     *
     * @param carrera la carrera a guardar
     * @return la misma carrera, ya con su id
     */
    public Carrera guardar(Carrera carrera) {
        Number id = insertar.executeAndReturnKey(Map.of("nombre_carrera", carrera.getNombre(), "facultad", carrera.getFacultad()));
        carrera.setIdCarrera(id.longValue());
        return carrera;
    }

    /**
     * Inserta varias carreras, una por una.
     *
     * @param carreras las carreras a guardar
     * @return las carreras guardadas, ya con su id
     */
    public List<Carrera> guardarTodas(List<Carrera> carreras) {
        List<Carrera> guardadas = new ArrayList<>();
        for (Carrera carrera : carreras) {
            guardadas.add(guardar(carrera));
        }
        return guardadas;
    }

    /**
     * Actualiza el nombre y la facultad de una carrera existente.
     *
     * @param carrera la carrera con los datos nuevos
     */
    public void actualizar(Carrera carrera) {
        jdbc.update("UPDATE carrera SET nombre_carrera = ?, facultad = ? WHERE id_carrera = ?", carrera.getNombre(),
                carrera.getFacultad(), carrera.getIdCarrera());
    }

    /**
     * Elimina una carrera.
     *
     * @param id el identificador de la carrera
     */
    public void eliminar(Long id) {
        jdbc.update("DELETE FROM carrera WHERE id_carrera = ?", id);
    }
}
