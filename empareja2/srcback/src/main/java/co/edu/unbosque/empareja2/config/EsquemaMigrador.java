package co.edu.unbosque.empareja2.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Aplica al arrancar los cambios de esquema que exige esta version si la base
 * todavia no los tiene (el proyecto no usa Flyway ni Liquibase). Hoy: la columna
 * reporte.estado. El mismo cambio esta en resources/db/migracion_reporte_estado.sql
 * por si el usuario de la base no tiene permiso ALTER.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class EsquemaMigrador implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(EsquemaMigrador.class);

    private final JdbcTemplate jdbc;

    public EsquemaMigrador(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void run(String... args) {
        if (existeColumna("reporte", "estado")) {
            return;
        }
        try {
            jdbc.execute("ALTER TABLE reporte ADD COLUMN estado VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE'");
            log.info("Migracion aplicada: columna reporte.estado creada");
        } catch (DataAccessException ex) {
            throw new IllegalStateException("Falta la columna reporte.estado y no se pudo crear automaticamente. "
                    + "Ejecuta src/main/resources/db/migracion_reporte_estado.sql con un usuario con permiso ALTER.",
                    ex);
        }
    }

    private boolean existeColumna(String tabla, String columna) {
        Integer n = jdbc.queryForObject("SELECT COUNT(*) FROM information_schema.columns "
                + "WHERE table_schema = DATABASE() AND table_name = ? AND column_name = ?", Integer.class, tabla,
                columna);
        return n != null && n > 0;
    }
}
