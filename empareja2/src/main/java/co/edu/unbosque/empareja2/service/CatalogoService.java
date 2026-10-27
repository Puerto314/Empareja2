package co.edu.unbosque.empareja2.service;

import java.util.List;

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
 * creacion es solo para administradores.
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
}
