package co.edu.unbosque.empareja2.service.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.empareja2.dto.request.ActualizarPerfilRequestDTO;
import co.edu.unbosque.empareja2.dto.request.AdminUsuarioRequestDTO;
import co.edu.unbosque.empareja2.dto.response.UsuarioPerfilDTO;
import co.edu.unbosque.empareja2.dto.response.UsuarioPublicoDTO;
import co.edu.unbosque.empareja2.entity.Rol;
import co.edu.unbosque.empareja2.entity.TipoReaccion;
import co.edu.unbosque.empareja2.entity.Usuario;
import co.edu.unbosque.empareja2.exception.DuplicateResourceException;
import co.edu.unbosque.empareja2.exception.ResourceNotFoundException;
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

    private final UsuarioRepository usuarioRepository;
    private final UniversidadRepository universidadRepository;
    private final ReaccionRepository reaccionRepository;
    private final UsuarioMapper usuarioMapper;
    private final PerfilAssembler perfilAssembler;
    private final UsuarioFinder usuarioFinder;
    private final PasswordEncoder passwordEncoder;

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

    @Override
    public List<UsuarioPublicoDTO> listarCatalogo(String correoActual, Long idUniversidad) {
        Usuario actual = usuarioFinder.porCorreo(correoActual);

        List<Usuario> usuarios;
        if (idUniversidad == null) {
            usuarios = usuarioRepository.findByRolAndIdNotOrderByNombreAscApellidoAsc(Rol.ESTUDIANTE, actual.getId());
        } else {
            if (!universidadRepository.existsById(idUniversidad)) {
                throw new ResourceNotFoundException("No existe la universidad con id " + idUniversidad);
            }
            usuarios = usuarioRepository.buscarPorUniversidad(Rol.ESTUDIANTE, actual.getId(), idUniversidad);
        }
        return aPublicos(usuarios, actual);
    }

    @Override
    public UsuarioPublicoDTO obtenerPerfilPublico(String correoActual, Long id) {
        Usuario actual = usuarioFinder.porCorreo(correoActual);
        Usuario usuario = usuarioFinder.estudianteVisible(id);
        return aPublicos(List.of(usuario), actual).get(0);
    }

    /** Convierte a DTO publico calculando, en consultas agrupadas, los me gusta de cada perfil. */
    private List<UsuarioPublicoDTO> aPublicos(List<Usuario> usuarios, Usuario actual) {
        if (usuarios.isEmpty()) {
            return List.of();
        }
        List<Long> ids = usuarios.stream().map(Usuario::getId).toList();

        Map<Long, Long> totales = new HashMap<>();
        reaccionRepository.contarPorDestinatarios(ids, TipoReaccion.ME_GUSTA)
                .forEach(c -> totales.put(c.idUsuario(), c.total()));
        Set<Long> mios = new HashSet<>(
                reaccionRepository.destinatariosReaccionados(actual.getId(), TipoReaccion.ME_GUSTA, ids));

        return usuarios.stream()
                .map(u -> usuarioMapper.toPublicoDTO(u, totales.getOrDefault(u.getId(), 0L), mios.contains(u.getId())))
                .toList();
    }

    // ------------------------------------------------------------------
    // Perfil propio
    // ------------------------------------------------------------------

    @Override
    public UsuarioPerfilDTO obtenerPerfilPropio(String correo) {
        return usuarioMapper.toPerfilDTO(usuarioFinder.porCorreo(correo));
    }

    @Override
    @Transactional
    public UsuarioPerfilDTO actualizarPerfilPropio(String correo, ActualizarPerfilRequestDTO request) {
        Usuario usuario = usuarioFinder.porCorreo(correo);
        perfilAssembler.validarEdadMinima(request.fechaNacimiento());

        usuarioMapper.aplicarCamposPerfil(usuario, request);
        perfilAssembler.aplicarIntereses(usuario, request.intereses());
        perfilAssembler.sincronizarEstudios(usuario, request.estudios());
        perfilAssembler.sincronizarRedes(usuario, request.redes());

        return usuarioMapper.toPerfilDTO(usuarioRepository.save(usuario));
    }

    @Override
    @Transactional
    public void eliminarCuentaPropia(String correo) {
        eliminarUsuario(usuarioFinder.porCorreo(correo));
    }

    // ------------------------------------------------------------------
    // Administracion
    // ------------------------------------------------------------------

    @Override
    public List<UsuarioPerfilDTO> listarParaAdmin() {
        return usuarioRepository.findAll().stream().map(usuarioMapper::toPerfilDTO).toList();
    }

    @Override
    public UsuarioPerfilDTO obtenerParaAdmin(Long id) {
        return usuarioMapper.toPerfilDTO(usuarioFinder.porId(id));
    }

    @Override
    @Transactional
    public UsuarioPerfilDTO crearPorAdmin(AdminUsuarioRequestDTO request) {
        if (usuarioRepository.existsByCorreo(request.correo())) {
            throw new DuplicateResourceException("Ya existe un usuario con ese correo/usuario");
        }
        if (usuarioRepository.existsByTipoDocumentoAndDocumento(request.tipoDocumento(), request.documento())) {
            throw new DuplicateResourceException("Ya existe un usuario con ese documento");
        }
        if (request.contrasena() == null || request.contrasena().isBlank()) {
            throw new IllegalArgumentException("La contrasena es obligatoria al crear un usuario");
        }

        Usuario usuario = usuarioMapper.toEntity(request, passwordEncoder.encode(request.contrasena()));
        perfilAssembler.aplicarIntereses(usuario, request.intereses());
        perfilAssembler.sincronizarEstudios(usuario, request.estudios());
        perfilAssembler.sincronizarRedes(usuario, request.redes());

        return usuarioMapper.toPerfilDTO(usuarioRepository.save(usuario));
    }

    @Override
    @Transactional
    public UsuarioPerfilDTO actualizarPorAdmin(Long id, AdminUsuarioRequestDTO request) {
        Usuario usuario = usuarioFinder.porId(id);

        if (!usuario.getCorreo().equals(request.correo()) && usuarioRepository.existsByCorreo(request.correo())) {
            throw new DuplicateResourceException("Ya existe un usuario con ese correo/usuario");
        }
        boolean cambiaDocumento = usuario.getTipoDocumento() != request.tipoDocumento()
                || !usuario.getDocumento().equals(request.documento());
        if (cambiaDocumento
                && usuarioRepository.existsByTipoDocumentoAndDocumento(request.tipoDocumento(), request.documento())) {
            throw new DuplicateResourceException("Ya existe un usuario con ese documento");
        }

        usuario.setCorreo(request.correo());
        usuarioMapper.aplicarCamposAdmin(usuario, request);
        if (request.rol() != null) {
            usuario.setRol(request.rol());
        }
        if (request.contrasena() != null && !request.contrasena().isBlank()) {
            usuario.setContrasenaHash(passwordEncoder.encode(request.contrasena()));
        }
        perfilAssembler.aplicarIntereses(usuario, request.intereses());
        perfilAssembler.sincronizarEstudios(usuario, request.estudios());
        perfilAssembler.sincronizarRedes(usuario, request.redes());

        return usuarioMapper.toPerfilDTO(usuarioRepository.save(usuario));
    }

    @Override
    @Transactional
    public void eliminarPorAdmin(Long id) {
        eliminarUsuario(usuarioFinder.porId(id));
    }

    /**
     * Elimina el usuario. Sus intereses, estudios, redes, me gusta, comentarios y
     * reportes (hechos y recibidos) se borran solos: las llaves foraneas de la base
     * de datos son ON DELETE CASCADE (ver database/empareja2.sql).
     */
    private void eliminarUsuario(Usuario usuario) {
        usuarioRepository.delete(usuario);
    }
}
