package co.edu.unbosque.empareja2.service;

import java.util.List;

import co.edu.unbosque.empareja2.dto.ComentarioDTO;
import co.edu.unbosque.empareja2.dto.ComentarioRequestDTO;

/**
 * Contrato de los comentarios privados: solo los ve su destinatario (y quien
 * los escribio).
 */
public interface ComentarioService {

    /**
     * Envia un comentario privado a otro usuario.
     *
     * @param correoActual   el correo del usuario que escribe
     * @param idDestinatario el identificador del usuario que lo recibe
     * @param request        el contenido del comentario
     * @return el comentario enviado
     */
    ComentarioDTO enviar(String correoActual, Long idDestinatario, ComentarioRequestDTO request);

    /**
     * Lista los comentarios que recibio el usuario.
     *
     * @param correoActual el correo del usuario
     * @return los comentarios recibidos
     */
    List<ComentarioDTO> listarRecibidos(String correoActual);

    /**
     * Lista los comentarios que envio el usuario.
     *
     * @param correoActual el correo del usuario
     * @return los comentarios enviados
     */
    List<ComentarioDTO> listarEnviados(String correoActual);

    /**
     * Moderacion (solo ADMIN): todos los comentarios del sistema.
     *
     * @return todos los comentarios
     */
    List<ComentarioDTO> listarTodosParaAdmin();

    /**
     * Moderacion (solo ADMIN): elimina un comentario por su llave (autor + numero).
     *
     * @param idAutor          el identificador del autor
     * @param numeroComentario el numero del comentario
     */
    void eliminarPorAdmin(Long idAutor, Integer numeroComentario);
}
