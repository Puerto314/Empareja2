package co.edu.unbosque.empareja2.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.RecordComponent;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import co.edu.unbosque.empareja2.TestData;
import co.edu.unbosque.empareja2.dto.request.AdminUsuarioRequestDTO;
import co.edu.unbosque.empareja2.dto.request.RegistroRequestDTO;
import co.edu.unbosque.empareja2.dto.response.UsuarioPerfilDTO;
import co.edu.unbosque.empareja2.dto.response.UsuarioPublicoDTO;
import co.edu.unbosque.empareja2.entity.Estudio;
import co.edu.unbosque.empareja2.entity.Genero;
import co.edu.unbosque.empareja2.entity.Rol;
import co.edu.unbosque.empareja2.entity.Tiene;
import co.edu.unbosque.empareja2.entity.TipoDocumento;
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
        u.getRedes().add(new Tiene(TestData.redSocial(2L, "Instagram"), "https://instagram.com/ana", "ana.g"));
        return u;
    }

    @Test
    void elDtoPublicoNoExponeDatosSensibles() {
        List<String> componentes = Arrays.stream(UsuarioPublicoDTO.class.getRecordComponents())
                .map(RecordComponent::getName).toList();

        assertThat(componentes).doesNotContain("correo", "contrasena", "contrasenaHash", "tipoDocumento",
                "documento", "fechaNacimiento", "rol");
    }

    @Test
    void toPublicoDTOCopiaSoloInformacionPublica() {
        UsuarioPublicoDTO dto = mapper.toPublicoDTO(usuarioCompleto(), 3L, true);

        assertThat(dto.id()).isEqualTo(7L);
        assertThat(dto.nombreCompleto()).isEqualTo("Ana Garcia");
        assertThat(dto.edad()).isEqualTo(22);
        assertThat(dto.genero()).isEqualTo(Genero.FEMENINO);
        assertThat(dto.totalMeGusta()).isEqualTo(3L);
        assertThat(dto.meGustaDelUsuarioActual()).isTrue();
        assertThat(dto.intereses()).containsExactly("Anime", "cine", "Musica");
        assertThat(dto.estudios()).hasSize(1);
        assertThat(dto.estudios().get(0).universidad()).isEqualTo("Universidad El Bosque");
        assertThat(dto.estudios().get(0).semestre()).isEqualTo(5);
        assertThat(dto.redes()).hasSize(1);
        assertThat(dto.redes().get(0).alias()).isEqualTo("ana.g");
    }

    @Test
    void toPerfilDTOIncluyeDatosPrivadosPeroNuncaElHash() {
        UsuarioPerfilDTO dto = mapper.toPerfilDTO(usuarioCompleto());

        assertThat(dto.correo()).isEqualTo("ana@mail.com");
        assertThat(dto.documento()).isEqualTo("1007");
        assertThat(dto.tipoDocumento()).isEqualTo(TipoDocumento.CC);
        assertThat(dto.rol()).isEqualTo(Rol.ESTUDIANTE);
        assertThat(dto.fechaNacimiento()).isNotNull();

        List<String> componentes = Arrays.stream(UsuarioPerfilDTO.class.getRecordComponents())
                .map(RecordComponent::getName).toList();
        assertThat(componentes).doesNotContain("contrasena", "contrasenaHash");
    }

    @Test
    void toEntityDeRegistroSiempreCreaEstudiante() {
        RegistroRequestDTO dto = new RegistroRequestDTO("a@b.com", "secreto123", TipoDocumento.TI, "55", Genero.OTRO,
                "Luis", "Perez", LocalDate.of(2000, 1, 1), "foto", Set.of("x"), List.of(), List.of());

        Usuario u = mapper.toEntity(dto, "HASH");

        assertThat(u.getRol()).isEqualTo(Rol.ESTUDIANTE);
        assertThat(u.getContrasenaHash()).isEqualTo("HASH");
        assertThat(u.getCorreo()).isEqualTo("a@b.com");
        assertThat(u.getTipoDocumento()).isEqualTo(TipoDocumento.TI);
        assertThat(u.getDocumento()).isEqualTo("55");
        assertThat(u.getNombreCompleto()).isEqualTo("Luis Perez");
    }

    @Test
    void toEntityDeAdminRespetaElRolSolicitadoYPorDefectoEsEstudiante() {
        AdminUsuarioRequestDTO conRol = new AdminUsuarioRequestDTO("root", "secreto123", Rol.ADMIN, TipoDocumento.CC,
                "1", null, "Root", "Admin", null, null, null, null, null);
        AdminUsuarioRequestDTO sinRol = new AdminUsuarioRequestDTO("est", "secreto123", null, TipoDocumento.CC, "2",
                null, "Est", "Udiante", null, null, null, null, null);

        assertThat(mapper.toEntity(conRol, "H").getRol()).isEqualTo(Rol.ADMIN);
        assertThat(mapper.toEntity(sinRol, "H").getRol()).isEqualTo(Rol.ESTUDIANTE);
    }
}
