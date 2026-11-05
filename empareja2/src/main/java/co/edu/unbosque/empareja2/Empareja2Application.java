package co.edu.unbosque.empareja2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de Empareja2: arranca la aplicacion Spring Boot (API REST del
 * catalogo de estudiantes universitarios).
 */
@SpringBootApplication
public class Empareja2Application {

    /**
     * Punto de entrada de la aplicacion.
     *
     * @param args los argumentos de la linea de comandos
     */
    public static void main(String[] args) {
        SpringApplication.run(Empareja2Application.class, args);
    }
}
