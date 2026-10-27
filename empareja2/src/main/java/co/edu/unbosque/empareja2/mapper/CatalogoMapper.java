package co.edu.unbosque.empareja2.mapper;

import org.springframework.stereotype.Component;

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

/**
 * Traduce los datos de referencia (ciudades, universidades, carreras, redes
 * sociales y la oferta academica) a DTOs.
 */
@Component
public class CatalogoMapper {

    public CiudadDTO toDTO(Ciudad ciudad) {
        return new CiudadDTO(ciudad.getIdCiudad(), ciudad.getNombre(), ciudad.getDepartamento());
    }

    public CarreraDTO toDTO(Carrera carrera) {
        return new CarreraDTO(carrera.getIdCarrera(), carrera.getNombre());
    }

    public RedSocialDTO toDTO(RedSocial red) {
        return new RedSocialDTO(red.getIdRedSocial(), red.getNombreRedSocial(), red.getUrl());
    }

    public UniversidadDTO toDTO(Universidad universidad) {
        Direccion d = universidad.getDireccion() != null ? universidad.getDireccion() : new Direccion();
        return new UniversidadDTO(universidad.getIdUniversidad(), universidad.getNombre(), universidad.getSigla(),
                d.getCalle(), d.getBarrio(), d.getNumeroDireccion(), toDTO(universidad.getCiudad()));
    }

    public OfreceDTO toDTO(Ofrece ofrece) {
        return new OfreceDTO(ofrece.getCarrera().getIdCarrera(), ofrece.getCarrera().getNombre(),
                ofrece.getUniversidad().getIdUniversidad(), ofrece.getUniversidad().getNombre(),
                ofrece.getFacultad());
    }
}
