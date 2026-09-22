package co.edu.unbosque.empareja2.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import co.edu.unbosque.empareja2.entity.Role;
import co.edu.unbosque.empareja2.entity.Student;
import co.edu.unbosque.empareja2.repository.StudentRepository;

/**
 * Crea, al arrancar la aplicacion, la cuenta de administrador semilla
 * si todavia no existe. Solo el usuario con rol ADMIN puede administrar
 * administradores y usuarios (ver SecurityConfig y AdminController).
 */
@Component
public class AdminSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminSeeder.class);

    private static final String ADMIN_USERNAME = "Puerto314";
    private static final String ADMIN_PASSWORD = "Puerto314";

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminSeeder(StudentRepository studentRepository, PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (studentRepository.existsByEmail(ADMIN_USERNAME)) {
            return;
        }

        Student admin = new Student();
        admin.setEmail(ADMIN_USERNAME);
        admin.setPassword(passwordEncoder.encode(ADMIN_PASSWORD));
        admin.setRole(Role.ADMIN);
        admin.setFullName("Administrador");

        studentRepository.save(admin);
        log.info("Usuario administrador semilla creado: {}", ADMIN_USERNAME);
    }
}
