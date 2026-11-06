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
import co.edu.unbosque.empareja2.entity.Universidad;
import co.edu.unbosque.empareja2.repository.CarreraRepository;
import co.edu.unbosque.empareja2.repository.CiudadRepository;
import co.edu.unbosque.empareja2.repository.OfreceRepository;
import co.edu.unbosque.empareja2.repository.UniversidadRepository;

/**
 * Carga datos de referencia basicos la primera vez que arranca la aplicacion
 * (solo si no hay ninguna universidad), para que el registro y el filtro por
 * universidad se puedan probar de inmediato. Reemplaza estos datos por los reales.
 */
@Component
public class CatalogoSeeder implements CommandLineRunner {

    /** Registro de eventos de la clase. */
    private static final Logger log = LoggerFactory.getLogger(CatalogoSeeder.class);

    /** Acceso a los datos de las ciudades. */
    private final CiudadRepository ciudadRepository;

    /** Acceso a los datos de las universidades. */
    private final UniversidadRepository universidadRepository;

    /** Acceso a los datos de las carreras. */
    private final CarreraRepository carreraRepository;

    /** Acceso a los datos de la oferta academica. */
    private final OfreceRepository ofreceRepository;

    /**
     * Crea el sembrador.
     *
     * @param ciudadRepository      el repositorio de ciudades
     * @param universidadRepository el repositorio de universidades
     * @param carreraRepository     el repositorio de carreras
     * @param ofreceRepository      el repositorio de la oferta academica
     */
    public CatalogoSeeder(CiudadRepository ciudadRepository, UniversidadRepository universidadRepository,
            CarreraRepository carreraRepository, OfreceRepository ofreceRepository) {
        this.ciudadRepository = ciudadRepository;
        this.universidadRepository = universidadRepository;
        this.carreraRepository = carreraRepository;
        this.ofreceRepository = ofreceRepository;
    }

    /**
     * Carga los datos de referencia iniciales si no hay universidades.
     *
     * @param args los argumentos de arranque (no se usan)
     */
    @Override
    @Transactional
    public void run(String... args) {
        if (universidadRepository.contar() > 0) {
            return;
        }

        Ciudad bogota = ciudadRepository.guardar(new Ciudad("Bogota", "Cundinamarca"));

        List<Universidad> universidades = universidadRepository.guardarTodas(List.of(
                new Universidad("Universidad El Bosque", "UNBOSQUE",
                        new Direccion("Avenida Carrera 9 # 131A-02", "Usaquen"), bogota),
                new Universidad("Universidad Nacional de Colombia", "UNAL",
                        new Direccion("Carrera 30 # 45-03", "Teusaquillo"), bogota),
                new Universidad("Pontificia Universidad Javeriana", "PUJ",
                        new Direccion("Carrera 7 # 40-62", "Chapinero"), bogota),
                new Universidad("Universidad de los Andes", "UNIANDES",
                        new Direccion("Carrera 1 # 18A-12", "La Candelaria"), bogota)));

        List<Carrera> carreras = carreraRepository.guardarTodas(List.of(
                new Carrera("Ingenieria de Sistemas", "Facultad de Ingenieria de Sistemas"),
                new Carrera("Medicina", "Facultad de Medicina"),
                new Carrera("Derecho", "Facultad de Derecho"),
                new Carrera("Psicologia", "Facultad de Psicologia"),
                new Carrera("Administracion de Empresas", "Facultad de Administracion de Empresas")));

        for (Universidad universidad : universidades) {
            for (Carrera carrera : carreras) {
                ofreceRepository.guardar(new Ofrece(carrera, universidad, carrera.getFacultad()));
            }
        }
        log.info("Datos de referencia iniciales cargados (1 ciudad, {} universidades, {} carreras)",
                universidades.size(), carreras.size());
    }
}
