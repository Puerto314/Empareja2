package co.edu.unbosque.empareja2.config;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.empareja2.entity.Carrera;
import co.edu.unbosque.empareja2.entity.Ciudad;
import co.edu.unbosque.empareja2.entity.Direccion;
import co.edu.unbosque.empareja2.entity.Ofrece;
import co.edu.unbosque.empareja2.entity.RedSocial;
import co.edu.unbosque.empareja2.entity.Universidad;
import co.edu.unbosque.empareja2.repository.CarreraRepository;
import co.edu.unbosque.empareja2.repository.CiudadRepository;
import co.edu.unbosque.empareja2.repository.OfreceRepository;
import co.edu.unbosque.empareja2.repository.RedSocialRepository;
import co.edu.unbosque.empareja2.repository.UniversidadRepository;

/**
 * Carga datos de referencia basicos la primera vez que arranca la aplicacion
 * (solo si no hay ninguna universidad), para que el registro y el filtro por
 * universidad se puedan probar de inmediato. Las direcciones de las
 * universidades se dejan vacias: un administrador las completa con
 * POST /api/admin/catalogos/universidades. Reemplaza estos datos por los reales.
 */
@Component
public class CatalogoSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(CatalogoSeeder.class);

    private final CiudadRepository ciudadRepository;
    private final UniversidadRepository universidadRepository;
    private final CarreraRepository carreraRepository;
    private final RedSocialRepository redSocialRepository;
    private final OfreceRepository ofreceRepository;

    public CatalogoSeeder(CiudadRepository ciudadRepository, UniversidadRepository universidadRepository,
            CarreraRepository carreraRepository, RedSocialRepository redSocialRepository,
            OfreceRepository ofreceRepository) {
        this.ciudadRepository = ciudadRepository;
        this.universidadRepository = universidadRepository;
        this.carreraRepository = carreraRepository;
        this.redSocialRepository = redSocialRepository;
        this.ofreceRepository = ofreceRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (universidadRepository.count() > 0) {
            return;
        }

        Ciudad bogota = ciudadRepository.save(new Ciudad("Bogota", "Cundinamarca"));

        List<Universidad> universidades = universidadRepository.saveAll(List.of(
                new Universidad("Universidad El Bosque", "UNBOSQUE", new Direccion(), bogota),
                new Universidad("Universidad Nacional de Colombia", "UNAL", new Direccion(), bogota),
                new Universidad("Pontificia Universidad Javeriana", "PUJ", new Direccion(), bogota),
                new Universidad("Universidad de los Andes", "UNIANDES", new Direccion(), bogota)));

        List<Carrera> carreras = carreraRepository.saveAll(List.of(
                new Carrera("Ingenieria de Sistemas"),
                new Carrera("Medicina"),
                new Carrera("Derecho"),
                new Carrera("Psicologia"),
                new Carrera("Administracion de Empresas")));

        redSocialRepository.saveAll(List.of(
                new RedSocial("Instagram", "https://www.instagram.com"),
                new RedSocial("Facebook", "https://www.facebook.com"),
                new RedSocial("X", "https://x.com"),
                new RedSocial("TikTok", "https://www.tiktok.com"),
                new RedSocial("LinkedIn", "https://www.linkedin.com")));

        // Ejemplo de oferta: cada universidad ofrece todas las carreras de la lista.
        for (Universidad universidad : universidades) {
            for (Carrera carrera : carreras) {
                ofreceRepository.save(new Ofrece(carrera, universidad, "Facultad de " + carrera.getNombre()));
            }
        }
        log.info("Datos de referencia iniciales cargados (1 ciudad, {} universidades, {} carreras)",
                universidades.size(), carreras.size());
    }
}
