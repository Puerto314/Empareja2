package co.edu.unbosque.empareja2.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.empareja2.dto.ComentarioDTO;
import co.edu.unbosque.empareja2.dto.ComentarioRequestDTO;
import co.edu.unbosque.empareja2.entity.Comentario;
import co.edu.unbosque.empareja2.entity.Usuario;
import co.edu.unbosque.empareja2.exception.LanzadorException;
import co.edu.unbosque.empareja2.mapper.InteraccionMapper;
import co.edu.unbosque.empareja2.repository.ComentarioRepository;
import co.edu.unbosque.empareja2.service.ComentarioService;

/**
 * Comentarios PRIVADOS entre usuarios. Un comentario solo se consulta a traves
 * de la bandeja de su destinatario ("recibidos") o de quien lo escribio
 * ("enviados"); no existe ningun endpoint que liste comentarios de terceros.
 */
@Service
@Transactional(readOnly = true)
public class ComentarioServiceImpl implements ComentarioService {

    /** Acceso a los datos de los comentarios. */
    private final ComentarioRepository comentarioRepository;

    /** Busca usuarios con el error 404 si no existen. */
    private final UsuarioFinder usuarioFinder;

    /** Convierte comentarios en DTOs. */
    private final InteraccionMapper interaccionMapper;

    /**
     * Crea el servicio.
     *
     * @param comentarioRepository el repositorio de comentarios
     * @param usuarioFinder        el buscador de usuarios
     * @param interaccionMapper    el convertidor de comentarios
     */
    public ComentarioServiceImpl(ComentarioRepository comentarioRepository, UsuarioFinder usuarioFinder,
            InteraccionMapper interaccionMapper) {
        this.comentarioRepository = comentarioRepository;
        this.usuarioFinder = usuarioFinder;
        this.interaccionMapper = interaccionMapper;
    }

    /**
     * Envia un comentario privado a otro estudiante (no a uno mismo).
     *
     * @param correoActual   el correo del usuario que escribe
     * @param idDestinatario el identificador del usuario que lo recibe
     * @param request        el contenido del comentario
     * @return el comentario enviado
     */
    @Override
    @Transactional
    public ComentarioDTO enviar(String correoActual, Long idDestinatario, ComentarioRequestDTO request) {
        Usuario autor = usuarioFinder.porCorreo(correoActual);
        Usuario destinatario = usuarioFinder.estudianteVisible(idDestinatario);

        if (autor.getId().equals(destinatario.getId())) {
            LanzadorException.comentarioAUnoMismo();
        }

        int numero = comentarioRepository.maximoNumero(autor.getId()) + 1;
        Comentario guardado = comentarioRepository
                .guardar(new Comentario(autor, destinatario, numero, request.getContenido().trim()));
        return interaccionMapper.toComentarioDTO(guardado);
    }

    /**
     * Lista los comentarios que recibio el usuario.
     *
     * @param correoActual el correo del usuario
     * @return los comentarios recibidos
     */
    @Override
    public List<ComentarioDTO> listarRecibidos(String correoActual) {
        Usuario actual = usuarioFinder.porCorreo(correoActual);
        return comentarioRepository.listarRecibidos(actual.getId()).stream()
                .map(interaccionMapper::toComentarioDTO).toList();
    }

    /**
     * Lista los comentarios que envio el usuario.
     *
     * @param correoActual el correo del usuario
     * @return los comentarios enviados
     */
    @Override
    public List<ComentarioDTO> listarEnviados(String correoActual) {
        Usuario actual = usuarioFinder.porCorreo(correoActual);
        return comentarioRepository.listarEnviados(actual.getId()).stream()
                .map(interaccionMapper::toComentarioDTO).toList();
    }

    /**
     * Moderacion (solo ADMIN): todos los comentarios del sistema.
     *
     * @return todos los comentarios
     */
    @Override
    public List<ComentarioDTO> listarTodosParaAdmin() {
        return comentarioRepository.listarTodos().stream().map(interaccionMapper::toComentarioDTO).toList();
    }

    /**
     * Moderacion (solo ADMIN): elimina un comentario por su llave.
     *
     * @param idAutor          el identificador del autor
     * @param numeroComentario el numero del comentario
     */
    @Override
    @Transactional
    public void eliminarPorAdmin(Long idAutor, Integer numeroComentario) {
        if (comentarioRepository.eliminar(idAutor, numeroComentario) == 0) {
            LanzadorException.comentarioNoEncontrado(idAutor, numeroComentario);
        }
    }
}
