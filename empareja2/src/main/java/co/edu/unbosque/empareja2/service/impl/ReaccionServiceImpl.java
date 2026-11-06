package co.edu.unbosque.empareja2.service.impl;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.empareja2.dto.MeGustaDTO;
import co.edu.unbosque.empareja2.dto.ReaccionAdminDTO;
import co.edu.unbosque.empareja2.dto.ReaccionDTO;
import co.edu.unbosque.empareja2.entity.Reaccion;
import co.edu.unbosque.empareja2.entity.Usuario;
import co.edu.unbosque.empareja2.exception.LanzadorException;
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

    /** Acceso a los datos de las reacciones. */
    private final ReaccionRepository reaccionRepository;

    /** Busca usuarios con el error 404 si no existen. */
    private final UsuarioFinder usuarioFinder;

    /** Convierte reacciones en DTOs. */
    private final InteraccionMapper interaccionMapper;

    /**
     * Crea el servicio.
     *
     * @param reaccionRepository el repositorio de reacciones
     * @param usuarioFinder      el buscador de usuarios
     * @param interaccionMapper  el convertidor de reacciones
     */
    public ReaccionServiceImpl(ReaccionRepository reaccionRepository, UsuarioFinder usuarioFinder,
            InteraccionMapper interaccionMapper) {
        this.reaccionRepository = reaccionRepository;
        this.usuarioFinder = usuarioFinder;
        this.interaccionMapper = interaccionMapper;
    }

    /**
     * Da me gusta a un perfil (no al propio y una sola vez). Si dos peticiones
     * simultaneas intentan dar el mismo me gusta, la restriccion unica de la base
     * de datos rechaza la segunda y se responde igual que un me gusta repetido.
     *
     * @param correoActual el correo del usuario que da el me gusta
     * @param idPerfil     el identificador del perfil
     * @return el estado de los me gusta del perfil
     */
    @Override
    @Transactional
    public MeGustaDTO darMeGusta(String correoActual, Long idPerfil) {
        Usuario autor = usuarioFinder.porCorreo(correoActual);
        Usuario destinatario = usuarioFinder.estudianteVisible(idPerfil);

        if (autor.getId().equals(destinatario.getId())) {
            LanzadorException.meGustaAUnoMismo();
        }
        if (reaccionRepository.buscar(autor.getId(), destinatario.getId()).isPresent()) {
            LanzadorException.meGustaYaDado();
        }

        int numero = reaccionRepository.maximoNumero(autor.getId()) + 1;
        try {
            reaccionRepository.guardar(new Reaccion(autor, destinatario, numero));
        } catch (DataIntegrityViolationException ex) {
            LanzadorException.meGustaYaDado();
        }

        return new MeGustaDTO(destinatario.getId(),
                reaccionRepository.contar(destinatario.getId()), true);
    }

    /**
     * Quita el me gusta dado a un perfil.
     *
     * @param correoActual el correo del usuario que quita el me gusta
     * @param idPerfil     el identificador del perfil
     * @return el estado de los me gusta del perfil
     */
    @Override
    @Transactional
    public MeGustaDTO quitarMeGusta(String correoActual, Long idPerfil) {
        Usuario autor = usuarioFinder.porCorreo(correoActual);
        Usuario destinatario = usuarioFinder.estudianteVisible(idPerfil);

        Reaccion reaccion = reaccionRepository.buscar(autor.getId(), destinatario.getId()).orElse(null);
        if (reaccion == null) {
            LanzadorException.meGustaNoEncontrado();
        }
        reaccionRepository.eliminar(reaccion.getAutor().getId(), reaccion.getNumeroReaccion());

        return new MeGustaDTO(destinatario.getId(),
                reaccionRepository.contar(destinatario.getId()), false);
    }

    /**
     * Lista publica de quienes dieron me gusta a un perfil.
     *
     * @param idPerfil el identificador del perfil
     * @return las reacciones recibidas
     */
    @Override
    public List<ReaccionDTO> listarMeGusta(Long idPerfil) {
        Usuario destinatario = usuarioFinder.estudianteVisible(idPerfil);
        return reaccionRepository.listarPorDestinatario(destinatario.getId()).stream()
                .map(interaccionMapper::toReaccionDTO).toList();
    }

    /**
     * Moderacion (solo ADMIN): todas las reacciones del sistema.
     *
     * @return todas las reacciones
     */
    @Override
    public List<ReaccionAdminDTO> listarTodasParaAdmin() {
        return reaccionRepository.listarTodas().stream().map(interaccionMapper::toReaccionAdminDTO).toList();
    }

    /**
     * Moderacion (solo ADMIN): elimina una reaccion por su llave.
     *
     * @param idAutor        el identificador del autor
     * @param numeroReaccion el numero de la reaccion
     */
    @Override
    @Transactional
    public void eliminarPorAdmin(Long idAutor, Integer numeroReaccion) {
        if (reaccionRepository.eliminar(idAutor, numeroReaccion) == 0) {
            LanzadorException.reaccionNoEncontrada(idAutor, numeroReaccion);
        }
    }
}
