package co.edu.unbosque.empareja2.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import co.edu.unbosque.empareja2.entity.Ciudad;

/**
 * Acceso a datos (JDBC) de la tabla ciudad.
 */
@Repository
public class CiudadRepository {

    /** Consulta base: las columnas de la tabla ciudad. */
    private static final String SELECT = "SELECT id_ciudad, nombre_ciudad, departamento FROM ciudad ";

    /** Herramienta de Spring para ejecutar sentencias SQL con JDBC. */
    private final JdbcTemplate jdbc;

    /** Inserta en la tabla ciudad y devuelve el id que genera la base de datos. */
    private final SimpleJdbcInsert insertar;

    /**
     * Crea el repositorio.
     *
     * @param jdbc el JdbcTemplate conectado a la base de datos
     */
    public CiudadRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
        this.insertar = new SimpleJdbcInsert(jdbc).withTableName("ciudad").usingGeneratedKeyColumns("id_ciudad");
    }

    /**
     * Convierte la fila actual del resultado en una Ciudad.
     *
     * @param rs   el resultado de la consulta, posicionado en una fila
     * @param fila el numero de la fila
     * @return la ciudad de esa fila
     * @throws SQLException si falla la lectura de una columna
     */
    private Ciudad mapear(ResultSet rs, int fila) throws SQLException {
        Ciudad ciudad = new Ciudad(rs.getString("nombre_ciudad"), rs.getString("departamento"));
        ciudad.setIdCiudad(rs.getLong("id_ciudad"));
        return ciudad;
    }

    /**
     * Busca una ciudad por su id.
     *
     * @param id el identificador de la ciudad
     * @return la ciudad, o vacio si no existe
     */
    public Optional<Ciudad> buscarPorId(Long id) {
        return jdbc.query(SELECT + "WHERE id_ciudad = ?", this::mapear, id).stream().findFirst();
    }

    /**
     * Lista todas las ciudades ordenadas por nombre.
     *
     * @return las ciudades
     */
    public List<Ciudad> listarTodas() {
        return jdbc.query(SELECT + "ORDER BY nombre_ciudad", this::mapear);
    }

    /**
     * Indica si ya existe una ciudad con ese nombre en ese departamento (sin
     * distinguir mayusculas).
     *
     * @param nombre       el nombre de la ciudad
     * @param departamento el departamento de la ciudad
     * @return true si ya existe
     */
    public boolean existePorNombreYDepartamento(String nombre, String departamento) {
        return Sql.contar(jdbc,
                "SELECT COUNT(*) FROM ciudad WHERE LOWER(nombre_ciudad) = LOWER(?) AND LOWER(departamento) = LOWER(?)",
                nombre, departamento) > 0;
    }

    /**
     * Igual que existePorNombreYDepartamento pero ignorando una ciudad (para
     * editar sin falso duplicado).
     *
     * @param nombre       el nombre de la ciudad
     * @param departamento el departamento de la ciudad
     * @param id           el id de la ciudad que se ignora
     * @return true si existe otra ciudad igual
     */
    public boolean existePorNombreYDepartamentoExceptoId(String nombre, String departamento, Long id) {
        return Sql.contar(jdbc, "SELECT COUNT(*) FROM ciudad WHERE LOWER(nombre_ciudad) = LOWER(?) "
                + "AND LOWER(departamento) = LOWER(?) AND id_ciudad <> ?", nombre, departamento, id) > 0;
    }

    /**
     * Inserta una ciudad nueva y le asigna el id que genero la base de datos.
     *
     * @param ciudad la ciudad a guardar
     * @return la misma ciudad, ya con su id
     */
    public Ciudad guardar(Ciudad ciudad) {
        Number id = insertar
                .executeAndReturnKey(Map.of("nombre_ciudad", ciudad.getNombre(), "departamento", ciudad.getDepartamento()));
        ciudad.setIdCiudad(id.longValue());
        return ciudad;
    }

    /**
     * Actualiza el nombre y el departamento de una ciudad existente.
     *
     * @param ciudad la ciudad con los datos nuevos
     */
    public void actualizar(Ciudad ciudad) {
        jdbc.update("UPDATE ciudad SET nombre_ciudad = ?, departamento = ? WHERE id_ciudad = ?", ciudad.getNombre(),
                ciudad.getDepartamento(), ciudad.getIdCiudad());
    }

    /**
     * Elimina una ciudad.
     *
     * @param id el identificador de la ciudad
     */
    public void eliminar(Long id) {
        jdbc.update("DELETE FROM ciudad WHERE id_ciudad = ?", id);
    }
}
