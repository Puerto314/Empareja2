package co.edu.unbosque.empareja2.config;

import java.security.SecureRandom;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import co.edu.unbosque.empareja2.entity.Rol;
import co.edu.unbosque.empareja2.entity.TipoDocumento;
import co.edu.unbosque.empareja2.entity.Usuario;
import co.edu.unbosque.empareja2.repository.UsuarioRepository;

/**
 * Crea, al arrancar la aplicacion, la cuenta de administrador semilla si todavia
 * no existe. Las credenciales se pueden cambiar con las propiedades
 * app.admin.correo y app.admin.contrasena (o las variables de entorno
 * ADMIN_CORREO y ADMIN_CONTRASENA). Si no se define la contrasena se genera una
 * aleatoria que se imprime una vez en el log.
 */
@Component
public class AdminSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminSeeder.class);

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final String adminCorreo;
    private final String adminContrasena;

    public AdminSeeder(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder,
            @Value("${app.admin.correo:${ADMIN_CORREO:Puerto314}}") String adminCorreo,
            @Value("${app.admin.contrasena:${ADMIN_CONTRASENA:}}") String adminContrasena) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminCorreo = adminCorreo;
        this.adminContrasena = adminContrasena;
    }

    @Override
    public void run(String... args) {
        if (usuarioRepository.existsByCorreo(adminCorreo)) {
            return;
        }

        // Sin ADMIN_CONTRASENA no hay contrasena por defecto conocida: se genera una aleatoria y se
        // muestra UNA sola vez en el log para poder entrar la primera vez.
        String contrasena = adminContrasena;
        boolean generada = contrasena == null || contrasena.isBlank();
        if (generada) {
            contrasena = generarContrasena();
        }

        Usuario admin = new Usuario();
        admin.setCorreo(adminCorreo);
        admin.setContrasenaHash(passwordEncoder.encode(contrasena));
        admin.setRol(Rol.ADMIN);
        admin.setTipoDocumento(TipoDocumento.CC);
        admin.setDocumento("ADMIN");
        admin.setNombre("Administrador");
        admin.setApellido("Sistema");

        usuarioRepository.save(admin);
        log.info("Usuario administrador semilla creado: {}", adminCorreo);
        if (generada) {
            log.warn("Contrasena generada para '{}': {}  (cambiala o define ADMIN_CONTRASENA)", adminCorreo,
                    contrasena);
        }
    }

    private static String generarContrasena() {
        byte[] bytes = new byte[18];
        new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
