package co.edu.unbosque.empareja2.dto.response;

public record EstudioDTO(Long idCarrera, String carrera, Long idUniversidad, String universidad,
        String siglaUniversidad, Integer semestre) {
}
