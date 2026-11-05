package co.edu.unbosque.empareja2.service.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.empareja2.dto.ActualizarPerfilRequestDTO;
import co.edu.unbosque.empareja2.dto.AdminUsuarioRequestDTO;
import co.edu.unbosque.empareja2.dto.ConteoReaccionesDTO;
import co.edu.unbosque.empareja2.dto.UsuarioPerfilDTO;
import co.edu.unbosque.empareja2.dto.UsuarioPublicoDTO;
import co.edu.unbosque.empareja2.entity.Rol;
import co.edu.unbosque.empareja2.entity.TipoReaccion;
import co.edu.unbosque.empareja2.entity.Usuario;
import co.edu.unbosque.empareja2.exception.LanzadorException;
import co.edu.unbosque.empareja2.mapper.UsuarioMapper;
import co.edu.unbosque.empareja2.repository.ReaccionRepository;
import co.edu.unbosque.empareja2.repository.UniversidadRepository;
import co.edu.unbosque.empareja2.repository.UsuarioRepository;
import co.edu.unbosque.empareja2.service.UsuarioService;

/**
 * Reglas de negocio del catalogo, del perfil propio y de la administracion de
 * usuarios. Los controladores nunca acceden a los repositorios directamente.
 */
@Service
@Transactional(readOnly = true)
public class UsuarioServiceImpl implements UsuarioService {

    /** Acceso a los datos de los usuarios. */
    private final UsuarioRepository usuarioRepository;

    /** Acceso a los datos de las universidades. */
    private final UniversidadRepository universidadRepository;

    /** Acceso a los datos de las reacciones. */
    private final ReaccionRepository reaccionRepository;

    /** Convierte entre usuarios y DTOs. */
    private final UsuarioMapper usuarioMapper;

    /** Arma los intereses, estudios y redes del perfil. */
    private final PerfilAssembler perfilAssembler;

    /** Busca usuarios con el error 404 si no existen. */
    private final UsuarioFinder usuarioFinder;

    /** Cifra las contrasenas. */
    private final PasswordEncoder passwordEncoder;

    /**
     * Crea el servicio.
     *
     * @param usuarioRepository     el repositorio de usuarios
     * @param universidadRepository el repositorio de universidades
     * @param reaccionRepository    el repositorio de reacciones
     * @param usuarioMapper         el convertidor de usuarios
     * @param perfilAssembler       el armador de perfiles
     * @param usuarioFinder         el buscador de usuarios
     * @param passwordEncoder       el cifrador de contrasenas
     */
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, UniversidadRepository universidadRepository,
            ReaccionRepository reaccionRepository, UsuarioMapper usuarioMapper, PerfilAssembler perfilAssembler,
            UsuarioFinder usuarioFinder, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.universidadRepository = universidadRepository;
        this.reaccionRepository = reaccionRepository;
        this.usuarioMapper = usuarioMapper;
        this.perfilAssembler = perfilAssembler;
        this.usuarioFinder = usuarioFinder;
        this.passwordEncoder = passwordEncoder;
    }

    // ------------------------------------------------------------------
    // Catalogo (informacion publica)
    // ------------------------------------------------------------------

    /**
     * Lista los demas estudiantes con su informacion publica, opcionalmente solo
     * los de una universidad.
     *
     * @param correoActual  el correo del usuario que consulta
     * @param idUniversidad la universidad a filtrar (puede ser null)
     * @return los usuarios del catalogo
     */
    @Override
    public List<UsuarioPublicoDTO> listarCatalogo(String correoActual, Long idUniversidad) {
        Usuario actual = usuarioFinder.porCorreo(correoActual);

        List<Usuario> usuarios;
        if (idUniversidad == null) {
            usuarios = usuarioRepository.listarPorRolExcepto(Rol.ESTUDIANTE, actual.getId());
        } else {
            exigirUniversidad(idUniversidad);
            usuarios = usuarioRepository.listarPorUniversidad(Rol.ESTUDIANTE, actual.getId(), idUniversidad);
        }
        return aPublicos(usuarios, actual);
    }

    /**
     * Ranking de los estudiantes con mas me gusta, opcionalmente solo los de una
     * universidad.
     *
     * @param correoActual  el correo del usuario que consulta
     * @param idUniversidad la universidad a filtrar (puede ser null)
     * @param limite        cuantos usuarios devolver (entre 1 y 100)
     * @return los usuarios del ranking
     */
    @Override
    public List<UsuarioPublicoDTO> rankingMeGusta(String correoActual, Long idUniversidad, int limite) {
        Usuario actual = usuarioFinder.porCorreo(correoActual);
        if (limite < 1 || limite > 100) {
            LanzadorException.limiteRankingInvalido();
        }
        if (idUniversidad != null) {
            exigirUniversidad(idUniversidad);
        }
        List<Usuario> ranking = usuarioRepository.rankingPorMeGusta(Rol.ESTUDIANTE, TipoReaccion.ME_GUSTA,
                idUniversidad, limite);
        return aPublicos(ranking, actual);
    }

    /**
     * Obtiene la informacion publica de un estudiante.
     *
     * @param correoActual el correo del usuario que consulta
     * @param id           el identificador del usuario consultado
     * @return la informacion publica del usuario
     */
    @Override
    public UsuarioPublicoDTO obtenerPerfilPublico(String correoActual, Long id) {
        Usuario actual = usuarioFinder.porCorreo(correoActual);
        Usuario usuario = usuarioFinder.estudianteVisible(id);
        return aPublicos(List.of(usuario), actual).get(0);
    }

    /**
     * Lanza 404 si la universidad no existe.
     *
     * @param idUniversidad el identificador de la universidad
     */
    private void exigirUniversidad(Long idUniversidad) {
        if (!universidadRepository.existePorId(idUniversidad)) {
            LanzadorException.universidadNoEncontrada(idUniversidad);
        }
    }

    /**
     * Convierte a DTO publico calculando, en consultas agrupadas, los me gusta de
     * cada perfil.
     *
     * @param usuarios los usuarios a convertir
     * @param actual   el usuario que consulta
     * @return los DTOs publicos, en el mismo orden
     */
    private List<UsuarioPublicoDTO> aPublicos(List<Usuario> usuarios, Usuario actual) {
        if (usuarios.isEmpty()) {
            return List.of();
        }
        List<Long> ids = usuarios.stream().map(Usuario::getId).toList();

        Map<Long, Long> totales = new HashMap<>();
        for (ConteoReaccionesDTO conteo : reaccionRepository.contarPorDestinatarios(ids, TipoReaccion.ME_GUSTA)) {
            totales.put(conteo.getIdUsuario(), conteo.getTotal());
        }
        Set<Long> mios = new HashSet<>(
                reaccionRepository.destinatariosReaccionados(actual.getId(), TipoReaccion.ME_GUSTA, ids));

        return usuarios.stream()
                .map(u -> usuarioMapper.toPublicoDTO(u, totales.getOrDefault(u.getId(), 0L), mios.contains(u.getId())))
                .toList();
    }

    // ------------------------------------------------------------------
    // Perfil propio
    // ------------------------------------------------------------------

    /**
     * Obtiene el perfil completo del usuario autenticado.
     *
     * @param correo el correo del usuario
     * @return su perfil completo
     */
    @Override
    public UsuarioPerfilDTO obtenerPerfilPropio(String correo) {
        return usuarioMapper.toPerfilDTO(usuarioFinder.porCorreo(correo));
    }

    /**
     * Actualiza el perfil del usuario autenticado.
     *
     * @param correo  el correo del usuario
     * @param request los datos nuevos
     * @return su perfil actualizado
     */
    @Override
    @Transactional
    public UsuarioPerfilDTO actualizarPerfilPropio(String correo, ActualizarPerfilRequestDTO request) {
        Usuario usuario = usuarioFinder.porCorreo(correo);
        perfilAssembler.validarEdadMinima(request.getFechaNacimiento());

        usuarioMapper.aplicarCamposPerfil(usuario, request);
        perfilAssembler.aplicarIntereses(usuario, request.getIntereses());
        perfilAssembler.sincronizarEstudios(usuario, request.getEstudios());
        perfilAssembler.sincronizarRedes(usuario, request.getRedes());

        return usuarioMapper.toPerfilDTO(usuarioRepository.guardar(usuario));
    }

    /**
     * Elimina la cuenta del usuario autenticado.
     *
     * @param correo el correo del usuario
     */
    @Override
    @Transactional
    public void eliminarCuentaPropia(String correo) {
        eliminarUsuario(usuarioFinder.porCorreo(correo));
    }

    // ------------------------------------------------------------------
    // Administracion
    // ------------------------------------------------------------------

    /**
     * Lista todos los usuarios con su perfil completo (solo ADMIN).
     *
     * @return los usuarios
     */
    @Override
    public List<UsuarioPerfilDTO> listarParaAdmin() {
        return usuarioRepository.listarTodos().stream().map(usuarioMapper::toPerfilDTO).toList();
    }

    /**
     * Obtiene el perfil completo de cualquier usuario (solo ADMIN).
     *
     * @param id el identificador del usuario
     * @return su perfil completo
     */
    @Override
    public UsuarioPerfilDTO obtenerParaAdmin(Long id) {
        return usuarioMapper.toPerfilDTO(usuarioFinder.porId(id));
    }

    /**
     * Crea un usuario, estudiante o administrador (solo ADMIN).
     *
     * @param request los datos del usuario
     * @return el perfil del usuario creado
     */
    @Override
    @Transactional
    public UsuarioPerfilDTO crearPorAdmin(AdminUsuarioRequestDTO request) {
        if (usuarioRepository.existePorCorreo(request.getCorreo())) {
            LanzadorException.correoUsuarioYaExiste();
        }
        if (request.getContrasena() == null || request.getContrasena().isBlank()) {
            LanzadorException.contrasenaObligatoriaAlCrear();
        }
        Rol rol = request.getRol() != null ? request.getRol() : Rol.ESTUDIANTE;
        validarSegunRol(rol, request);

        Usuario usuario = usuarioMapper.toEntity(request, passwordEncoder.encode(request.getContrasena()));
        if (rol == Rol.ESTUDIANTE) {
            aplicarColecciones(usuario, request);
        }
        return usuarioMapper.toPerfilDTO(usuarioRepository.guardar(usuario));
    }

    /**
     * Edita cualquier usuario (solo ADMIN). No se puede quitar el rol al ultimo
     * administrador.
     *
     * @param id      el identificador del usuario
     * @param request los datos nuevos
     * @return el perfil actualizado
     */
    @Override
    @Transactional
    public UsuarioPerfilDTO actualizarPorAdmin(Long id, AdminUsuarioRequestDTO request) {
        Usuario usuario = usuarioFinder.porId(id);

        if (usuarioRepository.existePorCorreoExceptoId(request.getCorreo(), id)) {
            LanzadorException.correoUsuarioYaExiste();
        }
        Rol rolFinal = request.getRol() != null ? request.getRol() : usuario.getRol();
        if (usuario.getRol() == Rol.ADMIN && rolFinal != Rol.ADMIN && esElUnicoAdministrador()) {
            LanzadorException.ultimoAdministradorSinRol();
        }
        validarSegunRol(rolFinal, request);

        usuario.setCorreo(request.getCorreo());
        usuarioMapper.aplicarCamposAdmin(usuario, request);
        usuario.setRol(rolFinal);
        if (request.getContrasena() != null && !request.getContrasena().isBlank()) {
            usuario.setContrasenaHash(passwordEncoder.encode(request.getContrasena()));
        }
        if (rolFinal == Rol.ESTUDIANTE) {
            aplicarColecciones(usuario, request);
        } else {
            // Un administrador no es un perfil del catalogo: no tiene intereses, estudios ni redes.
            usuario.getIntereses().clear();
            usuario.getEstudios().clear();
            usuario.getRedes().clear();
        }

        return usuarioMapper.toPerfilDTO(usuarioRepository.guardar(usuario));
    }

    /**
     * Elimina cualquier usuario (solo ADMIN). No se puede eliminar al ultimo
     * administrador.
     *
     * @param id el identificador del usuario
     */
    @Override
    @Transactional
    public void eliminarPorAdmin(Long id) {
        eliminarUsuario(usuarioFinder.porId(id));
    }

    /**
     * Un estudiante debe cumplir las mismas reglas que al registrarse; un
     * administrador solo la edad si la indica.
     *
     * @param rol     el rol que tendra el usuario
     * @param request los datos enviados por el administrador
     */
    private void validarSegunRol(Rol rol, AdminUsuarioRequestDTO request) {
        if (rol == Rol.ESTUDIANTE) {
            perfilAssembler.validarCorreo(request.getCorreo());
            perfilAssembler.exigirDatosDeEstudiante(request.getFechaNacimiento(), request.getEstudios());
        }
        perfilAssembler.validarEdadMinima(request.getFechaNacimiento());
    }

    /**
     * Aplica los intereses, estudios y redes enviados por el administrador.
     *
     * @param usuario el usuario a modificar
     * @param request los datos enviados por el administrador
     */
    private void aplicarColecciones(Usuario usuario, AdminUsuarioRequestDTO request) {
        perfilAssembler.aplicarIntereses(usuario, request.getIntereses());
        perfilAssembler.sincronizarEstudios(usuario, request.getEstudios());
        perfilAssembler.sincronizarRedes(usuario, request.getRedes());
    }

    /**
     * Indica si solo queda un administrador en el sistema.
     *
     * @return true si hay uno o ninguno
     */
    private boolean esElUnicoAdministrador() {
        return usuarioRepository.contarPorRol(Rol.ADMIN) <= 1;
    }

    /**
     * Elimina el usuario. Sus intereses, estudios, redes, me gusta, comentarios y
     * reportes (hechos y recibidos) se borran solos: las llaves foraneas de la base
     * de datos son ON DELETE CASCADE (ver database/empareja2.sql).
     *
     * @param usuario el usuario a eliminar
     */
    private void eliminarUsuario(Usuario usuario) {
        if (usuario.getRol() == Rol.ADMIN && esElUnicoAdministrador()) {
            LanzadorException.ultimoAdministradorEliminado();
        }
        usuarioRepository.eliminar(usuario);
    }
}
