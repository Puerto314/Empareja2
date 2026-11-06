package co.edu.unbosque.empareja2.mapper;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Component;

import co.edu.unbosque.empareja2.dto.ActualizarPerfilRequestDTO;
import co.edu.unbosque.empareja2.dto.AdminUsuarioRequestDTO;
import co.edu.unbosque.empareja2.dto.EstudioDTO;
import co.edu.unbosque.empareja2.dto.RedPerfilDTO;
import co.edu.unbosque.empareja2.dto.RegistroRequestDTO;
import co.edu.unbosque.empareja2.dto.UsuarioPerfilDTO;
import co.edu.unbosque.empareja2.dto.UsuarioPublicoDTO;
import co.edu.unbosque.empareja2.entity.Estudio;
import co.edu.unbosque.empareja2.entity.RedSocial;
import co.edu.unbosque.empareja2.entity.Rol;
import co.edu.unbosque.empareja2.entity.Usuario;

/**
 * Traduce entre la entidad Usuario y los DTOs. Mantener esta logica separada
 * evita "filtrar" campos sensibles (como el hash de la contrasena) hacia las
 * respuestas de la API.
 *
 * Las colecciones (intereses, estudios, redes) NO se copian aqui: las aplica
 * PerfilAssembler porque requieren validar contra la base de datos.
 */
@Component
public class UsuarioMapper {

    /**
     * Crea un estudiante a partir de los datos de registro.
     *
     * @param dto            los datos enviados al registrarse
     * @param contrasenaHash la contrasena ya cifrada
     * @return el usuario nuevo con rol ESTUDIANTE
     */
    public Usuario toEntity(RegistroRequestDTO dto, String contrasenaHash) {
        Usuario usuario = new Usuario();
        usuario.setCorreo(dto.getCorreo());
        usuario.setContrasenaHash(contrasenaHash);
        usuario.setRol(Rol.ESTUDIANTE);
        usuario.setGenero(dto.getGenero());
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setFechaNacimiento(dto.getFechaNacimiento());
        usuario.setFotoPerfil(dto.getFotoPerfil());
        return usuario;
    }

    /**
     * Crea un usuario a partir de lo enviado por un administrador (el rol viene del
     * request; si no viene se usa ESTUDIANTE).
     *
     * @param dto            los datos enviados por el administrador
     * @param contrasenaHash la contrasena ya cifrada
     * @return el usuario nuevo
     */
    public Usuario toEntity(AdminUsuarioRequestDTO dto, String contrasenaHash) {
        Usuario usuario = new Usuario();
        usuario.setCorreo(dto.getCorreo());
        usuario.setContrasenaHash(contrasenaHash);
        usuario.setRol(dto.getRol() != null ? dto.getRol() : Rol.ESTUDIANTE);
        aplicarCamposAdmin(usuario, dto);
        return usuario;
    }

    /**
     * Aplica los campos escalares editables por un administrador (no toca correo,
     * rol ni contrasena).
     *
     * @param usuario el usuario a modificar
     * @param dto     los datos enviados por el administrador
     */
    public void aplicarCamposAdmin(Usuario usuario, AdminUsuarioRequestDTO dto) {
        usuario.setGenero(dto.getGenero());
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setFechaNacimiento(dto.getFechaNacimiento());
        usuario.setFotoPerfil(dto.getFotoPerfil());
    }

    /**
     * Aplica los campos escalares que un usuario puede editar de su propio perfil.
     *
     * @param usuario el usuario a modificar
     * @param dto     los datos enviados por el usuario
     */
    public void aplicarCamposPerfil(Usuario usuario, ActualizarPerfilRequestDTO dto) {
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setGenero(dto.getGenero());
        usuario.setFechaNacimiento(dto.getFechaNacimiento());
        usuario.setFotoPerfil(dto.getFotoPerfil());
    }

    /**
     * Convierte un usuario en su DTO publico (sin datos sensibles).
     *
     * @param usuario                  el usuario
     * @param totalMeGusta             cuantos me gusta ha recibido
     * @param meGustaDelUsuarioActual  si el usuario que consulta ya le dio me gusta
     * @return el DTO publico del usuario
     */
    public UsuarioPublicoDTO toPublicoDTO(Usuario usuario, long totalMeGusta, boolean meGustaDelUsuarioActual) {
        return new UsuarioPublicoDTO(usuario.getId(), usuario.getNombre(), usuario.getApellido(),
                usuario.getNombreCompleto(), usuario.getEdad(), usuario.getGenero(), usuario.getFotoPerfil(),
                interesesOrdenados(usuario), estudiosDTO(usuario), redesDTO(usuario), totalMeGusta,
                meGustaDelUsuarioActual);
    }

    /**
     * Convierte un usuario en su DTO de perfil completo (con datos privados).
     *
     * @param usuario el usuario
     * @return el DTO del perfil completo
     */
    public UsuarioPerfilDTO toPerfilDTO(Usuario usuario) {
        return new UsuarioPerfilDTO(usuario.getId(), usuario.getCorreo(), usuario.getRol(), usuario.getGenero(),
                usuario.getNombre(), usuario.getApellido(), usuario.getNombreCompleto(), usuario.getEdad(),
                usuario.getFechaNacimiento(), usuario.getFotoPerfil(), interesesOrdenados(usuario),
                estudiosDTO(usuario), redesDTO(usuario));
    }

    /**
     * Convierte un estudio en su DTO.
     *
     * @param estudio el estudio
     * @return el DTO del estudio
     */
    public EstudioDTO toEstudioDTO(Estudio estudio) {
        return new EstudioDTO(estudio.getCarrera().getIdCarrera(), estudio.getCarrera().getNombre(),
                estudio.getUniversidad().getIdUniversidad(), estudio.getUniversidad().getNombre(),
                estudio.getUniversidad().getSigla(), estudio.getSemestre());
    }

    /**
     * Convierte el perfil de una red social en su DTO.
     *
     * @param red el perfil en la red social
     * @return el DTO del perfil en la red
     */
    public RedPerfilDTO toRedPerfilDTO(RedSocial red) {
        return new RedPerfilDTO(red.getTipoRedSocial(), red.getUrlPerfil());
    }

    /**
     * Los intereses del usuario ordenados alfabeticamente sin distinguir mayusculas.
     *
     * @param usuario el usuario
     * @return los intereses ordenados
     */
    private List<String> interesesOrdenados(Usuario usuario) {
        return usuario.getIntereses().stream().sorted(String.CASE_INSENSITIVE_ORDER).toList();
    }

    /**
     * Los estudios del usuario como DTOs ordenados por universidad y carrera.
     *
     * @param usuario el usuario
     * @return los estudios ordenados
     */
    private List<EstudioDTO> estudiosDTO(Usuario usuario) {
        return usuario.getEstudios().stream().map(this::toEstudioDTO)
                .sorted(Comparator.comparing(EstudioDTO::getUniversidad, String.CASE_INSENSITIVE_ORDER)
                        .thenComparing(EstudioDTO::getCarrera, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }

    /**
     * Las redes del usuario como DTOs ordenados por tipo de red.
     *
     * @param usuario el usuario
     * @return las redes ordenadas
     */
    private List<RedPerfilDTO> redesDTO(Usuario usuario) {
        return usuario.getRedes().stream().map(this::toRedPerfilDTO)
                .sorted(Comparator.comparing(RedPerfilDTO::getTipoRedSocial, String.CASE_INSENSITIVE_ORDER)).toList();
    }
}
