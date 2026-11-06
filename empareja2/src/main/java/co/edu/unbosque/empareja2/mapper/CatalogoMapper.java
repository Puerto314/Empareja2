package co.edu.unbosque.empareja2.mapper;

import org.springframework.stereotype.Component;

import co.edu.unbosque.empareja2.dto.CarreraDTO;
import co.edu.unbosque.empareja2.dto.CiudadDTO;
import co.edu.unbosque.empareja2.dto.OfreceDTO;
import co.edu.unbosque.empareja2.dto.UniversidadDTO;
import co.edu.unbosque.empareja2.entity.Carrera;
import co.edu.unbosque.empareja2.entity.Ciudad;
import co.edu.unbosque.empareja2.entity.Direccion;
import co.edu.unbosque.empareja2.entity.Ofrece;
import co.edu.unbosque.empareja2.entity.Universidad;

/**
 * Traduce los datos de referencia (ciudades, universidades, carreras
 * sociales y la oferta academica) a DTOs.
 */
@Component
public class CatalogoMapper {

    /**
     * Convierte una ciudad en su DTO.
     *
     * @param ciudad la ciudad
     * @return el DTO de la ciudad
     */
    public CiudadDTO toDTO(Ciudad ciudad) {
        return new CiudadDTO(ciudad.getIdCiudad(), ciudad.getNombre(), ciudad.getDepartamento());
    }

    /**
     * Convierte una carrera en su DTO.
     *
     * @param carrera la carrera
     * @return el DTO de la carrera
     */
    public CarreraDTO toDTO(Carrera carrera) {
        return new CarreraDTO(carrera.getIdCarrera(), carrera.getNombre(), carrera.getFacultad());
    }

    /**
     * Convierte una universidad en su DTO (la direccion se separa en calle y barrio).
     *
     * @param universidad la universidad
     * @return el DTO de la universidad
     */
    public UniversidadDTO toDTO(Universidad universidad) {
        Direccion direccion = universidad.getDireccion() != null ? universidad.getDireccion() : new Direccion();
        return new UniversidadDTO(universidad.getIdUniversidad(), universidad.getNombre(), universidad.getSigla(),
                direccion.getCalle(), direccion.getBarrio(), toDTO(universidad.getCiudad()));
    }

    /**
     * Convierte una oferta academica en su DTO.
     *
     * @param ofrece la oferta
     * @return el DTO de la oferta
     */
    public OfreceDTO toDTO(Ofrece ofrece) {
        return new OfreceDTO(ofrece.getCarrera().getIdCarrera(), ofrece.getCarrera().getNombre(),
                ofrece.getUniversidad().getIdUniversidad(), ofrece.getUniversidad().getNombre(),
                ofrece.getFacultad());
    }
}
