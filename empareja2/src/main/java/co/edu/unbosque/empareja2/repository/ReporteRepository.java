package co.edu.unbosque.empareja2.repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
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

    private static final String SELECT = "SELECT r.id_reporte, r.id_realizador, r.id_reportado, r.tipo_reporte, "
            + "r.estado, r.descripcion, r.fecha_hora, a.nombre AS realizador_nombre, "
            + "a.apellido AS realizador_apellido, b.nombre AS reportado_nombre, b.apellido AS reportado_apellido "
            + "FROM reporte r JOIN usuario a ON a.id_usuario = r.id_realizador "
            + "JOIN usuario b ON b.id_usuario = r.id_reportado ";

    private static final String ORDEN = "ORDER BY r.fecha_hora DESC, r.id_reporte DESC";

    private static final RowMapper<Reporte> MAPPER = (rs, i) -> {
        Usuario realizador = new Usuario();
        realizador.setId(rs.getLong("id_realizador"));
        realizador.setNombre(rs.getString("realizador_nombre"));
        realizador.setApellido(rs.getString("realizador_apellido"));
        Usuario reportado = new Usuario();
        reportado.setId(rs.getLong("id_reportado"));
        reportado.setNombre(rs.getString("reportado_nombre"));
        reportado.setApellido(rs.getString("reportado_apellido"));

        Reporte r = new Reporte(realizador, reportado, TipoReporte.valueOf(rs.getString("tipo_reporte")),
                rs.getString("descripcion"));
        r.setIdReporte(rs.getLong("id_reporte"));
        r.setEstado(EstadoReporte.valueOf(rs.getString("estado")));
        r.setFechaHora(rs.getObject("fecha_hora", LocalDateTime.class));
        return r;
    };

    private final JdbcTemplate jdbc;
    private final SimpleJdbcInsert insert;

    public ReporteRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
        this.insert = new SimpleJdbcInsert(jdbc).withTableName("reporte").usingGeneratedKeyColumns("id_reporte");
    }

    /** Todos los reportes, del mas reciente al mas antiguo (solo para administradores). */
    public List<Reporte> listarTodos() {
        return jdbc.query(SELECT + ORDEN, MAPPER);
    }

    public List<Reporte> listarPorEstado(EstadoReporte estado) {
        return jdbc.query(SELECT + "WHERE r.estado = ? " + ORDEN, MAPPER, estado.name());
    }

    public Optional<Reporte> findById(Long id) {
        return jdbc.query(SELECT + "WHERE r.id_reporte = ?", MAPPER, id).stream().findFirst();
    }

    /** Hay un reporte sin cerrar (PENDIENTE o EN_REVISION) del mismo usuario al mismo perfil por el mismo motivo. */
    public boolean existeActivo(Long idRealizador, Long idReportado, TipoReporte tipo) {
        Integer n = jdbc.queryForObject("SELECT COUNT(*) FROM reporte WHERE id_realizador = ? AND id_reportado = ? "
                + "AND tipo_reporte = ? AND estado IN ('PENDIENTE', 'EN_REVISION')", Integer.class, idRealizador,
                idReportado, tipo.name());
        return n != null && n > 0;
    }

    public Reporte save(Reporte r) {
        Map<String, Object> valores = new HashMap<>();
        valores.put("id_realizador", r.getRealizador().getId());
        valores.put("id_reportado", r.getReportado().getId());
        valores.put("tipo_reporte", r.getTipoReporte().name());
        valores.put("estado", r.getEstado().name());
        valores.put("descripcion", r.getDescripcion());
        valores.put("fecha_hora", r.getFechaHora());
        r.setIdReporte(insert.executeAndReturnKey(valores).longValue());
        return r;
    }

    public void actualizarEstado(Long id, EstadoReporte estado) {
        jdbc.update("UPDATE reporte SET estado = ? WHERE id_reporte = ?", estado.name(), id);
    }

    public void deleteById(Long id) {
        jdbc.update("DELETE FROM reporte WHERE id_reporte = ?", id);
    }
}
