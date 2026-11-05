package co.edu.unbosque.empareja2.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.empareja2.dto.ActualizarOfertaRequestDTO;
import co.edu.unbosque.empareja2.dto.CarreraDTO;
import co.edu.unbosque.empareja2.dto.CarreraRequestDTO;
import co.edu.unbosque.empareja2.dto.CiudadDTO;
import co.edu.unbosque.empareja2.dto.CiudadRequestDTO;
import co.edu.unbosque.empareja2.dto.OfreceDTO;
import co.edu.unbosque.empareja2.dto.OfreceRequestDTO;
import co.edu.unbosque.empareja2.dto.RedSocialDTO;
import co.edu.unbosque.empareja2.dto.UniversidadDTO;
import co.edu.unbosque.empareja2.dto.UniversidadRequestDTO;
import co.edu.unbosque.empareja2.entity.Carrera;
import co.edu.unbosque.empareja2.entity.Ciudad;
import co.edu.unbosque.empareja2.entity.Direccion;
import co.edu.unbosque.empareja2.entity.Ofrece;
import co.edu.unbosque.empareja2.entity.RedSocial;
import co.edu.unbosque.empareja2.entity.Universidad;
import co.edu.unbosque.empareja2.exception.LanzadorException;
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

    /** Acceso a los datos de las ciudades. */
    private final CiudadRepository ciudadRepository;

    /** Acceso a los datos de las universidades. */
    private final UniversidadRepository universidadRepository;

    /** Acceso a los datos de las carreras. */
    private final CarreraRepository carreraRepository;

    /** Acceso a los datos de las redes sociales. */
    private final RedSocialRepository redSocialRepository;

    /** Acceso a los datos de la oferta academica. */
    private final OfreceRepository ofreceRepository;

    /** Convierte los datos de referencia en DTOs. */
    private final CatalogoMapper mapper;

    /**
     * Crea el servicio.
     *
     * @param ciudadRepository      el repositorio de ciudades
     * @param universidadRepository el repositorio de universidades
     * @param carreraRepository     el repositorio de carreras
     * @param redSocialRepository   el repositorio de redes sociales
     * @param ofreceRepository      el repositorio de la oferta academica
     * @param mapper                el convertidor de datos de referencia
     */
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

    // ------------------------------------------------------------------
    // Listados
    // ------------------------------------------------------------------

    /**
     * Lista las ciudades ordenadas por nombre.
     *
     * @return las ciudades
     */
    @Override
    public List<CiudadDTO> listarCiudades() {
        return ciudadRepository.listarTodas().stream().map(mapper::toDTO).toList();
    }

    /**
     * Lista las universidades; si hay texto, busca por nombre o sigla.
     *
     * @param texto el texto a buscar (puede ser null o vacio)
     * @return las universidades
     */
    @Override
    public List<UniversidadDTO> listarUniversidades(String texto) {
        List<Universidad> universidades;
        if (texto == null || texto.isBlank()) {
            universidades = universidadRepository.listarTodas();
        } else {
            universidades = universidadRepository.listarPorTexto(texto.trim());
        }
        return universidades.stream().map(mapper::toDTO).toList();
    }

    /**
     * Lista las carreras ordenadas por nombre.
     *
     * @return las carreras
     */
    @Override
    public List<CarreraDTO> listarCarreras() {
        return carreraRepository.listarTodas().stream().map(mapper::toDTO).toList();
    }

    /**
     * Lista las redes sociales ordenadas por nombre.
     *
     * @return las redes sociales
     */
    @Override
    public List<RedSocialDTO> listarRedesSociales() {
        return redSocialRepository.listarTodas().stream().map(mapper::toDTO).toList();
    }

    /**
     * Lista la oferta academica, de todas las universidades o de una sola.
     *
     * @param idUniversidad la universidad a filtrar (null para todas)
     * @return las ofertas
     */
    @Override
    public List<OfreceDTO> listarOferta(Long idUniversidad) {
        List<Ofrece> oferta;
        if (idUniversidad == null) {
            oferta = ofreceRepository.listarTodas();
        } else {
            oferta = ofreceRepository.listarPorUniversidad(idUniversidad);
        }
        return oferta.stream().map(mapper::toDTO).toList();
    }

    // ------------------------------------------------------------------
    // Creacion
    // ------------------------------------------------------------------

    /**
     * Crea una ciudad (no se admite repetirla en el mismo departamento).
     *
     * @param request los datos de la ciudad
     * @return la ciudad creada
     */
    @Override
    @Transactional
    public CiudadDTO crearCiudad(CiudadRequestDTO request) {
        if (ciudadRepository.existePorNombreYDepartamento(request.getNombre(), request.getDepartamento())) {
            LanzadorException.ciudadYaExiste();
        }
        Ciudad ciudad = new Ciudad(request.getNombre().trim(), request.getDepartamento().trim());
        return mapper.toDTO(ciudadRepository.guardar(ciudad));
    }

    /**
     * Crea una universidad (nombre y sigla unicos, en una ciudad que exista).
     *
     * @param request los datos de la universidad
     * @return la universidad creada
     */
    @Override
    @Transactional
    public UniversidadDTO crearUniversidad(UniversidadRequestDTO request) {
        if (universidadRepository.existePorNombre(request.getNombre())) {
            LanzadorException.universidadNombreYaExiste();
        }
        if (universidadRepository.existePorSigla(request.getSigla())) {
            LanzadorException.universidadSiglaYaExiste();
        }
        Ciudad ciudad = buscarCiudad(request.getIdCiudad());

        Direccion direccion = new Direccion(request.getCalle().trim(), request.getBarrio().trim());
        Universidad universidad = new Universidad(request.getNombre().trim(), request.getSigla().trim(), direccion,
                ciudad);
        return mapper.toDTO(universidadRepository.guardar(universidad));
    }

    /**
     * Crea una carrera (nombre unico).
     *
     * @param request los datos de la carrera
     * @return la carrera creada
     */
    @Override
    @Transactional
    public CarreraDTO crearCarrera(CarreraRequestDTO request) {
        if (carreraRepository.existePorNombre(request.getNombre())) {
            LanzadorException.carreraYaExiste();
        }
        Carrera carrera = new Carrera(request.getNombre().trim(), request.getFacultad().trim());
        return mapper.toDTO(carreraRepository.guardar(carrera));
    }

    /**
     * Registra que una universidad ofrece una carrera (la pareja debe ser nueva).
     *
     * @param request la carrera y la universidad
     * @return la oferta creada
     */
    @Override
    @Transactional
    public OfreceDTO crearOferta(OfreceRequestDTO request) {
        Carrera carrera = buscarCarrera(request.getIdCarrera());
        Universidad universidad = buscarUniversidad(request.getIdUniversidad());

        if (ofreceRepository.existePorCarreraYUniversidad(carrera.getIdCarrera(), universidad.getIdUniversidad())) {
            LanzadorException.ofertaYaExiste();
        }
        return mapper.toDTO(ofreceRepository.guardar(new Ofrece(carrera, universidad, carrera.getFacultad())));
    }

    // ------------------------------------------------------------------
    // Consulta por id
    // ------------------------------------------------------------------

    /**
     * Obtiene una ciudad por id.
     *
     * @param id el identificador de la ciudad
     * @return la ciudad
     */
    @Override
    public CiudadDTO obtenerCiudad(Long id) {
        return mapper.toDTO(buscarCiudad(id));
    }

    /**
     * Obtiene una universidad por id.
     *
     * @param id el identificador de la universidad
     * @return la universidad
     */
    @Override
    public UniversidadDTO obtenerUniversidad(Long id) {
        return mapper.toDTO(buscarUniversidad(id));
    }

    /**
     * Obtiene una carrera por id.
     *
     * @param id el identificador de la carrera
     * @return la carrera
     */
    @Override
    public CarreraDTO obtenerCarrera(Long id) {
        return mapper.toDTO(buscarCarrera(id));
    }

    /**
     * Obtiene una red social por id.
     *
     * @param id el identificador de la red social
     * @return la red social
     */
    @Override
    public RedSocialDTO obtenerRedSocial(Long id) {
        return mapper.toDTO(buscarRedSocial(id));
    }

    // ------------------------------------------------------------------
    // Edicion
    // ------------------------------------------------------------------

    /**
     * Edita una ciudad (no se admite repetirla en el mismo departamento).
     *
     * @param id      el identificador de la ciudad
     * @param request los datos nuevos
     * @return la ciudad editada
     */
    @Override
    @Transactional
    public CiudadDTO actualizarCiudad(Long id, CiudadRequestDTO request) {
        Ciudad ciudad = buscarCiudad(id);
        String nombre = request.getNombre().trim();
        String departamento = request.getDepartamento().trim();
        if (ciudadRepository.existePorNombreYDepartamentoExceptoId(nombre, departamento, id)) {
            LanzadorException.ciudadYaExiste();
        }
        ciudad.setNombre(nombre);
        ciudad.setDepartamento(departamento);
        ciudadRepository.actualizar(ciudad);
        return mapper.toDTO(ciudad);
    }

    /**
     * Edita una universidad (nombre y sigla unicos, en una ciudad que exista).
     *
     * @param id      el identificador de la universidad
     * @param request los datos nuevos
     * @return la universidad editada
     */
    @Override
    @Transactional
    public UniversidadDTO actualizarUniversidad(Long id, UniversidadRequestDTO request) {
        Universidad universidad = buscarUniversidad(id);
        String nombre = request.getNombre().trim();
        String sigla = request.getSigla().trim();
        if (universidadRepository.existePorNombreExceptoId(nombre, id)) {
            LanzadorException.universidadNombreYaExiste();
        }
        if (universidadRepository.existePorSiglaExceptoId(sigla, id)) {
            LanzadorException.universidadSiglaYaExiste();
        }
        Ciudad ciudad = buscarCiudad(request.getIdCiudad());

        universidad.setNombre(nombre);
        universidad.setSigla(sigla);
        universidad.setDireccion(new Direccion(request.getCalle().trim(), request.getBarrio().trim()));
        universidad.setCiudad(ciudad);
        universidadRepository.actualizar(universidad);
        return mapper.toDTO(universidad);
    }

    /**
     * Edita una carrera (nombre unico).
     *
     * @param id      el identificador de la carrera
     * @param request los datos nuevos
     * @return la carrera editada
     */
    @Override
    @Transactional
    public CarreraDTO actualizarCarrera(Long id, CarreraRequestDTO request) {
        Carrera carrera = buscarCarrera(id);
        String nombre = request.getNombre().trim();
        if (carreraRepository.existePorNombreExceptoId(nombre, id)) {
            LanzadorException.carreraYaExiste();
        }
        carrera.setNombre(nombre);
        carrera.setFacultad(request.getFacultad().trim());
        carreraRepository.actualizar(carrera);
        return mapper.toDTO(carrera);
    }

    /**
     * Cambia la facultad de una oferta. En el modelo la facultad es un atributo de
     * la CARRERA: cambiarla la cambia en todas las universidades.
     *
     * @param idCarrera     el identificador de la carrera
     * @param idUniversidad el identificador de la universidad
     * @param request       la facultad nueva
     * @return la oferta editada
     */
    @Override
    @Transactional
    public OfreceDTO actualizarOferta(Long idCarrera, Long idUniversidad, ActualizarOfertaRequestDTO request) {
        Ofrece oferta = buscarOferta(idCarrera, idUniversidad);
        String facultad = request.getFacultad().trim();
        oferta.setFacultad(facultad);
        oferta.getCarrera().setFacultad(facultad);
        carreraRepository.actualizar(oferta.getCarrera());
        return mapper.toDTO(oferta);
    }

    // ------------------------------------------------------------------
    // Eliminacion
    // ------------------------------------------------------------------

    /**
     * Elimina una ciudad si no tiene universidades.
     *
     * @param id el identificador de la ciudad
     */
    @Override
    @Transactional
    public void eliminarCiudad(Long id) {
        buscarCiudad(id);
        if (universidadRepository.contarPorCiudad(id) > 0) {
            LanzadorException.ciudadConUniversidades();
        }
        ciudadRepository.eliminar(id);
    }

    /**
     * Elimina una universidad si no tiene estudiantes; sus ofertas se eliminan con
     * ella.
     *
     * @param id el identificador de la universidad
     */
    @Override
    @Transactional
    public void eliminarUniversidad(Long id) {
        buscarUniversidad(id);
        if (universidadRepository.contarEstudiantes(id) > 0) {
            LanzadorException.universidadConEstudiantes();
        }
        ofreceRepository.eliminarPorUniversidad(id);
        universidadRepository.eliminar(id);
    }

    /**
     * Elimina una carrera si no tiene estudiantes; sus ofertas se eliminan con ella.
     *
     * @param id el identificador de la carrera
     */
    @Override
    @Transactional
    public void eliminarCarrera(Long id) {
        buscarCarrera(id);
        if (carreraRepository.contarEstudiantes(id) > 0) {
            LanzadorException.carreraConEstudiantes();
        }
        ofreceRepository.eliminarPorCarrera(id);
        carreraRepository.eliminar(id);
    }

    /**
     * Elimina una oferta si ningun estudiante la esta cursando.
     *
     * @param idCarrera     el identificador de la carrera
     * @param idUniversidad el identificador de la universidad
     */
    @Override
    @Transactional
    public void eliminarOferta(Long idCarrera, Long idUniversidad) {
        buscarOferta(idCarrera, idUniversidad);
        if (ofreceRepository.contarEstudiantes(idCarrera, idUniversidad) > 0) {
            LanzadorException.ofertaConEstudiantes();
        }
        ofreceRepository.eliminar(idCarrera, idUniversidad);
    }

    // ------------------------------------------------------------------
    // Busquedas con 404
    // ------------------------------------------------------------------

    /**
     * Busca una ciudad y lanza 404 si no existe.
     *
     * @param id el identificador de la ciudad
     * @return la ciudad
     */
    private Ciudad buscarCiudad(Long id) {
        Ciudad ciudad = ciudadRepository.buscarPorId(id).orElse(null);
        if (ciudad == null) {
            LanzadorException.ciudadNoEncontrada(id);
        }
        return ciudad;
    }

    /**
     * Busca una universidad y lanza 404 si no existe.
     *
     * @param id el identificador de la universidad
     * @return la universidad
     */
    private Universidad buscarUniversidad(Long id) {
        Universidad universidad = universidadRepository.buscarPorId(id).orElse(null);
        if (universidad == null) {
            LanzadorException.universidadNoEncontrada(id);
        }
        return universidad;
    }

    /**
     * Busca una carrera y lanza 404 si no existe.
     *
     * @param id el identificador de la carrera
     * @return la carrera
     */
    private Carrera buscarCarrera(Long id) {
        Carrera carrera = carreraRepository.buscarPorId(id).orElse(null);
        if (carrera == null) {
            LanzadorException.carreraNoEncontrada(id);
        }
        return carrera;
    }

    /**
     * Busca una red social y lanza 404 si no existe.
     *
     * @param id el identificador de la red social
     * @return la red social
     */
    private RedSocial buscarRedSocial(Long id) {
        RedSocial red = redSocialRepository.buscarPorId(id).orElse(null);
        if (red == null) {
            LanzadorException.redSocialNoEncontrada(id);
        }
        return red;
    }

    /**
     * Busca una oferta y lanza 404 si no existe.
     *
     * @param idCarrera     el identificador de la carrera
     * @param idUniversidad el identificador de la universidad
     * @return la oferta
     */
    private Ofrece buscarOferta(Long idCarrera, Long idUniversidad) {
        Ofrece oferta = ofreceRepository.buscarPorIds(idCarrera, idUniversidad).orElse(null);
        if (oferta == null) {
            LanzadorException.ofertaNoEncontrada(idCarrera, idUniversidad);
        }
        return oferta;
    }
}
