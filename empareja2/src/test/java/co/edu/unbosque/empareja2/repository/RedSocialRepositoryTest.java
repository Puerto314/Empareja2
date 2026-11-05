package co.edu.unbosque.empareja2.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import co.edu.unbosque.empareja2.entity.RedSocial;

/** El catalogo de redes sociales vive en la tabla red_social (id_red_social, nombre, url_base). */
class RedSocialRepositoryTest {

    private final JdbcTemplate jdbc = mock(JdbcTemplate.class);
    private final RedSocialRepository repo = new RedSocialRepository(jdbc);

    private static RedSocial red(Long id, String nombre) {
        RedSocial r = new RedSocial(nombre, "https://" + nombre.toLowerCase() + ".com");
        r.setIdRedSocial(id);
        return r;
    }

    @Test
    @SuppressWarnings("unchecked")
    void listaElCatalogoOrdenadoPorNombreDesdeLaBase() {
        when(jdbc.query(anyString(), any(RowMapper.class))).thenReturn(List.of(red(2L, "Facebook"), red(1L, "Instagram")));

        assertThat(repo.listarTodas()).extracting(RedSocial::getNombreRedSocial)
                .containsExactly("Facebook", "Instagram");
    }

    @Test
    @SuppressWarnings("unchecked")
    void buscaPorIdYDevuelveVacioSiNoExiste() {
        when(jdbc.query(anyString(), any(RowMapper.class), eq(1L))).thenReturn(List.of(red(1L, "Instagram")));
        when(jdbc.query(anyString(), any(RowMapper.class), eq(99L))).thenReturn(List.of());

        assertThat(repo.buscarPorId(1L)).get().extracting(RedSocial::getNombreRedSocial).isEqualTo("Instagram");
        assertThat(repo.buscarPorId(99L)).isEmpty();
    }
}
