package co.edu.unbosque.empareja2.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import co.edu.unbosque.empareja2.entity.RedSocial;

/**
 * Acceso a datos (JDBC) del catalogo red_social(id_red_social, nombre, url_base).
 * Las redes de cada estudiante (con su nombre de usuario) viven en la tabla de union
 * estudiante_red_social, que maneja UsuarioRepository.
 */
@Repository
public class RedSocialRepository {

    /** Consulta base: las columnas de la tabla red_social. */
    private static final String SELECT = "SELECT id_red_social, nombre, url_base FROM red_social ";

    /** Herramienta de Spring para ejecutar sentencias SQL con JDBC. */
    private final JdbcTemplate jdbc;

    /**
     * Crea el repositorio.
     *
     * @param jdbc el JdbcTemplate conectado a la base de datos
     */
    public RedSocialRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /**
     * Convierte la fila actual del resultado en una RedSocial.
     *
     * @param rs   el resultado de la consulta, posicionado en una fila
     * @param fila el numero de la fila
     * @return la red social de esa fila
     * @throws SQLException si falla la lectura de una columna
     */
    private RedSocial mapear(ResultSet rs, int fila) throws SQLException {
        RedSocial red = new RedSocial(rs.getString("nombre"), rs.getString("url_base"));
        red.setIdRedSocial(rs.getLong("id_red_social"));
        return red;
    }

    /**
     * Busca una red social por su id.
     *
     * @param id el identificador de la red social
     * @return la red social, o vacio si no existe
     */
    public Optional<RedSocial> buscarPorId(Long id) {
        return jdbc.query(SELECT + "WHERE id_red_social = ?", this::mapear, id).stream().findFirst();
    }

    /**
     * Lista todas las redes sociales ordenadas por nombre.
     *
     * @return las redes sociales
     */
    public List<RedSocial> listarTodas() {
        return jdbc.query(SELECT + "ORDER BY nombre", this::mapear);
    }
}
