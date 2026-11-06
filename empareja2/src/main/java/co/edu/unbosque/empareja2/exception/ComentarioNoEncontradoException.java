package co.edu.unbosque.empareja2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Se lanza en el caso de un comentario que no existe. Se responde con 404.
 */
public class ComentarioNoEncontradoException extends ResponseStatusException {

    /**
     * Crea la excepcion con su mensaje ya definido.
     *
     * @param idAutor          el identificador del autor del comentario
     * @param numeroComentario el numero del comentario
     */
    public ComentarioNoEncontradoException(Long idAutor, Integer numeroComentario) {
        super(HttpStatus.NOT_FOUND,
                "No existe el comentario " + numeroComentario + " del usuario " + idAutor);
    }
}
