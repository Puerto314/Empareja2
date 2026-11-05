package co.edu.unbosque.empareja2.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import co.edu.unbosque.empareja2.entity.EstadoReporte;
import co.edu.unbosque.empareja2.entity.Reporte;
import co.edu.unbosque.empareja2.entity.TipoReporte;
import co.edu.unbosque.empareja2.entity.Usuario;

/**
 * Acceso a datos (JDBC) de la tabla reporte.
 */
@Repository
public class ReporteRepository {

    /** Consulta base: el reporte con el nombre de quien reporta y de quien es reportado. */
    private static final String SELECT = "SELECT r.id_reporte, r.id_realizador, r.id_reportado, "
            + "r.tipo AS tipo_reporte, r.estado, r.descripcion, r.fecha_hora, a.nombre AS realizador_nombre, "
            + "a.apellido AS realizador_apellido, b.nombre AS reportado_nombre, b.apellido AS reportado_apellido "
            + "FROM reporte r JOIN usuario a ON a.id_usuario = r.id_realizador "
            + "JOIN usuario b ON b.id_usuario = r.id_reportado ";

    /** Orden de los listados: del reporte mas reciente al mas antiguo. */
    private static final String ORDEN = "ORDER BY r.fecha_hora DESC, r.id_reporte DESC";

    /** Herramienta de Spring para ejecutar sentencias SQL con JDBC. */
    private final JdbcTemplate jdbc;

    /** Inserta en la tabla reporte y devuelve el id que genera la base de datos. */
    private final SimpleJdbcInsert insertar;

    /**
     * Crea el repositorio.
     *
     * @param jdbc el JdbcTemplate conectado a la base de datos
     */
    public ReporteRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
        this.insertar = new SimpleJdbcInsert(jdbc).withTableName("reporte").usingGeneratedKeyColumns("id_reporte");
    }

    /**
     * Convierte la fila actual del resultado en un Reporte.
     *
     * @param rs   el resultado de la consulta, posicionado en una fila
     * @param fila el numero de la fila
     * @return el reporte de esa fila, con los usuarios (id y nombre)
     * @throws SQLException si falla la lectura de una columna
     */
    private Reporte mapear(ResultSet rs, int fila) throws SQLException {
        Usuario realizador = new Usuario();
        realizador.setId(rs.getLong("id_realizador"));
        realizador.setNombre(rs.getString("realizador_nombre"));
        realizador.setApellido(rs.getString("realizador_apellido"));

        Usuario reportado = new Usuario();
        reportado.setId(rs.getLong("id_reportado"));
        reportado.setNombre(rs.getString("reportado_nombre"));
        reportado.setApellido(rs.getString("reportado_apellido"));

        Reporte reporte = new Reporte(realizador, reportado, TipoReporte.valueOf(rs.getString("tipo_reporte")),
                rs.getString("descripcion"));
        reporte.setIdReporte(rs.getLong("id_reporte"));
        reporte.setEstado(EstadoReporte.valueOf(rs.getString("estado")));
        reporte.setFechaHora(rs.getObject("fecha_hora", LocalDateTime.class));
        return reporte;
    }

    /**
     * Todos los reportes, del mas reciente al mas antiguo (solo para administradores).
     *
     * @return todos los reportes
     */
    public List<Reporte> listarTodos() {
        return jdbc.query(SELECT + ORDEN, this::mapear);
    }

    /**
     * Lista los reportes que estan en un estado.
     *
     * @param estado el estado buscado
     * @return los reportes en ese estado
     */
    public List<Reporte> listarPorEstado(EstadoReporte estado) {
        return jdbc.query(SELECT + "WHERE r.estado = ? " + ORDEN, this::mapear, estado.name());
    }

    /**
     * Busca un reporte por su id.
     *
     * @param id el identificador del reporte
     * @return el reporte, o vacio si no existe
     */
    public Optional<Reporte> buscarPorId(Long id) {
        return jdbc.query(SELECT + "WHERE r.id_reporte = ?", this::mapear, id).stream().findFirst();
    }

    /**
     * Indica si hay un reporte sin cerrar (PENDIENTE o EN_REVISION) del mismo
     * usuario al mismo perfil por el mismo motivo.
     *
     * @param idRealizador el identificador de quien reporta
     * @param idReportado  el identificador del usuario reportado
     * @param tipo         el motivo del reporte
     * @return true si ya hay un reporte activo igual
     */
    public boolean existeActivo(Long idRealizador, Long idReportado, TipoReporte tipo) {
        return Sql.contar(jdbc, "SELECT COUNT(*) FROM reporte WHERE id_realizador = ? AND id_reportado = ? "
                + "AND tipo = ? AND estado IN ('PENDIENTE', 'EN_REVISION')", idRealizador, idReportado,
                tipo.name()) > 0;
    }

    /**
     * Inserta un reporte nuevo y le asigna el id que genero la base de datos.
     *
     * @param reporte el reporte a guardar
     * @return el mismo reporte, ya con su id
     */
    public Reporte guardar(Reporte reporte) {
        Map<String, Object> valores = new HashMap<>();
        valores.put("id_realizador", reporte.getRealizador().getId());
        valores.put("id_reportado", reporte.getReportado().getId());
        valores.put("tipo", reporte.getTipoReporte().name());
        valores.put("estado", reporte.getEstado().name());
        valores.put("descripcion", reporte.getDescripcion());
        valores.put("fecha_hora", reporte.getFechaHora());
        reporte.setIdReporte(insertar.executeAndReturnKey(valores).longValue());
        return reporte;
    }

    /**
     * Cambia el estado de un reporte.
     *
     * @param id     el identificador del reporte
     * @param estado el estado nuevo
     */
    public void actualizarEstado(Long id, EstadoReporte estado) {
        jdbc.update("UPDATE reporte SET estado = ? WHERE id_reporte = ?", estado.name(), id);
    }

    /**
     * Elimina un reporte.
     *
     * @param id el identificador del reporte
     */
    public void eliminar(Long id) {
        jdbc.update("DELETE FROM reporte WHERE id_reporte = ?", id);
    }
}
