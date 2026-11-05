package co.edu.unbosque.empareja2.service;

import java.util.List;

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

/**
 * Datos de referencia: ciudades, universidades, carreras, redes sociales y la
 * oferta academica. La consulta es publica (se necesita para registrarse); la
 * creacion, edicion y eliminacion son solo para administradores.
 */
public interface CatalogoService {

    /**
     * Lista las ciudades ordenadas por nombre.
     *
     * @return las ciudades
     */
    List<CiudadDTO> listarCiudades();

    /**
     * Lista las universidades; si hay texto, busca por nombre o sigla.
     *
     * @param texto el texto a buscar (puede ser null o vacio)
     * @return las universidades
     */
    List<UniversidadDTO> listarUniversidades(String texto);

    /**
     * Lista las carreras ordenadas por nombre.
     *
     * @return las carreras
     */
    List<CarreraDTO> listarCarreras();

    /**
     * Lista las redes sociales ordenadas por nombre.
     *
     * @return las redes sociales
     */
    List<RedSocialDTO> listarRedesSociales();

    /**
     * Lista la oferta academica, de todas las universidades o de una sola.
     *
     * @param idUniversidad la universidad a filtrar (null para todas)
     * @return las ofertas
     */
    List<OfreceDTO> listarOferta(Long idUniversidad);

    /**
     * Crea una ciudad.
     *
     * @param request los datos de la ciudad
     * @return la ciudad creada
     */
    CiudadDTO crearCiudad(CiudadRequestDTO request);

    /**
     * Crea una universidad.
     *
     * @param request los datos de la universidad
     * @return la universidad creada
     */
    UniversidadDTO crearUniversidad(UniversidadRequestDTO request);

    /**
     * Crea una carrera.
     *
     * @param request los datos de la carrera
     * @return la carrera creada
     */
    CarreraDTO crearCarrera(CarreraRequestDTO request);

    /**
     * Registra que una universidad ofrece una carrera.
     *
     * @param request la carrera y la universidad
     * @return la oferta creada
     */
    OfreceDTO crearOferta(OfreceRequestDTO request);

    /**
     * Obtiene una ciudad por id.
     *
     * @param id el identificador de la ciudad
     * @return la ciudad
     */
    CiudadDTO obtenerCiudad(Long id);

    /**
     * Obtiene una universidad por id.
     *
     * @param id el identificador de la universidad
     * @return la universidad
     */
    UniversidadDTO obtenerUniversidad(Long id);

    /**
     * Obtiene una carrera por id.
     *
     * @param id el identificador de la carrera
     * @return la carrera
     */
    CarreraDTO obtenerCarrera(Long id);

    /**
     * Obtiene una red social por id.
     *
     * @param id el identificador de la red social
     * @return la red social
     */
    RedSocialDTO obtenerRedSocial(Long id);

    /**
     * Edita una ciudad.
     *
     * @param id      el identificador de la ciudad
     * @param request los datos nuevos
     * @return la ciudad editada
     */
    CiudadDTO actualizarCiudad(Long id, CiudadRequestDTO request);

    /**
     * Edita una universidad.
     *
     * @param id      el identificador de la universidad
     * @param request los datos nuevos
     * @return la universidad editada
     */
    UniversidadDTO actualizarUniversidad(Long id, UniversidadRequestDTO request);

    /**
     * Edita una carrera.
     *
     * @param id      el identificador de la carrera
     * @param request los datos nuevos
     * @return la carrera editada
     */
    CarreraDTO actualizarCarrera(Long id, CarreraRequestDTO request);

    /**
     * Cambia solo la facultad: la carrera y la universidad forman la llave.
     *
     * @param idCarrera     el identificador de la carrera
     * @param idUniversidad el identificador de la universidad
     * @param request       la facultad nueva
     * @return la oferta editada
     */
    OfreceDTO actualizarOferta(Long idCarrera, Long idUniversidad, ActualizarOfertaRequestDTO request);

    /**
     * Elimina una ciudad. No se puede si todavia tiene universidades.
     *
     * @param id el identificador de la ciudad
     */
    void eliminarCiudad(Long id);

    /**
     * Elimina una universidad. No se puede si tiene estudiantes; sus ofertas se
     * eliminan con ella.
     *
     * @param id el identificador de la universidad
     */
    void eliminarUniversidad(Long id);

    /**
     * Elimina una carrera. No se puede si tiene estudiantes; sus ofertas se
     * eliminan con ella.
     *
     * @param id el identificador de la carrera
     */
    void eliminarCarrera(Long id);

    /**
     * Elimina una oferta. No se puede si algun estudiante la esta cursando.
     *
     * @param idCarrera     el identificador de la carrera
     * @param idUniversidad el identificador de la universidad
     */
    void eliminarOferta(Long idCarrera, Long idUniversidad);
}
