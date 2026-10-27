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

import co.edu.unbosque.empareja2.dto.request.EstudioRequestDTO;
import co.edu.unbosque.empareja2.dto.request.RedPerfilRequestDTO;
import co.edu.unbosque.empareja2.entity.Carrera;
import co.edu.unbosque.empareja2.entity.Estudio;
import co.edu.unbosque.empareja2.entity.RedSocial;
import co.edu.unbosque.empareja2.entity.Tiene;
import co.edu.unbosque.empareja2.entity.Universidad;
import co.edu.unbosque.empareja2.entity.Usuario;
import co.edu.unbosque.empareja2.exception.ResourceNotFoundException;
import co.edu.unbosque.empareja2.repository.CarreraRepository;
import co.edu.unbosque.empareja2.repository.OfreceRepository;
import co.edu.unbosque.empareja2.repository.RedSocialRepository;
import co.edu.unbosque.empareja2.repository.UniversidadRepository;

/**
 * Arma las colecciones de un perfil (intereses, estudios y redes sociales) a
 * partir de lo enviado por el cliente, validando que las referencias existan.
 * Sincroniza "en sitio": actualiza lo existente, agrega lo nuevo y elimina lo
 * que ya no viene; UsuarioRepository.save luego reemplaza las filas en la base
 * de datos.
 */
@Component
public class PerfilAssembler {

    private final CarreraRepository carreraRepository;
    private final UniversidadRepository universidadRepository;
    private final OfreceRepository ofreceRepository;
    private final RedSocialRepository redSocialRepository;

    public PerfilAssembler(CarreraRepository carreraRepository, UniversidadRepository universidadRepository,
            OfreceRepository ofreceRepository, RedSocialRepository redSocialRepository) {
        this.carreraRepository = carreraRepository;
        this.universidadRepository = universidadRepository;
        this.ofreceRepository = ofreceRepository;
        this.redSocialRepository = redSocialRepository;
    }

    /** La fecha de nacimiento es opcional aqui; si viene, la edad debe ser al menos EDAD_MINIMA. */
    public void validarEdadMinima(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) {
            return;
        }
        Integer edad = Usuario.calcularEdad(fechaNacimiento, LocalDate.now());
        if (edad < Usuario.EDAD_MINIMA) {
            throw new IllegalArgumentException("Debes tener al menos " + Usuario.EDAD_MINIMA + " anos para registrarte");
        }
    }

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

    public void sincronizarEstudios(Usuario usuario, List<EstudioRequestDTO> solicitados) {
        List<EstudioRequestDTO> lista = solicitados == null ? List.of() : solicitados;

        Set<Long> carrerasSolicitadas = new HashSet<>();
        for (EstudioRequestDTO dto : lista) {
            if (!carrerasSolicitadas.add(dto.idCarrera())) {
                throw new IllegalArgumentException("La carrera con id " + dto.idCarrera() + " esta repetida");
            }
        }

        Map<Long, Estudio> existentes = usuario.getEstudios().stream()
                .collect(Collectors.toMap(e -> e.getCarrera().getIdCarrera(), Function.identity()));

        for (EstudioRequestDTO dto : lista) {
            Carrera carrera = carreraRepository.findById(dto.idCarrera()).orElseThrow(
                    () -> new ResourceNotFoundException("No existe la carrera con id " + dto.idCarrera()));
            Universidad universidad = universidadRepository.findById(dto.idUniversidad()).orElseThrow(
                    () -> new ResourceNotFoundException("No existe la universidad con id " + dto.idUniversidad()));
            if (!ofreceRepository.existsByCarreraAndUniversidad(carrera.getIdCarrera(), universidad.getIdUniversidad())) {
                throw new IllegalArgumentException("La universidad '" + universidad.getNombre()
                        + "' no ofrece la carrera '" + carrera.getNombre() + "'");
            }

            Estudio existente = existentes.get(carrera.getIdCarrera());
            if (existente != null) {
                existente.setUniversidad(universidad);
                existente.setSemestre(dto.semestre());
            } else {
                usuario.getEstudios().add(new Estudio(carrera, universidad, dto.semestre()));
            }
        }

        usuario.getEstudios().removeIf(e -> !carrerasSolicitadas.contains(e.getCarrera().getIdCarrera()));
    }

    public void sincronizarRedes(Usuario usuario, List<RedPerfilRequestDTO> solicitadas) {
        List<RedPerfilRequestDTO> lista = solicitadas == null ? List.of() : solicitadas;

        Set<Long> redesSolicitadas = new HashSet<>();
        for (RedPerfilRequestDTO dto : lista) {
            if (!redesSolicitadas.add(dto.idRedSocial())) {
                throw new IllegalArgumentException("La red social con id " + dto.idRedSocial() + " esta repetida");
            }
        }

        Map<Long, Tiene> existentes = usuario.getRedes().stream()
                .collect(Collectors.toMap(t -> t.getRedSocial().getIdRedSocial(), Function.identity()));

        for (RedPerfilRequestDTO dto : lista) {
            RedSocial red = redSocialRepository.findById(dto.idRedSocial()).orElseThrow(
                    () -> new ResourceNotFoundException("No existe la red social con id " + dto.idRedSocial()));

            Tiene existente = existentes.get(red.getIdRedSocial());
            if (existente != null) {
                existente.setUrlPerfil(dto.urlPerfil());
                existente.setAlias(dto.alias());
            } else {
                usuario.getRedes().add(new Tiene(red, dto.urlPerfil(), dto.alias()));
            }
        }

        usuario.getRedes().removeIf(t -> !redesSolicitadas.contains(t.getRedSocial().getIdRedSocial()));
    }
}
