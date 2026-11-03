package co.edu.unbosque.empareja2.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import co.edu.unbosque.empareja2.entity.RedSocial;

/** El catalogo de redes sociales es estatico: el modelo relacional no tiene una tabla de catalogo. */
class RedSocialRepositoryTest {

    private final RedSocialRepository repo = new RedSocialRepository();

    @Test
    void listaLasSieteRedesOrdenadasPorNombre() {
        assertThat(repo.findAllByOrderByNombreRedSocialAsc()).extracting(RedSocial::getNombreRedSocial)
                .containsExactly("Facebook", "Instagram", "LinkedIn", "TikTok", "Twitch", "X", "YouTube");
    }

    @Test
    void buscaPorIdYDevuelveVacioSiNoExiste() {
        assertThat(repo.findById(1L)).get().extracting(RedSocial::getNombreRedSocial).isEqualTo("Instagram");
        assertThat(repo.findById(7L)).isPresent();
        assertThat(repo.findById(99L)).isEmpty();
    }
}
