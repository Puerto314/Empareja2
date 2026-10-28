package co.edu.unbosque.empareja2.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.empareja2.dto.request.ActualizarOfertaRequestDTO;
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
import co.edu.unbosque.empareja2.exception.OperationNotAllowedException;
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

    // ------------------------------------------------------------------
    // Consulta por id
    // ------------------------------------------------------------------

    @Override
    public CiudadDTO obtenerCiudad(Long id) {
        return mapper.toDTO(buscarCiudad(id));
    }

    @Override
    public UniversidadDTO obtenerUniversidad(Long id) {
        return mapper.toDTO(buscarUniversidad(id));
    }

    @Override
    public CarreraDTO obtenerCarrera(Long id) {
        return mapper.toDTO(buscarCarrera(id));
    }

    @Override
    public RedSocialDTO obtenerRedSocial(Long id) {
        return mapper.toDTO(buscarRedSocial(id));
    }

    // ------------------------------------------------------------------
    // Edicion
    // ------------------------------------------------------------------

    @Override
    @Transactional
    public CiudadDTO actualizarCiudad(Long id, CiudadRequestDTO request) {
        Ciudad ciudad = buscarCiudad(id);
        String nombre = request.nombre().trim();
        String departamento = request.departamento().trim();
        if (ciudadRepository.existsByNombreAndDepartamentoAndIdNot(nombre, departamento, id)) {
            throw new DuplicateResourceException("Ya existe esa ciudad en ese departamento");
        }
        ciudad.setNombre(nombre);
        ciudad.setDepartamento(departamento);
        ciudadRepository.update(ciudad);
        return mapper.toDTO(ciudad);
    }

    @Override
    @Transactional
    public UniversidadDTO actualizarUniversidad(Long id, UniversidadRequestDTO request) {
        Universidad universidad = buscarUniversidad(id);
        String nombre = request.nombre().trim();
        String sigla = request.sigla().trim();
        if (universidadRepository.existsByNombreAndIdNot(nombre, id)) {
            throw new DuplicateResourceException("Ya existe una universidad con ese nombre");
        }
        if (universidadRepository.existsBySiglaAndIdNot(sigla, id)) {
            throw new DuplicateResourceException("Ya existe una universidad con esa sigla");
        }
        Ciudad ciudad = buscarCiudad(request.idCiudad());

        universidad.setNombre(nombre);
        universidad.setSigla(sigla);
        universidad.setDireccion(new Direccion(request.calle(), request.barrio(), request.numeroDireccion()));
        universidad.setCiudad(ciudad);
        universidadRepository.update(universidad);
        return mapper.toDTO(universidad);
    }

    @Override
    @Transactional
    public CarreraDTO actualizarCarrera(Long id, CarreraRequestDTO request) {
        Carrera carrera = buscarCarrera(id);
        String nombre = request.nombre().trim();
        if (carreraRepository.existsByNombreAndIdNot(nombre, id)) {
            throw new DuplicateResourceException("Ya existe una carrera con ese nombre");
        }
        carrera.setNombre(nombre);
        carreraRepository.update(carrera);
        return mapper.toDTO(carrera);
    }

    @Override
    @Transactional
    public RedSocialDTO actualizarRedSocial(Long id, RedSocialRequestDTO request) {
        RedSocial red = buscarRedSocial(id);
        String nombre = request.nombreRedSocial().trim();
        if (redSocialRepository.existsByNombreRedSocialAndIdNot(nombre, id)) {
            throw new DuplicateResourceException("Ya existe una red social con ese nombre");
        }
        red.setNombreRedSocial(nombre);
        red.setUrl(request.url().trim());
        redSocialRepository.update(red);
        return mapper.toDTO(red);
    }

    @Override
    @Transactional
    public OfreceDTO actualizarOferta(Long idCarrera, Long idUniversidad, ActualizarOfertaRequestDTO request) {
        Ofrece oferta = buscarOferta(idCarrera, idUniversidad);
        oferta.setFacultad(request.facultad().trim());
        ofreceRepository.update(oferta);
        return mapper.toDTO(oferta);
    }

    // ------------------------------------------------------------------
    // Eliminacion
    // ------------------------------------------------------------------

    @Override
    @Transactional
    public void eliminarCiudad(Long id) {
        buscarCiudad(id);
        if (universidadRepository.contarPorCiudad(id) > 0) {
            throw new OperationNotAllowedException(
                    "No se puede eliminar la ciudad: todavia tiene universidades registradas");
        }
        ciudadRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void eliminarUniversidad(Long id) {
        buscarUniversidad(id);
        if (universidadRepository.contarEstudiantes(id) > 0) {
            throw new OperationNotAllowedException(
                    "No se puede eliminar la universidad: hay usuarios que estudian en ella");
        }
        ofreceRepository.eliminarPorUniversidad(id);
        universidadRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void eliminarCarrera(Long id) {
        buscarCarrera(id);
        if (carreraRepository.contarEstudiantes(id) > 0) {
            throw new OperationNotAllowedException("No se puede eliminar la carrera: hay usuarios que la estudian");
        }
        ofreceRepository.eliminarPorCarrera(id);
        carreraRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void eliminarRedSocial(Long id) {
        buscarRedSocial(id);
        if (redSocialRepository.contarUsos(id) > 0) {
            throw new OperationNotAllowedException(
                    "No se puede eliminar la red social: hay usuarios que la tienen en su perfil");
        }
        redSocialRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void eliminarOferta(Long idCarrera, Long idUniversidad) {
        buscarOferta(idCarrera, idUniversidad);
        if (ofreceRepository.contarEstudiantes(idCarrera, idUniversidad) > 0) {
            throw new OperationNotAllowedException(
                    "No se puede eliminar la oferta: hay usuarios que cursan esa carrera en esa universidad");
        }
        ofreceRepository.delete(idCarrera, idUniversidad);
    }

    // ------------------------------------------------------------------
    // Busquedas con 404
    // ------------------------------------------------------------------

    private Ciudad buscarCiudad(Long id) {
        return ciudadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe la ciudad con id " + id));
    }

    private Universidad buscarUniversidad(Long id) {
        return universidadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe la universidad con id " + id));
    }

    private Carrera buscarCarrera(Long id) {
        return carreraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe la carrera con id " + id));
    }

    private RedSocial buscarRedSocial(Long id) {
        return redSocialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe la red social con id " + id));
    }

    private Ofrece buscarOferta(Long idCarrera, Long idUniversidad) {
        return ofreceRepository.findByIds(idCarrera, idUniversidad).orElseThrow(() -> new ResourceNotFoundException(
                "La universidad " + idUniversidad + " no ofrece la carrera " + idCarrera));
    }
}
