package co.edu.unbosque.empareja2.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import co.edu.unbosque.empareja2.exception.LanzadorException;

/**
 * Aplica al arrancar los cambios de esquema que exige esta version si la base
 * todavia no los tiene (el proyecto no usa Flyway ni Liquibase). Hoy: la columna
 * reporte.estado. El mismo cambio esta en resources/db/migracion_reporte_estado.sql
 * por si el usuario de la base no tiene permiso ALTER.
 *
 * Ademas verifica que ya se corrio resources/db/migracion_3fn.sql (tabla de union
 * estudiante_red_social + catalogo red_social) y, si no, detiene el arranque con un
 * mensaje claro en lugar de fallar mas tarde con errores SQL.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class EsquemaMigrador implements CommandLineRunner {

    /** Registro de eventos de la clase. */
    private static final Logger log = LoggerFactory.getLogger(EsquemaMigrador.class);

    /** Herramienta de Spring para ejecutar sentencias SQL con JDBC. */
    private final JdbcTemplate jdbc;

    /**
     * Crea el migrador.
     *
     * @param jdbc el JdbcTemplate conectado a la base de datos
     */
    public EsquemaMigrador(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /**
     * Verifica la migracion a 3FN y crea la columna reporte.estado si falta.
     *
     * @param args los argumentos de arranque (no se usan)
     */
    @Override
    public void run(String... args) {
        exigirMigracion3fn();
        if (existeColumna("reporte", "estado")) {
            return;
        }
        try {
            jdbc.execute("ALTER TABLE reporte ADD COLUMN estado VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE'");
            log.info("Migracion aplicada: columna reporte.estado creada");
        } catch (DataAccessException ex) {
            LanzadorException.columnaEstadoReporteFaltante(ex);
        }
    }

    /**
     * Detiene el arranque si la tabla estudiante_red_social no existe.
     */
    private void exigirMigracion3fn() {
        Long cantidad = jdbc.queryForObject("SELECT COUNT(*) FROM information_schema.tables "
                + "WHERE table_schema = DATABASE() AND table_name = 'estudiante_red_social'", Long.class);
        if (cantidad == null || cantidad == 0) {
            LanzadorException.migracion3fnFaltante();
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
