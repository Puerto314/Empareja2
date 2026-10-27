package co.edu.unbosque.empareja2.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import co.edu.unbosque.empareja2.entity.Usuario;
import co.edu.unbosque.empareja2.repository.UsuarioRepository;

/**
 * Puente entre Spring Security y la entidad Usuario. El "username" del sistema
 * es el correo.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("No existe un usuario con el correo: " + correo));

        return User.builder().username(usuario.getCorreo()).password(usuario.getContrasenaHash())
                .authorities("ROLE_" + usuario.getRol().name()).build();
    }
}
