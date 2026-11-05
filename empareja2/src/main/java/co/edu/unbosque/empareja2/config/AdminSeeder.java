package co.edu.unbosque.empareja2.config;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.empareja2.entity.Genero;
import co.edu.unbosque.empareja2.entity.Rol;
import co.edu.unbosque.empareja2.entity.Usuario;
import co.edu.unbosque.empareja2.repository.UsuarioRepository;

/**
 * Crea, al arrancar la aplicacion, la cuenta de administrador semilla si
 * todavia no existe. Las credenciales se pueden cambiar con las propiedades
 * app.admin.correo y app.admin.contrasena (o las variables de entorno
 * ADMIN_CORREO y ADMIN_CONTRASENA). Si no se define la contrasena se genera una
 * aleatoria que se imprime una vez en el log.
 */
@Component
public class AdminSeeder implements CommandLineRunner {

	/** Registro de eventos de la clase. */
	private static final Logger log = LoggerFactory.getLogger(AdminSeeder.class);

	/** Acceso a los datos de los usuarios. */
	private final UsuarioRepository usuarioRepository;

	/** Cifra la contrasena del administrador. */
	private final PasswordEncoder passwordEncoder;

	/** Correo (nombre de usuario) del administrador semilla. */
	private final String adminCorreo;

	/** Contrasena del administrador semilla (vacia si se debe generar una). */
	private final String adminContrasena;

	/**
	 * Crea el sembrador.
	 *
	 * @param usuarioRepository el repositorio de usuarios
	 * @param passwordEncoder   el cifrador de contrasenas
	 * @param adminCorreo       el correo del administrador semilla
	 * @param adminContrasena   la contrasena del administrador semilla (puede estar
	 *                          vacia)
	 */
	public AdminSeeder(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder,
			@Value("${app.admin.correo:${ADMIN_CORREO:Puerto314}}") String adminCorreo,
			@Value("${app.admin.contrasena:${ADMIN_CONTRASENA:}}") String adminContrasena) {
		this.usuarioRepository = usuarioRepository;
		this.passwordEncoder = passwordEncoder;
		this.adminCorreo = adminCorreo;
		this.adminContrasena = adminContrasena;
	}

	/**
	 * Crea el administrador semilla si no existe.
	 *
	 * @param args los argumentos de arranque (no se usan)
	 */
	@Override
	@Transactional
	public void run(String... args) {
		if (usuarioRepository.existePorCorreo(adminCorreo)) {
			return;
		}

		// Sin ADMIN_CONTRASENA no hay contrasena por defecto conocida: se genera una
		// aleatoria y se
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
		admin.setGenero(Genero.NO_ESPECIFICA);
		admin.setFechaNacimiento(LocalDate.of(2000, 1, 1));
		admin.setNombre("Administrador");
		admin.setApellido("Sistema");

		usuarioRepository.guardar(admin);
		log.info("Usuario administrador semilla creado: {}", adminCorreo);
		if (generada) {
			log.warn("Contrasena generada para '{}': {}  (cambiala o define ADMIN_CONTRASENA)", adminCorreo,
					contrasena);
		}
	}

	/**
	 * Genera una contrasena aleatoria.
	 *
	 * @return una contrasena aleatoria de 24 caracteres
	 */
	private static String generarContrasena() {
		byte[] bytes = new byte[18];
		new SecureRandom().nextBytes(bytes);
		return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
	}
}
