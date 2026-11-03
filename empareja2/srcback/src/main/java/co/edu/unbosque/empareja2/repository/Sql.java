package co.edu.unbosque.empareja2.repository;

import java.util.Collection;
import java.util.Collections;

/**
 * Utilidades pequenas para armar consultas JDBC.
 */
final class Sql {

    private Sql() {
    }

    /** "?, ?, ?" para n parametros (para clausulas IN). */
    static String placeholders(int n) {
        return String.join(", ", Collections.nCopies(n, "?"));
    }

    static Object[] toArray(Long first, Collection<Long> rest) {
        Object[] args = new Object[rest.size() + 1];
        args[0] = first;
        int i = 1;
        for (Long id : rest) {
            args[i++] = id;
        }
        return args;
    }
}
