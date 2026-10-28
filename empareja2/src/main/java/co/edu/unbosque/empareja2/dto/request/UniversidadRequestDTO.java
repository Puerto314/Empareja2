package co.edu.unbosque.empareja2.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;

/**
 * La direccion es un atributo compuesto: calle + barrio + numero_direccion.
 */
public record UniversidadRequestDTO(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 150, message = "El nombre es demasiado largo") String nombre,
        @NotBlank(message = "La sigla es obligatoria")
        @Size(max = 20, message = "La sigla es demasiado larga") String sigla,
        @Size(max = 100, message = "La calle es demasiado larga") String calle,
        @Size(max = 100, message = "El barrio es demasiado largo") String barrio,
        @Size(max = 20, message = "El numero de direccion es demasiado largo") String numeroDireccion,
        @NotNull(message = "La ciudad es obligatoria") Long idCiudad) {
}
