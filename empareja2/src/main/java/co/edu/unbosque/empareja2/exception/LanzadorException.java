package co.edu.unbosque.empareja2.exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import co.edu.unbosque.empareja2.entity.Usuario;

/**
 * Lugar unico donde se lanzan las excepciones de la aplicacion. Cada metodo
 * conoce el mensaje de su error y lanza la excepcion correspondiente, asi los
 * servicios, repositorios y demas clases solo llaman, por ejemplo,
 * <code>LanzadorException.ciudadNoEncontrada(id)</code> y no repiten textos ni
 * escriben <code>throw new ...</code>.
 *
 * Todos los metodos son estaticos y SIEMPRE lanzan una excepcion; por eso se
 * llaman dentro de un <code>if</code> que detecta el problema.
 *
 * Los errores se agrupan por la excepcion que lanzan:
 * <ul>
 * <li>ResourceNotFoundException (404): algo que se busca no existe.</li>
 * <li>DuplicateResourceException (409): un dato unico ya existe.</li>
 * <li>OperationNotAllowedException (409): una regla del negocio lo impide.</li>
 * <li>IllegalArgumentException (400): el cliente envio un dato invalido.</li>
 * <li>IllegalStateException (500): el sistema esta en un estado inesperado.</li>
 * </ul>
 */
public final class LanzadorException {

    /**
     * Esta clase solo tiene metodos estaticos; no se crean objetos de ella.
     */
    private LanzadorException() {
    }

    // ------------------------------------------------------------------
    // No encontrado (ResourceNotFoundException)
    // ------------------------------------------------------------------

    /**
     * Lanza el error de un usuario que no existe con ese correo.
     *
     * @param correo el correo que se busco
     * @throws ResourceNotFoundException siempre
     */
    public static void usuarioNoEncontradoPorCorreo(String correo) {
        throw new ResourceNotFoundException("No existe un usuario con correo " + correo);
    }

    /**
     * Lanza el error de un usuario que no existe con ese id.
     *
     * @param id el identificador que se busco
     * @throws ResourceNotFoundException siempre
     */
    public static void usuarioNoEncontradoPorId(Long id) {
        throw new ResourceNotFoundException("No existe un usuario con id " + id);
    }

    /**
     * Lanza el error de una ciudad que no existe.
     *
     * @param id el identificador que se busco
     * @throws ResourceNotFoundException siempre
     */
    public static void ciudadNoEncontrada(Long id) {
        throw new ResourceNotFoundException("No existe la ciudad con id " + id);
    }

    /**
     * Lanza el error de una universidad que no existe.
     *
     * @param id el identificador que se busco
     * @throws ResourceNotFoundException siempre
     */
    public static void universidadNoEncontrada(Long id) {
        throw new ResourceNotFoundException("No existe la universidad con id " + id);
    }

    /**
     * Lanza el error de una carrera que no existe.
     *
     * @param id el identificador que se busco
     * @throws ResourceNotFoundException siempre
     */
    public static void carreraNoEncontrada(Long id) {
        throw new ResourceNotFoundException("No existe la carrera con id " + id);
    }

    /**
     * Lanza el error de una oferta academica que no existe.
     *
     * @param idCarrera     el identificador de la carrera
     * @param idUniversidad el identificador de la universidad
     * @throws ResourceNotFoundException siempre
     */
    public static void ofertaNoEncontrada(Long idCarrera, Long idUniversidad) {
        throw new ResourceNotFoundException(
                "La universidad " + idUniversidad + " no ofrece la carrera " + idCarrera);
    }

    /**
     * Lanza el error de un reporte que no existe.
     *
     * @param id el identificador que se busco
     * @throws ResourceNotFoundException siempre
     */
    public static void reporteNoEncontrado(Long id) {
        throw new ResourceNotFoundException("No existe el reporte con id " + id);
    }

    /**
     * Lanza el error de un comentario que no existe.
     *
     * @param idAutor          el identificador del autor del comentario
     * @param numeroComentario el numero del comentario
     * @throws ResourceNotFoundException siempre
     */
    public static void comentarioNoEncontrado(Long idAutor, Integer numeroComentario) {
        throw new ResourceNotFoundException(
                "No existe el comentario " + numeroComentario + " del usuario " + idAutor);
    }

    /**
     * Lanza el error de una reaccion que no existe.
     *
     * @param idAutor        el identificador del autor de la reaccion
     * @param numeroReaccion el numero de la reaccion
     * @throws ResourceNotFoundException siempre
     */
    public static void reaccionNoEncontrada(Long idAutor, Integer numeroReaccion) {
        throw new ResourceNotFoundException(
                "No existe la reaccion " + numeroReaccion + " del usuario " + idAutor);
    }

    /**
     * Lanza el error de quitar un me gusta que el usuario nunca dio.
     *
     * @throws ResourceNotFoundException siempre
     */
    public static void meGustaNoEncontrado() {
        throw new ResourceNotFoundException("No le has dado me gusta a este perfil");
    }

    // ------------------------------------------------------------------
    // Datos repetidos (DuplicateResourceException)
    // ------------------------------------------------------------------

    /**
     * Lanza el error de registrarse con un correo que ya existe.
     *
     * @throws DuplicateResourceException siempre
     */
    public static void correoYaRegistrado() {
        throw new DuplicateResourceException("Ya existe un usuario registrado con ese correo");
    }

    /**
     * Lanza el error de un administrador que crea o edita un usuario con un
     * correo/usuario que ya existe.
     *
     * @throws DuplicateResourceException siempre
     */
    public static void correoUsuarioYaExiste() {
        throw new DuplicateResourceException("Ya existe un usuario con ese correo/usuario");
    }

    /**
     * Lanza el error de una ciudad repetida en el mismo departamento.
     *
     * @throws DuplicateResourceException siempre
     */
    public static void ciudadYaExiste() {
        throw new DuplicateResourceException("Ya existe esa ciudad en ese departamento");
    }

    /**
     * Lanza el error de una universidad con un nombre que ya existe.
     *
     * @throws DuplicateResourceException siempre
     */
    public static void universidadNombreYaExiste() {
        throw new DuplicateResourceException("Ya existe una universidad con ese nombre");
    }

    /**
     * Lanza el error de una universidad con una sigla que ya existe.
     *
     * @throws DuplicateResourceException siempre
     */
    public static void universidadSiglaYaExiste() {
        throw new DuplicateResourceException("Ya existe una universidad con esa sigla");
    }

    /**
     * Lanza el error de una carrera con un nombre que ya existe.
     *
     * @throws DuplicateResourceException siempre
     */
    public static void carreraYaExiste() {
        throw new DuplicateResourceException("Ya existe una carrera con ese nombre");
    }

    /**
     * Lanza el error de una oferta que ya estaba registrada.
     *
     * @throws DuplicateResourceException siempre
     */
    public static void ofertaYaExiste() {
        throw new DuplicateResourceException("Esa universidad ya ofrece esa carrera");
    }

    /**
     * Lanza el error de dar me gusta a un perfil que ya tiene el me gusta del usuario.
     *
     * @throws DuplicateResourceException siempre
     */
    public static void meGustaYaDado() {
        throw new DuplicateResourceException("Ya le diste me gusta a este perfil");
    }

    /**
     * Lanza el error de reportar otra vez un perfil por el mismo motivo mientras
     * el reporte anterior sigue sin cerrar.
     *
     * @throws DuplicateResourceException siempre
     */
    public static void reporteActivoYaExiste() {
        throw new DuplicateResourceException(
                "Ya reportaste este perfil por ese motivo y el reporte todavia esta en revision");
    }

    // ------------------------------------------------------------------
    // Operaciones no permitidas (OperationNotAllowedException)
    // ------------------------------------------------------------------

    /**
     * Lanza el error de eliminar una ciudad que todavia tiene universidades.
     *
     * @throws OperationNotAllowedException siempre
     */
    public static void ciudadConUniversidades() {
        throw new OperationNotAllowedException(
                "No se puede eliminar la ciudad: todavia tiene universidades registradas");
    }

    /**
     * Lanza el error de eliminar una universidad donde todavia estudian usuarios.
     *
     * @throws OperationNotAllowedException siempre
     */
    public static void universidadConEstudiantes() {
        throw new OperationNotAllowedException(
                "No se puede eliminar la universidad: hay usuarios que estudian en ella");
    }

    /**
     * Lanza el error de eliminar una carrera que todavia estudian usuarios.
     *
     * @throws OperationNotAllowedException siempre
     */
    public static void carreraConEstudiantes() {
        throw new OperationNotAllowedException("No se puede eliminar la carrera: hay usuarios que la estudian");
    }

    /**
     * Lanza el error de eliminar una oferta que algun estudiante esta cursando.
     *
     * @throws OperationNotAllowedException siempre
     */
    public static void ofertaConEstudiantes() {
        throw new OperationNotAllowedException(
                "No se puede eliminar la oferta: hay usuarios que cursan esa carrera en esa universidad");
    }

    /**
     * Lanza el error de quitarle el rol de administrador al unico administrador.
     *
     * @throws OperationNotAllowedException siempre
     */
    public static void ultimoAdministradorSinRol() {
        throw new OperationNotAllowedException("No se puede quitar el rol al ultimo administrador");
    }

    /**
     * Lanza el error de eliminar al unico administrador.
     *
     * @throws OperationNotAllowedException siempre
     */
    public static void ultimoAdministradorEliminado() {
        throw new OperationNotAllowedException("No se puede eliminar al ultimo administrador");
    }

    // ------------------------------------------------------------------
    // Datos invalidos (IllegalArgumentException)
    // ------------------------------------------------------------------

    /**
     * Lanza el error de enviarse un comentario a uno mismo.
     *
     * @throws IllegalArgumentException siempre
     */
    public static void comentarioAUnoMismo() {
        throw new IllegalArgumentException("No puedes enviarte comentarios a ti mismo");
    }

    /**
     * Lanza el error de darse me gusta a uno mismo.
     *
     * @throws IllegalArgumentException siempre
     */
    public static void meGustaAUnoMismo() {
        throw new IllegalArgumentException("No puedes darte me gusta a ti mismo");
    }

    /**
     * Lanza el error de reportar el propio perfil.
     *
     * @throws IllegalArgumentException siempre
     */
    public static void reporteAUnoMismo() {
        throw new IllegalArgumentException("No puedes reportar tu propio perfil");
    }

    /**
     * Lanza el error de un reporte de tipo OTRO sin descripcion.
     *
     * @throws IllegalArgumentException siempre
     */
    public static void reporteOtroSinDescripcion() {
        throw new IllegalArgumentException("Describe el motivo cuando el tipo de reporte es OTRO");
    }

    /**
     * Lanza el error de un limite de ranking fuera del rango permitido.
     *
     * @throws IllegalArgumentException siempre
     */
    public static void limiteRankingInvalido() {
        throw new IllegalArgumentException("El limite del ranking debe estar entre 1 y 100");
    }

    /**
     * Lanza el error de crear un usuario sin contrasena.
     *
     * @throws IllegalArgumentException siempre
     */
    public static void contrasenaObligatoriaAlCrear() {
        throw new IllegalArgumentException("La contrasena es obligatoria al crear un usuario");
    }

    /**
     * Lanza el error de registrarse sin cumplir la edad minima.
     *
     * @throws IllegalArgumentException siempre
     */
    public static void edadMinimaNoCumplida() {
        throw new IllegalArgumentException(
                "Debes tener al menos " + Usuario.EDAD_MINIMA + " anos para registrarte");
    }

    /**
     * Lanza el error de un estudiante sin fecha de nacimiento.
     *
     * @throws IllegalArgumentException siempre
     */
    public static void fechaNacimientoObligatoriaParaEstudiante() {
        throw new IllegalArgumentException("La fecha de nacimiento es obligatoria para un estudiante");
    }

    /**
     * Lanza el error de un estudiante que no indica exactamente una carrera con
     * su universidad.
     *
     * @throws IllegalArgumentException siempre
     */
    public static void debeIndicarUnaCarrera() {
        throw new IllegalArgumentException("Debes indicar exactamente una carrera y universidad");
    }

    /**
     * Lanza el error de indicar mas de una carrera con universidad.
     *
     * @throws IllegalArgumentException siempre
     */
    public static void soloUnaCarrera() {
        throw new IllegalArgumentException("Solo puedes indicar una carrera y universidad");
    }

    /**
     * Lanza el error de un correo con formato invalido.
     *
     * @throws IllegalArgumentException siempre
     */
    public static void correoFormatoInvalido() {
        throw new IllegalArgumentException("El correo no tiene un formato valido");
    }

    /**
     * Lanza el error de repetir una carrera en la lista de estudios.
     *
     * @param idCarrera el identificador de la carrera repetida
     * @throws IllegalArgumentException siempre
     */
    public static void carreraRepetida(Long idCarrera) {
        throw new IllegalArgumentException("La carrera con id " + idCarrera + " esta repetida");
    }

    /**
     * Lanza el error de repetir una red social en la lista de redes.
     *
     * @param tipoRedSocial el tipo de red social repetido
     * @throws IllegalArgumentException siempre
     */
    public static void redSocialRepetida(String tipoRedSocial) {
        throw new IllegalArgumentException("El tipo de red social \"" + tipoRedSocial + "\" esta repetido");
    }

    /**
     * Lanza el error de estudiar una carrera que la universidad no ofrece.
     *
     * @param nombreUniversidad el nombre de la universidad
     * @param nombreCarrera     el nombre de la carrera
     * @throws IllegalArgumentException siempre
     */
    public static void universidadNoOfreceCarrera(String nombreUniversidad, String nombreCarrera) {
        throw new IllegalArgumentException(
                "La universidad '" + nombreUniversidad + "' no ofrece la carrera '" + nombreCarrera + "'");
    }

    // ------------------------------------------------------------------
    // Estados inesperados del sistema (IllegalStateException)
    // ------------------------------------------------------------------

    /**
     * Lanza el error de guardar un estudiante sin carrera ni universidad.
     *
     * @throws IllegalStateException siempre
     */
    public static void estudianteSinEstudio() {
        throw new IllegalStateException("Un estudiante necesita una carrera y una universidad");
    }

    /**
     * Lanza el error de no poder volver a leer un usuario recien guardado.
     *
     * @throws IllegalStateException siempre
     */
    public static void usuarioNoRecargado() {
        throw new IllegalStateException("No se pudo recargar el usuario");
    }

    /**
     * Lanza el error de un usuario que se autentico pero no esta en la base de datos.
     *
     * @throws IllegalStateException siempre
     */
    public static void usuarioAutenticadoNoEncontrado() {
        throw new IllegalStateException("Usuario autenticado pero no encontrado en base de datos");
    }

    /**
     * Lanza el error de arrancar con una base de datos que todavia no tiene los
     * nombres de atributos nuevos.
     *
     * @throws IllegalStateException siempre
     */
    public static void esquemaDesactualizado() {
        throw new IllegalStateException("La base de datos todavia tiene el esquema anterior "
                + "(faltan usuario.nombre_usuario y/o red_social.id_usuario). "
                + "Ejecuta src/main/resources/db/migracion_nombres_atributos.sql una sola vez y vuelve a arrancar.");
    }

    // ------------------------------------------------------------------
    // Seguridad (Spring Security)
    // ------------------------------------------------------------------

    /**
     * Lanza el error de Spring Security cuando no existe el usuario que intenta
     * iniciar sesion.
     *
     * @param correo el correo que se busco
     * @throws UsernameNotFoundException siempre
     */
    public static void usuarioDeSesionNoEncontrado(String correo) {
        throw new UsernameNotFoundException("No existe un usuario con el correo: " + correo);
    }
}
