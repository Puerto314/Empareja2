package co.edu.unbosque.empareja2.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import co.edu.unbosque.empareja2.dto.EstudioRequestDTO;
import co.edu.unbosque.empareja2.dto.RedPerfilRequestDTO;
import co.edu.unbosque.empareja2.entity.Carrera;
import co.edu.unbosque.empareja2.entity.Estudio;
import co.edu.unbosque.empareja2.entity.RedSocial;
import co.edu.unbosque.empareja2.entity.Universidad;
import co.edu.unbosque.empareja2.entity.Usuario;
import co.edu.unbosque.empareja2.exception.LanzadorException;
import co.edu.unbosque.empareja2.repository.CarreraRepository;
import co.edu.unbosque.empareja2.repository.OfreceRepository;
import co.edu.unbosque.empareja2.repository.UniversidadRepository;

/**
 * Arma las colecciones de un perfil (intereses, estudios y redes sociales) a
 * partir de lo enviado por el cliente, validando que las referencias existan.
 * Sincroniza "en sitio": actualiza lo existente, agrega lo nuevo y elimina lo
 * que ya no viene; UsuarioRepository.guardar luego reemplaza las filas en la base
 * de datos.
 */
@Component
public class PerfilAssembler {

    /** Expresion regular del formato de un correo. */
    private static final String FORMATO_CORREO = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$";

    /** Acceso a los datos de las carreras. */
    private final CarreraRepository carreraRepository;

    /** Acceso a los datos de las universidades. */
    private final UniversidadRepository universidadRepository;

    /** Acceso a los datos de la oferta academica. */
    private final OfreceRepository ofreceRepository;

    /**
     * Crea el ensamblador.
     *
     * @param carreraRepository     el repositorio de carreras
     * @param universidadRepository el repositorio de universidades
     * @param ofreceRepository      el repositorio de la oferta academica
     */
    public PerfilAssembler(CarreraRepository carreraRepository, UniversidadRepository universidadRepository,
            OfreceRepository ofreceRepository) {
        this.carreraRepository = carreraRepository;
        this.universidadRepository = universidadRepository;
        this.ofreceRepository = ofreceRepository;
    }

    /**
     * La fecha de nacimiento es opcional aqui; si viene, la edad debe ser al
     * menos EDAD_MINIMA.
     *
     * @param fechaNacimiento la fecha de nacimiento (puede ser null)
     */
    public void validarEdadMinima(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) {
            return;
        }
        Integer edad = Usuario.calcularEdad(fechaNacimiento, LocalDate.now());
        if (edad < Usuario.EDAD_MINIMA) {
            LanzadorException.edadMinimaNoCumplida();
        }
    }

    /**
     * Un ESTUDIANTE aparece en el catalogo, asi que (como en el registro propio)
     * debe tener fecha de nacimiento valida y exactamente una carrera con su
     * universidad.
     *
     * @param fechaNacimiento la fecha de nacimiento
     * @param estudios        los estudios enviados
     */
    public void exigirDatosDeEstudiante(LocalDate fechaNacimiento, List<EstudioRequestDTO> estudios) {
        if (fechaNacimiento == null) {
            LanzadorException.fechaNacimientoObligatoriaParaEstudiante();
        }
        if (estudios == null || estudios.size() != 1) {
            LanzadorException.debeIndicarUnaCarrera();
        }
    }

    /**
     * El nombre de usuario de un estudiante es su correo, asi que debe tener
     * formato de correo.
     *
     * @param correo el correo a validar
     */
    public void validarCorreo(String correo) {
        if (correo == null || !correo.matches(FORMATO_CORREO)) {
            LanzadorException.correoFormatoInvalido();
        }
    }

    /**
     * Reemplaza los intereses del usuario por los enviados, sin vacios ni
     * espacios sobrantes.
     *
     * @param usuario   el usuario a modificar
     * @param intereses los intereses enviados (puede ser null)
     */
    public void aplicarIntereses(Usuario usuario, Collection<String> intereses) {
        Set<String> limpios = new LinkedHashSet<>();
        if (intereses != null) {
            for (String interes : intereses) {
                if (interes != null && !interes.isBlank()) {
                    limpios.add(interes.trim());
                }
            }
        }
        usuario.getIntereses().retainAll(limpios);
        usuario.getIntereses().addAll(limpios);
    }

    /**
     * Sincroniza el estudio del usuario con el enviado, validando que la carrera
     * y la universidad existan y que la universidad ofrezca la carrera.
     *
     * @param usuario    el usuario a modificar
     * @param solicitados los estudios enviados (a lo sumo uno)
     */
    public void sincronizarEstudios(Usuario usuario, List<EstudioRequestDTO> solicitados) {
        List<EstudioRequestDTO> lista = solicitados == null ? List.of() : solicitados;
        if (lista.size() > 1) {
            LanzadorException.soloUnaCarrera();
        }

        Set<Long> carrerasSolicitadas = new HashSet<>();
        for (EstudioRequestDTO dto : lista) {
            if (!carrerasSolicitadas.add(dto.getIdCarrera())) {
                LanzadorException.carreraRepetida(dto.getIdCarrera());
            }
        }

        Map<Long, Estudio> existentes = usuario.getEstudios().stream()
                .collect(Collectors.toMap(e -> e.getCarrera().getIdCarrera(), Function.identity()));

        for (EstudioRequestDTO dto : lista) {
            Carrera carrera = carreraRepository.buscarPorId(dto.getIdCarrera()).orElse(null);
            if (carrera == null) {
                LanzadorException.carreraNoEncontrada(dto.getIdCarrera());
            }
            Universidad universidad = universidadRepository.buscarPorId(dto.getIdUniversidad()).orElse(null);
            if (universidad == null) {
                LanzadorException.universidadNoEncontrada(dto.getIdUniversidad());
            }
            if (!ofreceRepository.existePorCarreraYUniversidad(carrera.getIdCarrera(),
                    universidad.getIdUniversidad())) {
                LanzadorException.universidadNoOfreceCarrera(universidad.getNombre(), carrera.getNombre());
            }

            Estudio existente = existentes.get(carrera.getIdCarrera());
            if (existente != null) {
                existente.setUniversidad(universidad);
                existente.setSemestre(dto.getSemestre());
            } else {
                usuario.getEstudios().add(new Estudio(carrera, universidad, dto.getSemestre()));
            }
        }

        usuario.getEstudios().removeIf(e -> !carrerasSolicitadas.contains(e.getCarrera().getIdCarrera()));
    }

    /**
     * Reemplaza las redes sociales del usuario por las enviadas, validando que no
     * haya dos del mismo tipo (sin distinguir mayusculas).
     *
     * @param usuario     el usuario a modificar
     * @param solicitadas las redes enviadas (puede ser null)
     */
    public void sincronizarRedes(Usuario usuario, List<RedPerfilRequestDTO> solicitadas) {
        List<RedPerfilRequestDTO> lista = solicitadas == null ? List.of() : solicitadas;

        Set<String> tiposVistos = new HashSet<>();
        List<RedSocial> nuevas = new ArrayList<>();
        for (RedPerfilRequestDTO dto : lista) {
            String tipo = dto.getTipoRedSocial().trim();
            if (!tiposVistos.add(tipo.toLowerCase(Locale.ROOT))) {
                LanzadorException.redSocialRepetida(tipo);
            }
            nuevas.add(new RedSocial(tipo, dto.getUrlPerfil().trim()));
        }

        usuario.getRedes().clear();
        usuario.getRedes().addAll(nuevas);
    }
}
