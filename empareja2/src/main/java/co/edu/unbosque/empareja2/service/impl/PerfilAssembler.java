package co.edu.unbosque.empareja2.service.impl;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
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
import co.edu.unbosque.empareja2.entity.Tiene;
import co.edu.unbosque.empareja2.entity.Universidad;
import co.edu.unbosque.empareja2.entity.Usuario;
import co.edu.unbosque.empareja2.exception.LanzadorException;
import co.edu.unbosque.empareja2.repository.CarreraRepository;
import co.edu.unbosque.empareja2.repository.OfreceRepository;
import co.edu.unbosque.empareja2.repository.RedSocialRepository;
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

    /** Acceso a los datos de las redes sociales. */
    private final RedSocialRepository redSocialRepository;

    /**
     * Crea el ensamblador.
     *
     * @param carreraRepository     el repositorio de carreras
     * @param universidadRepository el repositorio de universidades
     * @param ofreceRepository      el repositorio de la oferta academica
     * @param redSocialRepository   el repositorio de redes sociales
     */
    public PerfilAssembler(CarreraRepository carreraRepository, UniversidadRepository universidadRepository,
            OfreceRepository ofreceRepository, RedSocialRepository redSocialRepository) {
        this.carreraRepository = carreraRepository;
        this.universidadRepository = universidadRepository;
        this.ofreceRepository = ofreceRepository;
        this.redSocialRepository = redSocialRepository;
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
     * Sincroniza las redes sociales del usuario con las enviadas, validando que
     * cada red exista y no este repetida.
     *
     * @param usuario     el usuario a modificar
     * @param solicitadas las redes enviadas (puede ser null)
     */
    public void sincronizarRedes(Usuario usuario, List<RedPerfilRequestDTO> solicitadas) {
        List<RedPerfilRequestDTO> lista = solicitadas == null ? List.of() : solicitadas;

        Set<Long> redesSolicitadas = new HashSet<>();
        for (RedPerfilRequestDTO dto : lista) {
            if (!redesSolicitadas.add(dto.getIdRedSocial())) {
                LanzadorException.redSocialRepetida(dto.getIdRedSocial());
            }
        }

        Map<Long, Tiene> existentes = usuario.getRedes().stream()
                .collect(Collectors.toMap(t -> t.getRedSocial().getIdRedSocial(), Function.identity()));

        for (RedPerfilRequestDTO dto : lista) {
            RedSocial red = redSocialRepository.buscarPorId(dto.getIdRedSocial()).orElse(null);
            if (red == null) {
                LanzadorException.redSocialNoEncontrada(dto.getIdRedSocial());
            }

            Tiene existente = existentes.get(red.getIdRedSocial());
            if (existente != null) {
                existente.setNombreUsuario(dto.getNombreUsuario().trim());
            } else {
                usuario.getRedes().add(new Tiene(red, dto.getNombreUsuario().trim()));
            }
        }

        usuario.getRedes().removeIf(t -> !redesSolicitadas.contains(t.getRedSocial().getIdRedSocial()));
    }
}
