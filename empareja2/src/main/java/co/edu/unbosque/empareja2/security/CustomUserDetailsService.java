package co.edu.unbosque.empareja2.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import co.edu.unbosque.empareja2.entity.Usuario;
import co.edu.unbosque.empareja2.exception.LanzadorException;
import co.edu.unbosque.empareja2.repository.UsuarioRepository;

/**
 * Puente entre Spring Security y la entidad Usuario. El "username" del sistema
 * es el correo.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    /** Acceso a los datos de los usuarios. */
    private final UsuarioRepository usuarioRepository;

    /**
     * Crea el servicio.
     *
     * @param usuarioRepository el repositorio de usuarios
     */
    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Carga los datos de acceso (correo, contrasena cifrada y rol) de un usuario.
     *
     * @param correo el correo del usuario
     * @return los datos de acceso del usuario
     * @throws UsernameNotFoundException si no existe un usuario con ese correo
     */
    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.buscarPorCorreo(correo).orElse(null);
        if (usuario == null) {
            LanzadorException.usuarioDeSesionNoEncontrado(correo);
        }

        return User.builder().username(usuario.getCorreo()).password(usuario.getContrasenaHash())
                .authorities("ROLE_" + usuario.getRol().name()).build();
    }
}
