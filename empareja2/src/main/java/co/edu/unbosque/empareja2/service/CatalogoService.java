package co.edu.unbosque.empareja2.service;

import java.util.List;

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

/**
 * Datos de referencia: ciudades, universidades, carreras, redes sociales y la
 * oferta academica. La consulta es publica (se necesita para registrarse); la
 * creacion, edicion y eliminacion son solo para administradores.
 */
public interface CatalogoService {

    List<CiudadDTO> listarCiudades();

    List<UniversidadDTO> listarUniversidades(String texto);

    List<CarreraDTO> listarCarreras();

    List<RedSocialDTO> listarRedesSociales();

    List<OfreceDTO> listarOferta(Long idUniversidad);

    CiudadDTO crearCiudad(CiudadRequestDTO request);

    UniversidadDTO crearUniversidad(UniversidadRequestDTO request);

    CarreraDTO crearCarrera(CarreraRequestDTO request);

    RedSocialDTO crearRedSocial(RedSocialRequestDTO request);

    OfreceDTO crearOferta(OfreceRequestDTO request);

    CiudadDTO obtenerCiudad(Long id);

    UniversidadDTO obtenerUniversidad(Long id);

    CarreraDTO obtenerCarrera(Long id);

    RedSocialDTO obtenerRedSocial(Long id);

    CiudadDTO actualizarCiudad(Long id, CiudadRequestDTO request);

    UniversidadDTO actualizarUniversidad(Long id, UniversidadRequestDTO request);

    CarreraDTO actualizarCarrera(Long id, CarreraRequestDTO request);

    RedSocialDTO actualizarRedSocial(Long id, RedSocialRequestDTO request);

    /** Cambia solo la facultad: la carrera y la universidad forman la llave. */
    OfreceDTO actualizarOferta(Long idCarrera, Long idUniversidad, ActualizarOfertaRequestDTO request);

    /** No se puede eliminar una ciudad que todavia tiene universidades. */
    void eliminarCiudad(Long id);

    /** No se puede eliminar una universidad con estudiantes; sus ofertas se eliminan con ella. */
    void eliminarUniversidad(Long id);

    /** No se puede eliminar una carrera con estudiantes; sus ofertas se eliminan con ella. */
    void eliminarCarrera(Long id);

    /** No se puede eliminar una red social que algun usuario tiene en su perfil. */
    void eliminarRedSocial(Long id);

    /** No se puede eliminar una oferta que algun estudiante esta cursando. */
    void eliminarOferta(Long idCarrera, Long idUniversidad);
}
