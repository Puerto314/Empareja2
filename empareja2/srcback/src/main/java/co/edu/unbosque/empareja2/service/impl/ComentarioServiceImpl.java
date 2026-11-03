package co.edu.unbosque.empareja2.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.empareja2.dto.request.ComentarioRequestDTO;
import co.edu.unbosque.empareja2.dto.response.ComentarioDTO;
import co.edu.unbosque.empareja2.entity.Comentario;
import co.edu.unbosque.empareja2.entity.Usuario;
import co.edu.unbosque.empareja2.exception.ResourceNotFoundException;
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

    private final ComentarioRepository comentarioRepository;
    private final UsuarioFinder usuarioFinder;
    private final InteraccionMapper interaccionMapper;

    public ComentarioServiceImpl(ComentarioRepository comentarioRepository, UsuarioFinder usuarioFinder,
            InteraccionMapper interaccionMapper) {
        this.comentarioRepository = comentarioRepository;
        this.usuarioFinder = usuarioFinder;
        this.interaccionMapper = interaccionMapper;
    }

    @Override
    @Transactional
    public ComentarioDTO enviar(String correoActual, Long idDestinatario, ComentarioRequestDTO request) {
        Usuario autor = usuarioFinder.porCorreo(correoActual);
        Usuario destinatario = usuarioFinder.estudianteVisible(idDestinatario);

        if (autor.getId().equals(destinatario.getId())) {
            throw new IllegalArgumentException("No puedes enviarte comentarios a ti mismo");
        }

        int numero = comentarioRepository.maximoNumero(autor.getId()) + 1;
        Comentario guardado = comentarioRepository
                .save(new Comentario(autor, destinatario, numero, request.contenido().trim()));
        return interaccionMapper.toComentarioDTO(guardado);
    }

    @Override
    public List<ComentarioDTO> listarRecibidos(String correoActual) {
        Usuario actual = usuarioFinder.porCorreo(correoActual);
        return comentarioRepository.listarRecibidos(actual.getId()).stream().map(interaccionMapper::toComentarioDTO)
                .toList();
    }

    @Override
    public List<ComentarioDTO> listarEnviados(String correoActual) {
        Usuario actual = usuarioFinder.porCorreo(correoActual);
        return comentarioRepository.listarEnviados(actual.getId()).stream().map(interaccionMapper::toComentarioDTO)
                .toList();
    }

    @Override
    public List<ComentarioDTO> listarTodosParaAdmin() {
        return comentarioRepository.listarTodos().stream().map(interaccionMapper::toComentarioDTO).toList();
    }

    @Override
    @Transactional
    public void eliminarPorAdmin(Long idAutor, Integer numeroComentario) {
        if (comentarioRepository.eliminar(idAutor, numeroComentario) == 0) {
            throw new ResourceNotFoundException(
                    "No existe el comentario " + numeroComentario + " del usuario " + idAutor);
        }
    }
}
