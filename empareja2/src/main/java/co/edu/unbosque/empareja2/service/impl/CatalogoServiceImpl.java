package co.edu.unbosque.empareja2.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.empareja2.dto.request.CarreraRequestDTO;
import co.edu.unbosque.empareja2.dto.request.CiudadRequestDTO;
import co.edu.unbosque.empareja2.dto.request.OfreceRequestDTO;
import co.edu.unbosque.empareja2.dto.request.RedSocialRequestDTO;
import co.edu.unbosque.empareja2.dto.request.UniversidadRequestDTO;
import co.edu.unbosque.empareja2.dto.response.CarreraDTO;
import co.edu.unbosque.empareja2.dto.response.CiudadDTO;
import co.edu.unbosque.empareja2.dto.response.OfreceDTO;
import co.edu.unbosque.empareja2.dto.response.RedSocialDTO;
import co.edu.unbosque.empareja2.dto.response.UniversidadDTO;
import co.edu.unbosque.empareja2.entity.Carrera;
import co.edu.unbosque.empareja2.entity.Ciudad;
import co.edu.unbosque.empareja2.entity.Direccion;
import co.edu.unbosque.empareja2.entity.Ofrece;
import co.edu.unbosque.empareja2.entity.RedSocial;
import co.edu.unbosque.empareja2.entity.Universidad;
import co.edu.unbosque.empareja2.exception.DuplicateResourceException;
import co.edu.unbosque.empareja2.exception.ResourceNotFoundException;
import co.edu.unbosque.empareja2.mapper.CatalogoMapper;
import co.edu.unbosque.empareja2.repository.CarreraRepository;
import co.edu.unbosque.empareja2.repository.CiudadRepository;
import co.edu.unbosque.empareja2.repository.OfreceRepository;
import co.edu.unbosque.empareja2.repository.RedSocialRepository;
import co.edu.unbosque.empareja2.repository.UniversidadRepository;
import co.edu.unbosque.empareja2.service.CatalogoService;

/**
 * Datos de referencia del sistema (ciudades, universidades, carreras, redes
 * sociales y oferta academica).
 */
@Service
@Transactional(readOnly = true)
public class CatalogoServiceImpl implements CatalogoService {

    private final CiudadRepository ciudadRepository;
    private final UniversidadRepository universidadRepository;
    private final CarreraRepository carreraRepository;
    private final RedSocialRepository redSocialRepository;
    private final OfreceRepository ofreceRepository;
    private final CatalogoMapper mapper;

    public CatalogoServiceImpl(CiudadRepository ciudadRepository, UniversidadRepository universidadRepository,
            CarreraRepository carreraRepository, RedSocialRepository redSocialRepository,
            OfreceRepository ofreceRepository, CatalogoMapper mapper) {
        this.ciudadRepository = ciudadRepository;
        this.universidadRepository = universidadRepository;
        this.carreraRepository = carreraRepository;
        this.redSocialRepository = redSocialRepository;
        this.ofreceRepository = ofreceRepository;
        this.mapper = mapper;
    }

    @Override
    public List<CiudadDTO> listarCiudades() {
        return ciudadRepository.findAllByOrderByNombreAsc().stream().map(mapper::toDTO).toList();
    }

    @Override
    public List<UniversidadDTO> listarUniversidades(String texto) {
        List<Universidad> universidades = (texto == null || texto.isBlank()) ? universidadRepository.listarTodas()
                : universidadRepository.buscar(texto.trim());
        return universidades.stream().map(mapper::toDTO).toList();
    }

    @Override
    public List<CarreraDTO> listarCarreras() {
        return carreraRepository.findAllByOrderByNombreAsc().stream().map(mapper::toDTO).toList();
    }

    @Override
    public List<RedSocialDTO> listarRedesSociales() {
        return redSocialRepository.findAllByOrderByNombreRedSocialAsc().stream().map(mapper::toDTO).toList();
    }

    @Override
    public List<OfreceDTO> listarOferta(Long idUniversidad) {
        List<Ofrece> oferta = idUniversidad == null ? ofreceRepository.listarTodas()
                : ofreceRepository.listarPorUniversidad(idUniversidad);
        return oferta.stream().map(mapper::toDTO).toList();
    }

    @Override
    @Transactional
    public CiudadDTO crearCiudad(CiudadRequestDTO request) {
        if (ciudadRepository.existsByNombreIgnoreCaseAndDepartamentoIgnoreCase(request.nombre(),
                request.departamento())) {
            throw new DuplicateResourceException("Ya existe esa ciudad en ese departamento");
        }
        return mapper.toDTO(ciudadRepository.save(new Ciudad(request.nombre().trim(), request.departamento().trim())));
    }

    @Override
    @Transactional
    public UniversidadDTO crearUniversidad(UniversidadRequestDTO request) {
        if (universidadRepository.existsByNombreIgnoreCase(request.nombre())) {
            throw new DuplicateResourceException("Ya existe una universidad con ese nombre");
        }
        if (universidadRepository.existsBySiglaIgnoreCase(request.sigla())) {
            throw new DuplicateResourceException("Ya existe una universidad con esa sigla");
        }
        Ciudad ciudad = ciudadRepository.findById(request.idCiudad())
                .orElseThrow(() -> new ResourceNotFoundException("No existe la ciudad con id " + request.idCiudad()));

        Direccion direccion = new Direccion(request.calle(), request.barrio(), request.numeroDireccion());
        Universidad universidad = new Universidad(request.nombre().trim(), request.sigla().trim(), direccion, ciudad);
        return mapper.toDTO(universidadRepository.save(universidad));
    }

    @Override
    @Transactional
    public CarreraDTO crearCarrera(CarreraRequestDTO request) {
        if (carreraRepository.existsByNombreIgnoreCase(request.nombre())) {
            throw new DuplicateResourceException("Ya existe una carrera con ese nombre");
        }
        return mapper.toDTO(carreraRepository.save(new Carrera(request.nombre().trim())));
    }

    @Override
    @Transactional
    public RedSocialDTO crearRedSocial(RedSocialRequestDTO request) {
        if (redSocialRepository.existsByNombreRedSocialIgnoreCase(request.nombreRedSocial())) {
            throw new DuplicateResourceException("Ya existe una red social con ese nombre");
        }
        return mapper.toDTO(
                redSocialRepository.save(new RedSocial(request.nombreRedSocial().trim(), request.url().trim())));
    }

    @Override
    @Transactional
    public OfreceDTO crearOferta(OfreceRequestDTO request) {
        Carrera carrera = carreraRepository.findById(request.idCarrera())
                .orElseThrow(() -> new ResourceNotFoundException("No existe la carrera con id " + request.idCarrera()));
        Universidad universidad = universidadRepository.findById(request.idUniversidad()).orElseThrow(
                () -> new ResourceNotFoundException("No existe la universidad con id " + request.idUniversidad()));

        if (ofreceRepository.existsByCarreraAndUniversidad(carrera.getIdCarrera(), universidad.getIdUniversidad())) {
            throw new DuplicateResourceException("Esa universidad ya ofrece esa carrera");
        }
        return mapper.toDTO(ofreceRepository.save(new Ofrece(carrera, universidad, request.facultad().trim())));
    }
}
