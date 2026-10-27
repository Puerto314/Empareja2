package co.edu.unbosque.empareja2.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.empareja2.dto.response.MeGustaDTO;
import co.edu.unbosque.empareja2.dto.response.ReaccionDTO;
import co.edu.unbosque.empareja2.entity.Reaccion;
import co.edu.unbosque.empareja2.entity.TipoReaccion;
import co.edu.unbosque.empareja2.entity.Usuario;
import co.edu.unbosque.empareja2.exception.DuplicateResourceException;
import co.edu.unbosque.empareja2.exception.ResourceNotFoundException;
import co.edu.unbosque.empareja2.mapper.InteraccionMapper;
import co.edu.unbosque.empareja2.repository.ReaccionRepository;
import co.edu.unbosque.empareja2.service.ReaccionService;

/**
 * "Me gusta" a perfiles. Son publicos: cualquier usuario autenticado puede ver
 * quien dio me gusta a un perfil.
 */
@Service
@Transactional(readOnly = true)
public class ReaccionServiceImpl implements ReaccionService {

    private final ReaccionRepository reaccionRepository;
    private final UsuarioFinder usuarioFinder;
    private final InteraccionMapper interaccionMapper;

    public ReaccionServiceImpl(ReaccionRepository reaccionRepository, UsuarioFinder usuarioFinder,
            InteraccionMapper interaccionMapper) {
        this.reaccionRepository = reaccionRepository;
        this.usuarioFinder = usuarioFinder;
        this.interaccionMapper = interaccionMapper;
    }

    @Override
    @Transactional
    public MeGustaDTO darMeGusta(String correoActual, Long idPerfil) {
        Usuario autor = usuarioFinder.porCorreo(correoActual);
        Usuario destinatario = usuarioFinder.estudianteVisible(idPerfil);

        if (autor.getId().equals(destinatario.getId())) {
            throw new IllegalArgumentException("No puedes darte me gusta a ti mismo");
        }
        if (reaccionRepository.buscar(autor.getId(), destinatario.getId(), TipoReaccion.ME_GUSTA).isPresent()) {
            throw new DuplicateResourceException("Ya le diste me gusta a este perfil");
        }

        int numero = reaccionRepository.maximoNumero(autor.getId()) + 1;
        reaccionRepository.save(new Reaccion(autor, destinatario, numero, TipoReaccion.ME_GUSTA));

        return new MeGustaDTO(destinatario.getId(),
                reaccionRepository.contar(destinatario.getId(), TipoReaccion.ME_GUSTA), true);
    }

    @Override
    @Transactional
    public MeGustaDTO quitarMeGusta(String correoActual, Long idPerfil) {
        Usuario autor = usuarioFinder.porCorreo(correoActual);
        Usuario destinatario = usuarioFinder.estudianteVisible(idPerfil);

        Reaccion reaccion = reaccionRepository.buscar(autor.getId(), destinatario.getId(), TipoReaccion.ME_GUSTA)
                .orElseThrow(() -> new ResourceNotFoundException("No le has dado me gusta a este perfil"));
        reaccionRepository.delete(reaccion);

        return new MeGustaDTO(destinatario.getId(),
                reaccionRepository.contar(destinatario.getId(), TipoReaccion.ME_GUSTA), false);
    }

    @Override
    public List<ReaccionDTO> listarMeGusta(Long idPerfil) {
        Usuario destinatario = usuarioFinder.estudianteVisible(idPerfil);
        return reaccionRepository.listarPorDestinatario(destinatario.getId(), TipoReaccion.ME_GUSTA).stream()
                .map(interaccionMapper::toReaccionDTO).toList();
    }
}
