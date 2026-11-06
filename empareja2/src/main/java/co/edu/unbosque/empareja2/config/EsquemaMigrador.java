package co.edu.unbosque.empareja2.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import co.edu.unbosque.empareja2.exception.LanzadorException;

/**
 * Verifica al arrancar que la base de datos ya tiene el esquema que usa esta
 * version (el proyecto no usa Flyway ni Liquibase). Si todavia tiene los nombres
 * de atributos anteriores, detiene el arranque con un mensaje claro en lugar de
 * fallar mas tarde con errores SQL. El cambio se aplica una sola vez con
 * resources/db/migracion_nombres_atributos.sql.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class EsquemaMigrador implements CommandLineRunner {

    /** Herramienta de Spring para ejecutar sentencias SQL con JDBC. */
    private final JdbcTemplate jdbc;

    /**
     * Crea el verificador.
     *
     * @param jdbc el JdbcTemplate conectado a la base de datos
     */
    public EsquemaMigrador(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /**
     * Detiene el arranque si falta alguna de las columnas del esquema nuevo.
     *
     * @param args los argumentos de arranque (no se usan)
     */
    @Override
    public void run(String... args) {
        if (!existeColumna("usuario", "nombre_usuario") || !existeColumna("red_social", "id_usuario")
                || existeColumna("reaccion", "tipo")) {
            LanzadorException.esquemaDesactualizado();
        }
    }

    /**
     * Indica si una columna existe en la base de datos actual.
     *
     * @param tabla   el nombre de la tabla
     * @param columna el nombre de la columna
     * @return true si la columna existe
     */
    private boolean existeColumna(String tabla, String columna) {
        Long cantidad = jdbc.queryForObject("SELECT COUNT(*) FROM information_schema.columns "
                + "WHERE table_schema = DATABASE() AND table_name = ? AND column_name = ?", Long.class, tabla,
                columna);
        return cantidad != null && cantidad > 0;
    }
}
