package co.edu.unbosque.empareja2.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * La direccion es un atributo compuesto: calle + barrio + numero_direccion.
 */
public record UniversidadRequestDTO(
        @NotBlank(message = "El nombre es obligatorio") String nombre,
        @NotBlank(message = "La sigla es obligatoria") String sigla,
        String calle,
        String barrio,
        String numeroDireccion,
        @NotNull(message = "La ciudad es obligatoria") Long idCiudad) {
}
