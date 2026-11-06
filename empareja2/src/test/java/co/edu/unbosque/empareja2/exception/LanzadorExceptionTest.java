package co.edu.unbosque.empareja2.exception;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import co.edu.unbosque.empareja2.entity.Usuario;

/**
 * Cada metodo del lanzador debe lanzar SU propia clase de excepcion, con el
 * mensaje dentro de la clase y (para las que responden por HTTP) con su codigo.
 */
class LanzadorExceptionTest {

    private static Arguments caso(String nombre, Runnable accion, Class<? extends RuntimeException> tipo,
            HttpStatus estado, String mensaje) {
        return Arguments.of(nombre, accion, tipo, estado, mensaje);
    }

    static Stream<Arguments> casos() {
        return Stream.of(
                caso("usuarioNoEncontradoPorCorreo", () -> LanzadorException.usuarioNoEncontradoPorCorreo("ana@mail.com"),
                        UsuarioNoEncontradoPorCorreoException.class, HttpStatus.NOT_FOUND,
                        "No existe un usuario con correo ana@mail.com"),
                caso("usuarioNoEncontradoPorId", () -> LanzadorException.usuarioNoEncontradoPorId(7L),
                        UsuarioNoEncontradoPorIdException.class, HttpStatus.NOT_FOUND,
                        "No existe un usuario con id 7"),
                caso("ciudadNoEncontrada", () -> LanzadorException.ciudadNoEncontrada(3L),
                        CiudadNoEncontradaException.class, HttpStatus.NOT_FOUND,
                        "No existe la ciudad con id 3"),
                caso("universidadNoEncontrada", () -> LanzadorException.universidadNoEncontrada(4L),
                        UniversidadNoEncontradaException.class, HttpStatus.NOT_FOUND,
                        "No existe la universidad con id 4"),
                caso("carreraNoEncontrada", () -> LanzadorException.carreraNoEncontrada(5L),
                        CarreraNoEncontradaException.class, HttpStatus.NOT_FOUND,
                        "No existe la carrera con id 5"),
                caso("ofertaNoEncontrada", () -> LanzadorException.ofertaNoEncontrada(1L, 2L),
                        OfertaNoEncontradaException.class, HttpStatus.NOT_FOUND,
                        "La universidad 2 no ofrece la carrera 1"),
                caso("reporteNoEncontrado", () -> LanzadorException.reporteNoEncontrado(6L),
                        ReporteNoEncontradoException.class, HttpStatus.NOT_FOUND,
                        "No existe el reporte con id 6"),
                caso("comentarioNoEncontrado", () -> LanzadorException.comentarioNoEncontrado(1L, 2),
                        ComentarioNoEncontradoException.class, HttpStatus.NOT_FOUND,
                        "No existe el comentario 2 del usuario 1"),
                caso("reaccionNoEncontrada", () -> LanzadorException.reaccionNoEncontrada(1L, 3),
                        ReaccionNoEncontradaException.class, HttpStatus.NOT_FOUND,
                        "No existe la reaccion 3 del usuario 1"),
                caso("meGustaNoEncontrado", LanzadorException::meGustaNoEncontrado,
                        MeGustaNoEncontradoException.class, HttpStatus.NOT_FOUND,
                        "No le has dado me gusta a este perfil"),
                caso("correoYaRegistrado", LanzadorException::correoYaRegistrado,
                        CorreoYaRegistradoException.class, HttpStatus.CONFLICT,
                        "Ya existe un usuario registrado con ese correo"),
                caso("correoUsuarioYaExiste", LanzadorException::correoUsuarioYaExiste,
                        CorreoUsuarioYaExisteException.class, HttpStatus.CONFLICT,
                        "Ya existe un usuario con ese correo/usuario"),
                caso("ciudadYaExiste", LanzadorException::ciudadYaExiste,
                        CiudadYaExisteException.class, HttpStatus.CONFLICT,
                        "Ya existe esa ciudad en ese departamento"),
                caso("universidadNombreYaExiste", LanzadorException::universidadNombreYaExiste,
                        UniversidadNombreYaExisteException.class, HttpStatus.CONFLICT,
                        "Ya existe una universidad con ese nombre"),
                caso("universidadSiglaYaExiste", LanzadorException::universidadSiglaYaExiste,
                        UniversidadSiglaYaExisteException.class, HttpStatus.CONFLICT,
                        "Ya existe una universidad con esa sigla"),
                caso("carreraYaExiste", LanzadorException::carreraYaExiste,
                        CarreraYaExisteException.class, HttpStatus.CONFLICT,
                        "Ya existe una carrera con ese nombre"),
                caso("ofertaYaExiste", LanzadorException::ofertaYaExiste,
                        OfertaYaExisteException.class, HttpStatus.CONFLICT,
                        "Esa universidad ya ofrece esa carrera"),
                caso("meGustaYaDado", LanzadorException::meGustaYaDado,
                        MeGustaYaDadoException.class, HttpStatus.CONFLICT,
                        "Ya le diste me gusta a este perfil"),
                caso("reporteActivoYaExiste", LanzadorException::reporteActivoYaExiste,
                        ReporteActivoYaExisteException.class, HttpStatus.CONFLICT,
                        "Ya reportaste este perfil por ese motivo y el reporte todavia esta en revision"),
                caso("ciudadConUniversidades", LanzadorException::ciudadConUniversidades,
                        CiudadConUniversidadesException.class, HttpStatus.CONFLICT,
                        "No se puede eliminar la ciudad: todavia tiene universidades registradas"),
                caso("universidadConEstudiantes", LanzadorException::universidadConEstudiantes,
                        UniversidadConEstudiantesException.class, HttpStatus.CONFLICT,
                        "No se puede eliminar la universidad: hay usuarios que estudian en ella"),
                caso("carreraConEstudiantes", LanzadorException::carreraConEstudiantes,
                        CarreraConEstudiantesException.class, HttpStatus.CONFLICT,
                        "No se puede eliminar la carrera: hay usuarios que la estudian"),
                caso("ofertaConEstudiantes", LanzadorException::ofertaConEstudiantes,
                        OfertaConEstudiantesException.class, HttpStatus.CONFLICT,
                        "No se puede eliminar la oferta: hay usuarios que cursan esa carrera en esa universidad"),
                caso("ultimoAdministradorSinRol", LanzadorException::ultimoAdministradorSinRol,
                        UltimoAdministradorSinRolException.class, HttpStatus.CONFLICT,
                        "No se puede quitar el rol al ultimo administrador"),
                caso("ultimoAdministradorEliminado", LanzadorException::ultimoAdministradorEliminado,
                        UltimoAdministradorEliminadoException.class, HttpStatus.CONFLICT,
                        "No se puede eliminar al ultimo administrador"),
                caso("comentarioAUnoMismo", LanzadorException::comentarioAUnoMismo,
                        ComentarioAUnoMismoException.class, HttpStatus.BAD_REQUEST,
                        "No puedes enviarte comentarios a ti mismo"),
                caso("meGustaAUnoMismo", LanzadorException::meGustaAUnoMismo,
                        MeGustaAUnoMismoException.class, HttpStatus.BAD_REQUEST,
                        "No puedes darte me gusta a ti mismo"),
                caso("reporteAUnoMismo", LanzadorException::reporteAUnoMismo,
                        ReporteAUnoMismoException.class, HttpStatus.BAD_REQUEST,
                        "No puedes reportar tu propio perfil"),
                caso("reporteOtroSinDescripcion", LanzadorException::reporteOtroSinDescripcion,
                        ReporteOtroSinDescripcionException.class, HttpStatus.BAD_REQUEST,
                        "Describe el motivo cuando el tipo de reporte es OTRO"),
                caso("limiteRankingInvalido", LanzadorException::limiteRankingInvalido,
                        LimiteRankingInvalidoException.class, HttpStatus.BAD_REQUEST,
                        "El limite del ranking debe estar entre 1 y 100"),
                caso("contrasenaObligatoriaAlCrear", LanzadorException::contrasenaObligatoriaAlCrear,
                        ContrasenaObligatoriaAlCrearException.class, HttpStatus.BAD_REQUEST,
                        "La contrasena es obligatoria al crear un usuario"),
                caso("edadMinimaNoCumplida", LanzadorException::edadMinimaNoCumplida,
                        EdadMinimaNoCumplidaException.class, HttpStatus.BAD_REQUEST,
                        "Debes tener al menos " + Usuario.EDAD_MINIMA + " anos para registrarte"),
                caso("fechaNacimientoObligatoriaParaEstudiante", LanzadorException::fechaNacimientoObligatoriaParaEstudiante,
                        FechaNacimientoObligatoriaParaEstudianteException.class, HttpStatus.BAD_REQUEST,
                        "La fecha de nacimiento es obligatoria para un estudiante"),
                caso("debeIndicarUnaCarrera", LanzadorException::debeIndicarUnaCarrera,
                        DebeIndicarUnaCarreraException.class, HttpStatus.BAD_REQUEST,
                        "Debes indicar exactamente una carrera y universidad"),
                caso("soloUnaCarrera", LanzadorException::soloUnaCarrera,
                        SoloUnaCarreraException.class, HttpStatus.BAD_REQUEST,
                        "Solo puedes indicar una carrera y universidad"),
                caso("correoFormatoInvalido", LanzadorException::correoFormatoInvalido,
                        CorreoFormatoInvalidoException.class, HttpStatus.BAD_REQUEST,
                        "El correo no tiene un formato valido"),
                caso("carreraRepetida", () -> LanzadorException.carreraRepetida(8L),
                        CarreraRepetidaException.class, HttpStatus.BAD_REQUEST,
                        "La carrera con id 8 esta repetida"),
                caso("redSocialRepetida", () -> LanzadorException.redSocialRepetida("Instagram"),
                        RedSocialRepetidaException.class, HttpStatus.BAD_REQUEST,
                        "El tipo de red social \"Instagram\" esta repetido"),
                caso("universidadNoOfreceCarrera", () -> LanzadorException.universidadNoOfreceCarrera("Uni", "Medicina"),
                        UniversidadNoOfreceCarreraException.class, HttpStatus.BAD_REQUEST,
                        "La universidad 'Uni' no ofrece la carrera 'Medicina'"),
                caso("credencialesInvalidas", LanzadorException::credencialesInvalidas,
                        CredencialesInvalidasException.class, HttpStatus.UNAUTHORIZED,
                        "Credenciales invalidas"),
                caso("estudianteSinEstudio", LanzadorException::estudianteSinEstudio,
                        EstudianteSinEstudioException.class, null,
                        "Un estudiante necesita una carrera y una universidad"),
                caso("usuarioNoRecargado", LanzadorException::usuarioNoRecargado,
                        UsuarioNoRecargadoException.class, null,
                        "No se pudo recargar el usuario"),
                caso("usuarioAutenticadoNoEncontrado", LanzadorException::usuarioAutenticadoNoEncontrado,
                        UsuarioAutenticadoNoEncontradoException.class, null,
                        "Usuario autenticado pero no encontrado en base de datos"),
                caso("esquemaDesactualizado", LanzadorException::esquemaDesactualizado,
                        EsquemaDesactualizadoException.class, null,
                        "La base de datos todavia tiene el esquema anterior "
                    + "(faltan usuario.nombre_usuario y/o red_social.id_usuario). "
                    + "Ejecuta src/main/resources/db/migracion_nombres_atributos.sql una sola vez y vuelve a arrancar."),
                caso("usuarioDeSesionNoEncontrado", () -> LanzadorException.usuarioDeSesionNoEncontrado("ana@mail.com"),
                        UsuarioDeSesionNoEncontradoException.class, null,
                        "No existe un usuario con el correo: ana@mail.com")
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("casos")
    void cadaMetodoLanzaSuExcepcionConSuMensajeYSuCodigo(String nombre, Runnable accion,
            Class<? extends RuntimeException> tipo, HttpStatus estado, String mensaje) {
        Throwable error = catchThrowable(accion::run);

        assertThat(error).isInstanceOf(tipo);
        assertThat(error.getClass()).isEqualTo(tipo);
        if (estado != null) {
            ResponseStatusException respuesta = (ResponseStatusException) error;
            assertThat(respuesta.getStatusCode()).isEqualTo(estado);
            assertThat(respuesta.getReason()).isEqualTo(mensaje);
        } else {
            assertThat(error.getMessage()).isEqualTo(mensaje);
        }
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("casos")
    void lasClasesSoloTienenElConstructorConSuSuper(String nombre, Runnable accion,
            Class<? extends RuntimeException> tipo, HttpStatus estado, String mensaje) {
        assertThat(Arrays.stream(tipo.getDeclaredFields()).filter(f -> !f.isSynthetic()))
                .as("campos (como serialVersionUID)").isEmpty();
        assertThat(Arrays.stream(tipo.getDeclaredMethods()).filter(m -> !m.isSynthetic()))
                .as("metodos").isEmpty();
        assertThat(tipo.getDeclaredConstructors()).as("constructores").hasSize(1);
        assertThat(Modifier.isPublic(tipo.getDeclaredConstructors()[0].getModifiers())).isTrue();
    }

    @Test
    void haySoloUnaClaseDeExcepcionPorMetodoDelLanzador() {
        long metodos = Arrays.stream(LanzadorException.class.getDeclaredMethods())
                .filter(m -> Modifier.isPublic(m.getModifiers())).count();
        assertThat(casos().count()).isEqualTo(metodos);
        assertThat(casos().map(a -> a.get()[2]).distinct().count()).isEqualTo(metodos);
    }

    @Test
    void yaNoExisteUnManejadorGlobalDeExcepciones() {
        assertThatThrownBy(() -> Class.forName("co.edu.unbosque.empareja2.exception.GlobalExceptionHandler"))
                .isInstanceOf(ClassNotFoundException.class);
    }
}
