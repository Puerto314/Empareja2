package co.edu.unbosque.empareja2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Prueba de integracion: levanta toda la aplicacion contra el MySQL REAL, por
 * eso solo corre si defines EMPAREJA2_DB_TESTS=true (y la base empareja2 ya
 * existe, con DB_USER / DB_PASSWORD). Los demas tests no necesitan base de datos.
 */
@SpringBootTest
@EnabledIfEnvironmentVariable(named = "EMPAREJA2_DB_TESTS", matches = "true")
class Empareja2ApplicationTests {

    @Test
    void contextLoads() {
    }

}
