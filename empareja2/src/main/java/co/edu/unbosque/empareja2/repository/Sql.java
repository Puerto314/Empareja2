package co.edu.unbosque.empareja2.repository;

import java.util.Collections;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Utilidades pequenas que comparten los repositorios JDBC.
 */
final class Sql {

    /**
     * Esta clase solo tiene metodos estaticos; no se crean objetos de ella.
     */
    private Sql() {
    }

    /**
     * Arma los signos de interrogacion de una clausula IN. Por ejemplo, para 3
     * valores devuelve "?, ?, ?".
     *
     * @param cantidad cuantos valores tendra el IN
     * @return los signos de interrogacion separados por coma
     */
    static String placeholders(int cantidad) {
        return String.join(", ", Collections.nCopies(cantidad, "?"));
    }

    /**
     * Ejecuta una consulta SELECT COUNT(*) y devuelve su resultado.
     *
     * @param jdbc el JdbcTemplate con el que se ejecuta la consulta
     * @param sql  la consulta, con signos de interrogacion para los parametros
     * @param args los valores de los signos de interrogacion, en orden
     * @return el numero contado (0 si la base no devuelve nada)
     */
    static long contar(JdbcTemplate jdbc, String sql, Object... args) {
        Long total = jdbc.queryForObject(sql, Long.class, args);
        return total == null ? 0 : total;
    }
}
