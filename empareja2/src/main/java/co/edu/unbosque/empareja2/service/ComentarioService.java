package co.edu.unbosque.empareja2.service;

import java.util.List;

import co.edu.unbosque.empareja2.dto.request.ComentarioRequestDTO;
import co.edu.unbosque.empareja2.dto.response.ComentarioDTO;

/**
 * Contrato de los comentarios privados: solo los ve su destinatario (y quien
 * los escribio).
 */
public interface ComentarioService {

    ComentarioDTO enviar(String correoActual, Long idDestinatario, ComentarioRequestDTO request);

    List<ComentarioDTO> listarRecibidos(String correoActual);

    List<ComentarioDTO> listarEnviados(String correoActual);
}
