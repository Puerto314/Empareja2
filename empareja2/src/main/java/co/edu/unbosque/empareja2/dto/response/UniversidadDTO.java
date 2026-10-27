package co.edu.unbosque.empareja2.dto.response;

public record UniversidadDTO(Long idUniversidad, String nombre, String sigla, String calle, String barrio,
        String numeroDireccion, CiudadDTO ciudad) {
}
