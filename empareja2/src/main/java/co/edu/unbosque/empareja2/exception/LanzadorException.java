package co.edu.unbosque.empareja2.exception;

/**
 * Lugar unico desde donde se lanzan las excepciones de la aplicacion. Cada
 * metodo lanza SU propia clase de excepcion (paquete exception); el mensaje y el
 * codigo HTTP viven dentro de cada clase, asi que los servicios, repositorios y
 * demas clases solo llaman, por ejemplo,
 * <code>LanzadorException.ciudadNoEncontrada(id)</code> y no repiten textos ni
 * escriben <code>throw new ...</code>.
 *
 * Todos los metodos son estaticos y SIEMPRE lanzan una excepcion; por eso se
 * llaman dentro de un <code>if</code> que detecta el problema.
 *
 * No existe un manejador global: las excepciones de respuesta extienden
 * ResponseStatusException, de modo que Spring responde con su codigo HTTP y su
 * mensaje sin ninguna clase adicional.
 */
public final class LanzadorException {

    /**
     * Esta clase solo tiene metodos estaticos; no se crean objetos de ella.
     */
    private LanzadorException() {
    }

    // ------------------------------------------------------------------
    // No encontrado (404)
    // ------------------------------------------------------------------

    /**
     * Lanza el error de un usuario que no existe con ese correo.
     *
     * @param correo el correo que se busco
     * @throws UsuarioNoEncontradoPorCorreoException siempre
     */
    public static void usuarioNoEncontradoPorCorreo(String correo) {
        throw new UsuarioNoEncontradoPorCorreoException(correo);
    }

    /**
     * Lanza el error de un usuario que no existe con ese id.
     *
     * @param id el identificador que se busco
     * @throws UsuarioNoEncontradoPorIdException siempre
     */
    public static void usuarioNoEncontradoPorId(Long id) {
        throw new UsuarioNoEncontradoPorIdException(id);
    }

    /**
     * Lanza el error de una ciudad que no existe.
     *
     * @param id el identificador que se busco
     * @throws CiudadNoEncontradaException siempre
     */
    public static void ciudadNoEncontrada(Long id) {
        throw new CiudadNoEncontradaException(id);
    }

    /**
     * Lanza el error de una universidad que no existe.
     *
     * @param id el identificador que se busco
     * @throws UniversidadNoEncontradaException siempre
     */
    public static void universidadNoEncontrada(Long id) {
        throw new UniversidadNoEncontradaException(id);
    }

    /**
     * Lanza el error de una carrera que no existe.
     *
     * @param id el identificador que se busco
     * @throws CarreraNoEncontradaException siempre
     */
    public static void carreraNoEncontrada(Long id) {
        throw new CarreraNoEncontradaException(id);
    }

    /**
     * Lanza el error de una oferta academica que no existe.
     *
     * @param idCarrera     el identificador de la carrera
     * @param idUniversidad el identificador de la universidad
     * @throws OfertaNoEncontradaException siempre
     */
    public static void ofertaNoEncontrada(Long idCarrera, Long idUniversidad) {
        throw new OfertaNoEncontradaException(idCarrera, idUniversidad);
    }

    /**
     * Lanza el error de un reporte que no existe.
     *
     * @param id el identificador que se busco
     * @throws ReporteNoEncontradoException siempre
     */
    public static void reporteNoEncontrado(Long id) {
        throw new ReporteNoEncontradoException(id);
    }

    /**
     * Lanza el error de un comentario que no existe.
     *
     * @param idAutor          el identificador del autor del comentario
     * @param numeroComentario el numero del comentario
     * @throws ComentarioNoEncontradoException siempre
     */
    public static void comentarioNoEncontrado(Long idAutor, Integer numeroComentario) {
        throw new ComentarioNoEncontradoException(idAutor, numeroComentario);
    }

    /**
     * Lanza el error de una reaccion que no existe.
     *
     * @param idAutor        el identificador del autor de la reaccion
     * @param numeroReaccion el numero de la reaccion
     * @throws ReaccionNoEncontradaException siempre
     */
    public static void reaccionNoEncontrada(Long idAutor, Integer numeroReaccion) {
        throw new ReaccionNoEncontradaException(idAutor, numeroReaccion);
    }

    /**
     * Lanza el error de quitar un me gusta que el usuario nunca dio.
     *
     * @throws MeGustaNoEncontradoException siempre
     */
    public static void meGustaNoEncontrado() {
        throw new MeGustaNoEncontradoException();
    }

    // ------------------------------------------------------------------
    // Datos repetidos (409)
    // ------------------------------------------------------------------

    /**
     * Lanza el error de registrarse con un correo que ya existe.
     *
     * @throws CorreoYaRegistradoException siempre
     */
    public static void correoYaRegistrado() {
        throw new CorreoYaRegistradoException();
    }

    /**
     * Lanza el error de un administrador que crea o edita un usuario con un
     * correo/usuario que ya existe.
     *
     * @throws CorreoUsuarioYaExisteException siempre
     */
    public static void correoUsuarioYaExiste() {
        throw new CorreoUsuarioYaExisteException();
    }

    /**
     * Lanza el error de una ciudad repetida en el mismo departamento.
     *
     * @throws CiudadYaExisteException siempre
     */
    public static void ciudadYaExiste() {
        throw new CiudadYaExisteException();
    }

    /**
     * Lanza el error de una universidad con un nombre que ya existe.
     *
     * @throws UniversidadNombreYaExisteException siempre
     */
    public static void universidadNombreYaExiste() {
        throw new UniversidadNombreYaExisteException();
    }

    /**
     * Lanza el error de una universidad con una sigla que ya existe.
     *
     * @throws UniversidadSiglaYaExisteException siempre
     */
    public static void universidadSiglaYaExiste() {
        throw new UniversidadSiglaYaExisteException();
    }

    /**
     * Lanza el error de una carrera con un nombre que ya existe.
     *
     * @throws CarreraYaExisteException siempre
     */
    public static void carreraYaExiste() {
        throw new CarreraYaExisteException();
    }

    /**
     * Lanza el error de una oferta que ya estaba registrada.
     *
     * @throws OfertaYaExisteException siempre
     */
    public static void ofertaYaExiste() {
        throw new OfertaYaExisteException();
    }

    /**
     * Lanza el error de dar me gusta a un perfil que ya tiene el me gusta del
     * usuario.
     *
     * @throws MeGustaYaDadoException siempre
     */
    public static void meGustaYaDado() {
        throw new MeGustaYaDadoException();
    }

    /**
     * Lanza el error de reportar otra vez un perfil por el mismo motivo
     * mientras el reporte anterior sigue sin cerrar.
     *
     * @throws ReporteActivoYaExisteException siempre
     */
    public static void reporteActivoYaExiste() {
        throw new ReporteActivoYaExisteException();
    }

    // ------------------------------------------------------------------
    // Operaciones no permitidas (409)
    // ------------------------------------------------------------------

    /**
     * Lanza el error de eliminar una ciudad que todavia tiene universidades.
     *
     * @throws CiudadConUniversidadesException siempre
     */
    public static void ciudadConUniversidades() {
        throw new CiudadConUniversidadesException();
    }

    /**
     * Lanza el error de eliminar una universidad donde todavia estudian
     * usuarios.
     *
     * @throws UniversidadConEstudiantesException siempre
     */
    public static void universidadConEstudiantes() {
        throw new UniversidadConEstudiantesException();
    }

    /**
     * Lanza el error de eliminar una carrera que todavia estudian usuarios.
     *
     * @throws CarreraConEstudiantesException siempre
     */
    public static void carreraConEstudiantes() {
        throw new CarreraConEstudiantesException();
    }

    /**
     * Lanza el error de eliminar una oferta que algun estudiante esta
     * cursando.
     *
     * @throws OfertaConEstudiantesException siempre
     */
    public static void ofertaConEstudiantes() {
        throw new OfertaConEstudiantesException();
    }

    /**
     * Lanza el error de quitarle el rol de administrador al unico
     * administrador.
     *
     * @throws UltimoAdministradorSinRolException siempre
     */
    public static void ultimoAdministradorSinRol() {
        throw new UltimoAdministradorSinRolException();
    }

    /**
     * Lanza el error de eliminar al unico administrador.
     *
     * @throws UltimoAdministradorEliminadoException siempre
     */
    public static void ultimoAdministradorEliminado() {
        throw new UltimoAdministradorEliminadoException();
    }

    // ------------------------------------------------------------------
    // Datos invalidos (400)
    // ------------------------------------------------------------------

    /**
     * Lanza el error de enviarse un comentario a uno mismo.
     *
     * @throws ComentarioAUnoMismoException siempre
     */
    public static void comentarioAUnoMismo() {
        throw new ComentarioAUnoMismoException();
    }

    /**
     * Lanza el error de darse me gusta a uno mismo.
     *
     * @throws MeGustaAUnoMismoException siempre
     */
    public static void meGustaAUnoMismo() {
        throw new MeGustaAUnoMismoException();
    }

    /**
     * Lanza el error de reportar el propio perfil.
     *
     * @throws ReporteAUnoMismoException siempre
     */
    public static void reporteAUnoMismo() {
        throw new ReporteAUnoMismoException();
    }

    /**
     * Lanza el error de un reporte de tipo OTRO sin descripcion.
     *
     * @throws ReporteOtroSinDescripcionException siempre
     */
    public static void reporteOtroSinDescripcion() {
        throw new ReporteOtroSinDescripcionException();
    }

    /**
     * Lanza el error de un limite de ranking fuera del rango permitido.
     *
     * @throws LimiteRankingInvalidoException siempre
     */
    public static void limiteRankingInvalido() {
        throw new LimiteRankingInvalidoException();
    }

    /**
     * Lanza el error de crear un usuario sin contrasena.
     *
     * @throws ContrasenaObligatoriaAlCrearException siempre
     */
    public static void contrasenaObligatoriaAlCrear() {
        throw new ContrasenaObligatoriaAlCrearException();
    }

    /**
     * Lanza el error de registrarse sin cumplir la edad minima.
     *
     * @throws EdadMinimaNoCumplidaException siempre
     */
    public static void edadMinimaNoCumplida() {
        throw new EdadMinimaNoCumplidaException();
    }

    /**
     * Lanza el error de un estudiante sin fecha de nacimiento.
     *
     * @throws FechaNacimientoObligatoriaParaEstudianteException siempre
     */
    public static void fechaNacimientoObligatoriaParaEstudiante() {
        throw new FechaNacimientoObligatoriaParaEstudianteException();
    }

    /**
     * Lanza el error de un estudiante que no indica exactamente una carrera
     * con su universidad.
     *
     * @throws DebeIndicarUnaCarreraException siempre
     */
    public static void debeIndicarUnaCarrera() {
        throw new DebeIndicarUnaCarreraException();
    }

    /**
     * Lanza el error de indicar mas de una carrera con universidad.
     *
     * @throws SoloUnaCarreraException siempre
     */
    public static void soloUnaCarrera() {
        throw new SoloUnaCarreraException();
    }

    /**
     * Lanza el error de un correo con formato invalido.
     *
     * @throws CorreoFormatoInvalidoException siempre
     */
    public static void correoFormatoInvalido() {
        throw new CorreoFormatoInvalidoException();
    }

    /**
     * Lanza el error de repetir una carrera en la lista de estudios.
     *
     * @param idCarrera el identificador de la carrera repetida
     * @throws CarreraRepetidaException siempre
     */
    public static void carreraRepetida(Long idCarrera) {
        throw new CarreraRepetidaException(idCarrera);
    }

    /**
     * Lanza el error de repetir una red social en la lista de redes.
     *
     * @param tipoRedSocial el tipo de red social repetido
     * @throws RedSocialRepetidaException siempre
     */
    public static void redSocialRepetida(String tipoRedSocial) {
        throw new RedSocialRepetidaException(tipoRedSocial);
    }

    /**
     * Lanza el error de estudiar una carrera que la universidad no ofrece.
     *
     * @param nombreUniversidad el nombre de la universidad
     * @param nombreCarrera     el nombre de la carrera
     * @throws UniversidadNoOfreceCarreraException siempre
     */
    public static void universidadNoOfreceCarrera(String nombreUniversidad, String nombreCarrera) {
        throw new UniversidadNoOfreceCarreraException(nombreUniversidad, nombreCarrera);
    }

    // ------------------------------------------------------------------
    // Estados inesperados del sistema (500)
    // ------------------------------------------------------------------

    /**
     * Lanza el error de guardar un estudiante sin carrera ni universidad.
     *
     * @throws EstudianteSinEstudioException siempre
     */
    public static void estudianteSinEstudio() {
        throw new EstudianteSinEstudioException();
    }

    /**
     * Lanza el error de no poder volver a leer un usuario recien guardado.
     *
     * @throws UsuarioNoRecargadoException siempre
     */
    public static void usuarioNoRecargado() {
        throw new UsuarioNoRecargadoException();
    }

    /**
     * Lanza el error de un usuario que se autentico pero no esta en la base de
     * datos.
     *
     * @throws UsuarioAutenticadoNoEncontradoException siempre
     */
    public static void usuarioAutenticadoNoEncontrado() {
        throw new UsuarioAutenticadoNoEncontradoException();
    }

    /**
     * Lanza el error de arrancar con una base de datos que todavia no tiene
     * los nombres de atributos nuevos.
     *
     * @throws EsquemaDesactualizadoException siempre
     */
    public static void esquemaDesactualizado() {
        throw new EsquemaDesactualizadoException();
    }

    // ------------------------------------------------------------------
    // Seguridad (Spring Security)
    // ------------------------------------------------------------------

    /**
     * Lanza el error de Spring Security cuando no existe el usuario que
     * intenta iniciar sesion.
     *
     * @param correo el correo que se busco
     * @throws UsuarioDeSesionNoEncontradoException siempre
     */
    public static void usuarioDeSesionNoEncontrado(String correo) {
        throw new UsuarioDeSesionNoEncontradoException(correo);
    }

    /**
     * Lanza el error de iniciar sesion con un correo o una contrasena
     * incorrectos.
     *
     * @throws CredencialesInvalidasException siempre
     */
    public static void credencialesInvalidas() {
        throw new CredencialesInvalidasException();
    }
}
