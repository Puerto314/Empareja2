package co.edu.unbosque.empareja2.mapper;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Component;

import co.edu.unbosque.empareja2.dto.request.ActualizarPerfilRequestDTO;
import co.edu.unbosque.empareja2.dto.request.AdminUsuarioRequestDTO;
import co.edu.unbosque.empareja2.dto.request.RegistroRequestDTO;
import co.edu.unbosque.empareja2.dto.response.EstudioDTO;
import co.edu.unbosque.empareja2.dto.response.RedPerfilDTO;
import co.edu.unbosque.empareja2.dto.response.UsuarioPerfilDTO;
import co.edu.unbosque.empareja2.dto.response.UsuarioPublicoDTO;
import co.edu.unbosque.empareja2.entity.Estudio;
import co.edu.unbosque.empareja2.entity.Rol;
import co.edu.unbosque.empareja2.entity.Tiene;
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

    public Usuario toEntity(RegistroRequestDTO dto, String contrasenaHash) {
        Usuario usuario = new Usuario();
        usuario.setCorreo(dto.correo());
        usuario.setContrasenaHash(contrasenaHash);
        usuario.setRol(Rol.ESTUDIANTE);
        usuario.setTipoDocumento(dto.tipoDocumento());
        usuario.setDocumento(dto.documento());
        usuario.setGenero(dto.genero());
        usuario.setNombre(dto.nombre());
        usuario.setApellido(dto.apellido());
        usuario.setFechaNacimiento(dto.fechaNacimiento());
        usuario.setFotoPerfil(dto.fotoPerfil());
        return usuario;
    }

    /** Crea un usuario a partir de lo enviado por un administrador (el rol viene del request). */
    public Usuario toEntity(AdminUsuarioRequestDTO dto, String contrasenaHash) {
        Usuario usuario = new Usuario();
        usuario.setCorreo(dto.correo());
        usuario.setContrasenaHash(contrasenaHash);
        usuario.setRol(dto.rol() != null ? dto.rol() : Rol.ESTUDIANTE);
        aplicarCamposAdmin(usuario, dto);
        return usuario;
    }

    /** Aplica los campos escalares editables por un administrador (no toca correo, rol ni contrasena). */
    public void aplicarCamposAdmin(Usuario usuario, AdminUsuarioRequestDTO dto) {
        usuario.setTipoDocumento(dto.tipoDocumento());
        usuario.setDocumento(dto.documento());
        usuario.setGenero(dto.genero());
        usuario.setNombre(dto.nombre());
        usuario.setApellido(dto.apellido());
        usuario.setFechaNacimiento(dto.fechaNacimiento());
        usuario.setFotoPerfil(dto.fotoPerfil());
    }

    /** Aplica los campos escalares que un usuario puede editar de su propio perfil. */
    public void aplicarCamposPerfil(Usuario usuario, ActualizarPerfilRequestDTO dto) {
        usuario.setNombre(dto.nombre());
        usuario.setApellido(dto.apellido());
        usuario.setGenero(dto.genero());
        usuario.setFechaNacimiento(dto.fechaNacimiento());
        usuario.setFotoPerfil(dto.fotoPerfil());
    }

    public UsuarioPublicoDTO toPublicoDTO(Usuario usuario, long totalMeGusta, boolean meGustaDelUsuarioActual) {
        return new UsuarioPublicoDTO(usuario.getId(), usuario.getNombre(), usuario.getApellido(),
                usuario.getNombreCompleto(), usuario.getEdad(), usuario.getGenero(), usuario.getFotoPerfil(),
                interesesOrdenados(usuario), estudiosDTO(usuario), redesDTO(usuario), totalMeGusta,
                meGustaDelUsuarioActual);
    }

    public UsuarioPerfilDTO toPerfilDTO(Usuario usuario) {
        return new UsuarioPerfilDTO(usuario.getId(), usuario.getTipoDocumento(), usuario.getDocumento(),
                usuario.getCorreo(), usuario.getRol(), usuario.getGenero(), usuario.getNombre(),
                usuario.getApellido(), usuario.getNombreCompleto(), usuario.getEdad(),
                usuario.getFechaNacimiento(), usuario.getFotoPerfil(), interesesOrdenados(usuario),
                estudiosDTO(usuario), redesDTO(usuario), usuario.getFechaCreacion());
    }

    public EstudioDTO toEstudioDTO(Estudio estudio) {
        return new EstudioDTO(estudio.getCarrera().getIdCarrera(), estudio.getCarrera().getNombre(),
                estudio.getUniversidad().getIdUniversidad(), estudio.getUniversidad().getNombre(),
                estudio.getUniversidad().getSigla(), estudio.getSemestre());
    }

    public RedPerfilDTO toRedPerfilDTO(Tiene tiene) {
        return new RedPerfilDTO(tiene.getRedSocial().getIdRedSocial(), tiene.getRedSocial().getNombreRedSocial(),
                tiene.getUrlPerfil(), tiene.getAlias());
    }

    private List<String> interesesOrdenados(Usuario usuario) {
        return usuario.getIntereses().stream().sorted(String.CASE_INSENSITIVE_ORDER).toList();
    }

    private List<EstudioDTO> estudiosDTO(Usuario usuario) {
        return usuario.getEstudios().stream().map(this::toEstudioDTO)
                .sorted(Comparator.comparing(EstudioDTO::universidad, String.CASE_INSENSITIVE_ORDER)
                        .thenComparing(EstudioDTO::carrera, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }

    private List<RedPerfilDTO> redesDTO(Usuario usuario) {
        return usuario.getRedes().stream().map(this::toRedPerfilDTO)
                .sorted(Comparator.comparing(RedPerfilDTO::redSocial, String.CASE_INSENSITIVE_ORDER)).toList();
    }
}
