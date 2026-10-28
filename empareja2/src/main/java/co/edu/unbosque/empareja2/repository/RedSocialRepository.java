package co.edu.unbosque.empareja2.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import co.edu.unbosque.empareja2.entity.RedSocial;

/**
 * Acceso a datos (JDBC) de la tabla red_social.
 */
@Repository
public class RedSocialRepository {

    private static final RowMapper<RedSocial> MAPPER = (rs, i) -> {
        RedSocial r = new RedSocial(rs.getString("nombre_red_social"), rs.getString("url"));
        r.setIdRedSocial(rs.getLong("id_red_social"));
        return r;
    };

    private final JdbcTemplate jdbc;
    private final SimpleJdbcInsert insert;

    public RedSocialRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
        this.insert = new SimpleJdbcInsert(jdbc).withTableName("red_social")
                .usingGeneratedKeyColumns("id_red_social");
    }

    public Optional<RedSocial> findById(Long id) {
        return jdbc.query("SELECT id_red_social, nombre_red_social, url FROM red_social WHERE id_red_social = ?",
                MAPPER, id).stream().findFirst();
    }

    public boolean existsByNombreRedSocialIgnoreCase(String nombre) {
        Integer n = jdbc.queryForObject(
                "SELECT COUNT(*) FROM red_social WHERE LOWER(nombre_red_social) = LOWER(?)", Integer.class, nombre);
        return n != null && n > 0;
    }

    public List<RedSocial> findAllByOrderByNombreRedSocialAsc() {
        return jdbc.query("SELECT id_red_social, nombre_red_social, url FROM red_social ORDER BY nombre_red_social",
                MAPPER);
    }

    public RedSocial save(RedSocial red) {
        Number id = insert.executeAndReturnKey(
                Map.of("nombre_red_social", red.getNombreRedSocial(), "url", red.getUrl()));
        red.setIdRedSocial(id.longValue());
        return red;
    }

    public List<RedSocial> saveAll(List<RedSocial> redes) {
        List<RedSocial> guardadas = new ArrayList<>();
        for (RedSocial red : redes) {
            guardadas.add(save(red));
        }
        return guardadas;
    }

    public boolean existsByNombreRedSocialAndIdNot(String nombre, Long id) {
        Integer n = jdbc.queryForObject("SELECT COUNT(*) FROM red_social WHERE LOWER(nombre_red_social) = LOWER(?) "
                + "AND id_red_social <> ?", Integer.class, nombre, id);
        return n != null && n > 0;
    }

    /** Cuantos usuarios tienen un perfil registrado en esta red social. */
    public long contarUsos(Long idRedSocial) {
        Long n = jdbc.queryForObject("SELECT COUNT(*) FROM tiene WHERE id_red_social = ?", Long.class, idRedSocial);
        return n == null ? 0 : n;
    }

    public void update(RedSocial red) {
        jdbc.update("UPDATE red_social SET nombre_red_social = ?, url = ? WHERE id_red_social = ?",
                red.getNombreRedSocial(), red.getUrl(), red.getIdRedSocial());
    }

    public void deleteById(Long id) {
        jdbc.update("DELETE FROM red_social WHERE id_red_social = ?", id);
    }
}
