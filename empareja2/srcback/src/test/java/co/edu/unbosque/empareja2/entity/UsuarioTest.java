package co.edu.unbosque.empareja2.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class UsuarioTest {

    @Test
    void nombreCompletoEsCompuestoDeNombreYApellido() {
        Usuario u = new Usuario();
        u.setNombre("  Ana ");
        u.setApellido("Garcia  ");
        assertThat(u.getNombreCompleto()).isEqualTo("Ana Garcia");
    }

    @Test
    void nombreCompletoToleraAtributosNulos() {
        Usuario u = new Usuario();
        u.setNombre("Ana");
        assertThat(u.getNombreCompleto()).isEqualTo("Ana");
        assertThat(new Usuario().getNombreCompleto()).isEmpty();
    }

    @Test
    void edadSeDerivaDeLaFechaDeNacimiento() {
        LocalDate hoy = LocalDate.of(2026, 10, 25);
        assertThat(Usuario.calcularEdad(LocalDate.of(2000, 10, 25), hoy)).isEqualTo(26);
    }

    @Test
    void edadNoCuentaElAnioSiAunNoHaCumplido() {
        LocalDate hoy = LocalDate.of(2026, 10, 25);
        assertThat(Usuario.calcularEdad(LocalDate.of(2000, 10, 26), hoy)).isEqualTo(25);
    }

    @Test
    void edadEsNulaSinFechaDeNacimiento() {
        assertThat(Usuario.calcularEdad(null, LocalDate.now())).isNull();
        assertThat(new Usuario().getEdad()).isNull();
    }

    @Test
    void getEdadUsaLaFechaActual() {
        Usuario u = new Usuario();
        u.setFechaNacimiento(LocalDate.now().minusYears(30).minusDays(1));
        assertThat(u.getEdad()).isEqualTo(30);
    }

    @Test
    void elRolPorDefectoEsEstudiante() {
        assertThat(new Usuario().getRol()).isEqualTo(Rol.ESTUDIANTE);
    }
}
