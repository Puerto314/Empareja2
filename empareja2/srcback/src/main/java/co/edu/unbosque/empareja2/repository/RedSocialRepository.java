package co.edu.unbosque.empareja2.repository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import co.edu.unbosque.empareja2.entity.RedSocial;

/**
 * Catalogo ESTATICO de redes sociales. El modelo relacional no tiene un catalogo
 * de redes: la tabla red_social guarda, por estudiante, la red y su nombre de
 * usuario. Los ids de esta lista son los que se guardan en red_social.id_red_social.
 */
@Repository
public class RedSocialRepository {

    private static final List<RedSocial> REDES = List.of(
            red(1L, "Instagram", "https://www.instagram.com"),
            red(2L, "Facebook", "https://www.facebook.com"),
            red(3L, "X", "https://x.com"),
            red(4L, "TikTok", "https://www.tiktok.com"),
            red(5L, "LinkedIn", "https://www.linkedin.com"),
            red(6L, "YouTube", "https://www.youtube.com"),
            red(7L, "Twitch", "https://www.twitch.tv"));

    private static RedSocial red(Long id, String nombre, String url) {
        RedSocial r = new RedSocial(nombre, url);
        r.setIdRedSocial(id);
        return r;
    }

    public Optional<RedSocial> findById(Long id) {
        return REDES.stream().filter(r -> r.getIdRedSocial().equals(id)).findFirst();
    }

    public List<RedSocial> findAllByOrderByNombreRedSocialAsc() {
        return REDES.stream().sorted(Comparator.comparing(RedSocial::getNombreRedSocial, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }
}
