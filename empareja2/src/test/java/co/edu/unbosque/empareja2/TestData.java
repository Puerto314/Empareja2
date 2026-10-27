package co.edu.unbosque.empareja2;

import java.time.LocalDate;

import co.edu.unbosque.empareja2.entity.Carrera;
import co.edu.unbosque.empareja2.entity.Ciudad;
import co.edu.unbosque.empareja2.entity.Direccion;
import co.edu.unbosque.empareja2.entity.RedSocial;
import co.edu.unbosque.empareja2.entity.Rol;
import co.edu.unbosque.empareja2.entity.TipoDocumento;
import co.edu.unbosque.empareja2.entity.Universidad;
import co.edu.unbosque.empareja2.entity.Usuario;

/**
 * Fabrica de objetos de prueba compartida por los tests.
 */
public final class TestData {

    private TestData() {
    }

    public static Usuario usuario(Long id, String correo, String nombre, String apellido) {
        Usuario u = new Usuario();
        u.setId(id);
        u.setCorreo(correo);
        u.setContrasenaHash("hash-" + id);
        u.setRol(Rol.ESTUDIANTE);
        u.setTipoDocumento(TipoDocumento.CC);
        u.setDocumento("100" + id);
        u.setNombre(nombre);
        u.setApellido(apellido);
        u.setFechaNacimiento(LocalDate.now().minusYears(22));
        return u;
    }

    public static Ciudad ciudad(Long id, String nombre) {
        Ciudad c = new Ciudad(nombre, "Cundinamarca");
        c.setIdCiudad(id);
        return c;
    }

    public static Universidad universidad(Long id, String nombre, String sigla) {
        Universidad u = new Universidad(nombre, sigla, new Direccion("Calle 1", "Centro", "10-20"),
                ciudad(1L, "Bogota"));
        u.setIdUniversidad(id);
        return u;
    }

    public static Carrera carrera(Long id, String nombre) {
        Carrera c = new Carrera(nombre);
        c.setIdCarrera(id);
        return c;
    }

    public static RedSocial redSocial(Long id, String nombre) {
        RedSocial r = new RedSocial(nombre, "https://" + nombre.toLowerCase() + ".com");
        r.setIdRedSocial(id);
        return r;
    }
}
