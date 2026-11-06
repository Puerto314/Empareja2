package co.edu.unbosque.empareja2.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import co.edu.unbosque.empareja2.TestData;
import co.edu.unbosque.empareja2.dto.AdminUsuarioRequestDTO;
import co.edu.unbosque.empareja2.dto.RegistroRequestDTO;
import co.edu.unbosque.empareja2.dto.UsuarioPerfilDTO;
import co.edu.unbosque.empareja2.dto.UsuarioPublicoDTO;
import co.edu.unbosque.empareja2.entity.Estudio;
import co.edu.unbosque.empareja2.entity.Genero;
import co.edu.unbosque.empareja2.entity.Rol;
import co.edu.unbosque.empareja2.entity.Usuario;

class UsuarioMapperTest {

    private final UsuarioMapper mapper = new UsuarioMapper();

    private Usuario usuarioCompleto() {
        Usuario u = TestData.usuario(7L, "ana@mail.com", "Ana", "Garcia");
        u.setGenero(Genero.FEMENINO);
        u.setFotoPerfil("https://img/ana.png");
        u.getIntereses().addAll(Set.of("Musica", "cine", "Anime"));
        u.getEstudios().add(new Estudio(TestData.carrera(1L, "Medicina"),
                TestData.universidad(10L, "Universidad El Bosque", "UNBOSQUE"), 5));
        u.getRedes().add(TestData.redSocial("Instagram", "ana.g"));
        return u;
    }

    @Test
    void elDtoPublicoNoExponeDatosSensibles() {
        List<String> componentes = Arrays.stream(UsuarioPublicoDTO.class.getDeclaredFields())
                .map(Field::getName).toList();

        assertThat(componentes).doesNotContain("correo", "contrasena", "contrasenaHash",
                "fechaNacimiento", "rol");
    }

    @Test
    void toPublicoDTOCopiaSoloInformacionPublica() {
        UsuarioPublicoDTO dto = mapper.toPublicoDTO(usuarioCompleto(), 3L, true);

        assertThat(dto.getId()).isEqualTo(7L);
        assertThat(dto.getNombreCompleto()).isEqualTo("Ana Garcia");
        assertThat(dto.getEdad()).isEqualTo(22);
        assertThat(dto.getGenero()).isEqualTo(Genero.FEMENINO);
        assertThat(dto.getTotalMeGusta()).isEqualTo(3L);
        assertThat(dto.isMeGustaDelUsuarioActual()).isTrue();
        assertThat(dto.getIntereses()).containsExactly("Anime", "cine", "Musica");
        assertThat(dto.getEstudios()).hasSize(1);
        assertThat(dto.getEstudios().get(0).getUniversidad()).isEqualTo("Universidad El Bosque");
        assertThat(dto.getEstudios().get(0).getSemestre()).isEqualTo(5);
        assertThat(dto.getRedes()).hasSize(1);
        assertThat(dto.getRedes().get(0).getTipoRedSocial()).isEqualTo("Instagram");
        assertThat(dto.getRedes().get(0).getUrlPerfil()).isEqualTo("https://instagram.com/ana.g");
    }

    @Test
    void toPerfilDTOIncluyeDatosPrivadosPeroNuncaElHash() {
        UsuarioPerfilDTO dto = mapper.toPerfilDTO(usuarioCompleto());

        assertThat(dto.getCorreo()).isEqualTo("ana@mail.com");
        assertThat(dto.getRol()).isEqualTo(Rol.ESTUDIANTE);
        assertThat(dto.getFechaNacimiento()).isNotNull();

        List<String> componentes = Arrays.stream(UsuarioPerfilDTO.class.getDeclaredFields())
                .map(Field::getName).toList();
        assertThat(componentes).doesNotContain("contrasena", "contrasenaHash", "tipoDocumento", "documento");
    }

    @Test
    void toEntityDeRegistroSiempreCreaEstudiante() {
        RegistroRequestDTO dto = new RegistroRequestDTO("a@b.com", "secreto123", Genero.OTRO,
                "Luis", "Perez", LocalDate.of(2000, 1, 1), "foto", Set.of("x"), List.of(), List.of());

        Usuario u = mapper.toEntity(dto, "HASH");

        assertThat(u.getRol()).isEqualTo(Rol.ESTUDIANTE);
        assertThat(u.getContrasenaHash()).isEqualTo("HASH");
        assertThat(u.getCorreo()).isEqualTo("a@b.com");
        assertThat(u.getGenero()).isEqualTo(Genero.OTRO);
        assertThat(u.getNombreCompleto()).isEqualTo("Luis Perez");
    }

    @Test
    void toEntityDeAdminRespetaElRolSolicitadoYPorDefectoEsEstudiante() {
        AdminUsuarioRequestDTO conRol = new AdminUsuarioRequestDTO("root", "secreto123", Rol.ADMIN,
                null, "Root", "Admin", null, null, null, null, null);
        AdminUsuarioRequestDTO sinRol = new AdminUsuarioRequestDTO("est", "secreto123", null,
                null, "Est", "Udiante", null, null, null, null, null);

        assertThat(mapper.toEntity(conRol, "H").getRol()).isEqualTo(Rol.ADMIN);
        assertThat(mapper.toEntity(sinRol, "H").getRol()).isEqualTo(Rol.ESTUDIANTE);
    }
}
