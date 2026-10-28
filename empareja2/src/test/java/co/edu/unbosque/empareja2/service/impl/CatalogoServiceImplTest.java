package co.edu.unbosque.empareja2.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import co.edu.unbosque.empareja2.TestData;
import co.edu.unbosque.empareja2.dto.request.ActualizarOfertaRequestDTO;
import co.edu.unbosque.empareja2.dto.request.CarreraRequestDTO;
import co.edu.unbosque.empareja2.dto.request.CiudadRequestDTO;
import co.edu.unbosque.empareja2.dto.request.OfreceRequestDTO;
import co.edu.unbosque.empareja2.dto.request.RedSocialRequestDTO;
import co.edu.unbosque.empareja2.dto.request.UniversidadRequestDTO;
import co.edu.unbosque.empareja2.dto.response.CiudadDTO;
import co.edu.unbosque.empareja2.dto.response.OfreceDTO;
import co.edu.unbosque.empareja2.dto.response.UniversidadDTO;
import co.edu.unbosque.empareja2.entity.Carrera;
import co.edu.unbosque.empareja2.entity.Ciudad;
import co.edu.unbosque.empareja2.entity.Ofrece;
import co.edu.unbosque.empareja2.entity.RedSocial;
import co.edu.unbosque.empareja2.entity.Universidad;
import co.edu.unbosque.empareja2.exception.DuplicateResourceException;
import co.edu.unbosque.empareja2.exception.OperationNotAllowedException;
import co.edu.unbosque.empareja2.exception.ResourceNotFoundException;
import co.edu.unbosque.empareja2.mapper.CatalogoMapper;
import co.edu.unbosque.empareja2.repository.CarreraRepository;
import co.edu.unbosque.empareja2.repository.CiudadRepository;
import co.edu.unbosque.empareja2.repository.OfreceRepository;
import co.edu.unbosque.empareja2.repository.RedSocialRepository;
import co.edu.unbosque.empareja2.repository.UniversidadRepository;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CatalogoServiceImplTest {

    @Mock
    private CiudadRepository ciudadRepository;
    @Mock
    private UniversidadRepository universidadRepository;
    @Mock
    private CarreraRepository carreraRepository;
    @Mock
    private RedSocialRepository redSocialRepository;
    @Mock
    private OfreceRepository ofreceRepository;

    private CatalogoServiceImpl service;

    @BeforeEach
    void preparar() {
        service = new CatalogoServiceImpl(ciudadRepository, universidadRepository, carreraRepository,
                redSocialRepository, ofreceRepository, new CatalogoMapper());
    }

    @Test
    void listarUniversidadesSinTextoTraeTodas() {
        when(universidadRepository.listarTodas())
                .thenReturn(List.of(TestData.universidad(1L, "Universidad El Bosque", "UNBOSQUE")));

        List<UniversidadDTO> lista = service.listarUniversidades("  ");

        assertThat(lista).hasSize(1);
        assertThat(lista.get(0).sigla()).isEqualTo("UNBOSQUE");
        assertThat(lista.get(0).calle()).isEqualTo("Calle 1");
        assertThat(lista.get(0).ciudad().nombre()).isEqualTo("Bogota");
        verify(universidadRepository, never()).buscar(any());
    }

    @Test
    void listarUniversidadesConTextoBuscaSinEspacios() {
        when(universidadRepository.buscar("bosque"))
                .thenReturn(List.of(TestData.universidad(1L, "Universidad El Bosque", "UNBOSQUE")));

        assertThat(service.listarUniversidades("  bosque ")).hasSize(1);
    }

    @Test
    void crearUniversidadGuardaLaDireccionCompuesta() {
        when(ciudadRepository.findById(1L)).thenReturn(Optional.of(TestData.ciudad(1L, "Bogota")));
        when(universidadRepository.save(any(Universidad.class))).thenAnswer(inv -> inv.getArgument(0));

        UniversidadDTO dto = service
                .crearUniversidad(new UniversidadRequestDTO("Uni Nueva", "UN", "Av 1", "Usaquen", "9-9", 1L));

        assertThat(dto.calle()).isEqualTo("Av 1");
        assertThat(dto.barrio()).isEqualTo("Usaquen");
        assertThat(dto.numeroDireccion()).isEqualTo("9-9");
    }

    @Test
    void crearUniversidadRechazaNombreSiglaRepetidosOCiudadInexistente() {
        UniversidadRequestDTO req = new UniversidadRequestDTO("Uni", "UN", null, null, null, 1L);

        when(universidadRepository.existsByNombreIgnoreCase("Uni")).thenReturn(true);
        assertThatThrownBy(() -> service.crearUniversidad(req)).isInstanceOf(DuplicateResourceException.class);

        when(universidadRepository.existsByNombreIgnoreCase("Uni")).thenReturn(false);
        when(universidadRepository.existsBySiglaIgnoreCase("UN")).thenReturn(true);
        assertThatThrownBy(() -> service.crearUniversidad(req)).isInstanceOf(DuplicateResourceException.class);

        when(universidadRepository.existsBySiglaIgnoreCase("UN")).thenReturn(false);
        when(ciudadRepository.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.crearUniversidad(req)).isInstanceOf(ResourceNotFoundException.class);
        verify(universidadRepository, never()).save(any());
    }

    @Test
    void crearCarreraRechazaRepetida() {
        when(carreraRepository.existsByNombreIgnoreCase("Medicina")).thenReturn(true);

        assertThatThrownBy(() -> service.crearCarrera(new CarreraRequestDTO("Medicina")))
                .isInstanceOf(DuplicateResourceException.class);
    }

    @Test
    void crearOfertaRegistraLaFacultad() {
        Carrera carrera = TestData.carrera(1L, "Medicina");
        Universidad uni = TestData.universidad(10L, "Universidad El Bosque", "UNBOSQUE");
        when(carreraRepository.findById(1L)).thenReturn(Optional.of(carrera));
        when(universidadRepository.findById(10L)).thenReturn(Optional.of(uni));
        when(ofreceRepository.existsByCarreraAndUniversidad(1L, 10L)).thenReturn(false);
        when(ofreceRepository.save(any(Ofrece.class))).thenAnswer(inv -> inv.getArgument(0));

        OfreceDTO dto = service.crearOferta(new OfreceRequestDTO(1L, 10L, " Facultad de Medicina "));

        assertThat(dto.facultad()).isEqualTo("Facultad de Medicina");
        assertThat(dto.carrera()).isEqualTo("Medicina");
        assertThat(dto.universidad()).isEqualTo("Universidad El Bosque");
    }

    @Test
    void crearOfertaRepetidaOConReferenciasInexistentesFalla() {
        when(carreraRepository.findById(1L)).thenReturn(Optional.of(TestData.carrera(1L, "Medicina")));
        when(universidadRepository.findById(10L))
                .thenReturn(Optional.of(TestData.universidad(10L, "Uni", "U")));
        when(ofreceRepository.existsByCarreraAndUniversidad(1L, 10L)).thenReturn(true);
        assertThatThrownBy(() -> service.crearOferta(new OfreceRequestDTO(1L, 10L, "F")))
                .isInstanceOf(DuplicateResourceException.class);

        assertThatThrownBy(() -> service.crearOferta(new OfreceRequestDTO(9L, 10L, "F")))
                .isInstanceOf(ResourceNotFoundException.class);
        assertThatThrownBy(() -> service.crearOferta(new OfreceRequestDTO(1L, 9L, "F")))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    // ---- edicion ----

    @Test
    void actualizarCiudadPermiteConservarSuPropioNombre() {
        when(ciudadRepository.findById(1L)).thenReturn(Optional.of(TestData.ciudad(1L, "Bogota")));

        CiudadDTO dto = service.actualizarCiudad(1L, new CiudadRequestDTO(" Bogota D.C. ", "Cundinamarca"));

        assertThat(dto.nombre()).isEqualTo("Bogota D.C.");
        verify(ciudadRepository).update(any(Ciudad.class));
    }

    @Test
    void actualizarCiudadRechazaDuplicadaOInexistente() {
        when(ciudadRepository.findById(1L)).thenReturn(Optional.of(TestData.ciudad(1L, "Bogota")));
        when(ciudadRepository.existsByNombreAndDepartamentoAndIdNot("Cali", "Valle", 1L)).thenReturn(true);

        assertThatThrownBy(() -> service.actualizarCiudad(1L, new CiudadRequestDTO("Cali", "Valle")))
                .isInstanceOf(DuplicateResourceException.class);
        assertThatThrownBy(() -> service.actualizarCiudad(9L, new CiudadRequestDTO("Cali", "Valle")))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(ciudadRepository, never()).update(any());
    }

    @Test
    void actualizarUniversidadCambiaDireccionYCiudad() {
        when(universidadRepository.findById(10L))
                .thenReturn(Optional.of(TestData.universidad(10L, "Uni", "UN")));
        when(ciudadRepository.findById(2L)).thenReturn(Optional.of(TestData.ciudad(2L, "Medellin")));

        UniversidadDTO dto = service
                .actualizarUniversidad(10L, new UniversidadRequestDTO("Uni Nueva", "UNN", "Av 9", "Centro", "1-2", 2L));

        assertThat(dto.nombre()).isEqualTo("Uni Nueva");
        assertThat(dto.calle()).isEqualTo("Av 9");
        assertThat(dto.ciudad().nombre()).isEqualTo("Medellin");
        verify(universidadRepository).update(any(Universidad.class));
    }

    @Test
    void actualizarUniversidadRechazaNombreOSiglaDeOtra() {
        when(universidadRepository.findById(10L))
                .thenReturn(Optional.of(TestData.universidad(10L, "Uni", "UN")));
        UniversidadRequestDTO req = new UniversidadRequestDTO("Otra", "OT", null, null, null, 1L);

        when(universidadRepository.existsByNombreAndIdNot("Otra", 10L)).thenReturn(true);
        assertThatThrownBy(() -> service.actualizarUniversidad(10L, req))
                .isInstanceOf(DuplicateResourceException.class);

        when(universidadRepository.existsByNombreAndIdNot("Otra", 10L)).thenReturn(false);
        when(universidadRepository.existsBySiglaAndIdNot("OT", 10L)).thenReturn(true);
        assertThatThrownBy(() -> service.actualizarUniversidad(10L, req))
                .isInstanceOf(DuplicateResourceException.class);
        verify(universidadRepository, never()).update(any());
    }

    @Test
    void actualizarCarreraYRedSocial() {
        when(carreraRepository.findById(1L)).thenReturn(Optional.of(TestData.carrera(1L, "Medicina")));
        when(redSocialRepository.findById(5L)).thenReturn(Optional.of(TestData.redSocial(5L, "Instagram")));

        assertThat(service.actualizarCarrera(1L, new CarreraRequestDTO(" Medicina General ")).nombre())
                .isEqualTo("Medicina General");
        assertThat(service.actualizarRedSocial(5L, new RedSocialRequestDTO("Insta", "https://insta.com")).url())
                .isEqualTo("https://insta.com");
        verify(carreraRepository).update(any(Carrera.class));
        verify(redSocialRepository).update(any(RedSocial.class));
    }

    @Test
    void actualizarOfertaCambiaSoloLaFacultad() {
        Ofrece oferta = new Ofrece(TestData.carrera(1L, "Medicina"),
                TestData.universidad(10L, "Uni", "UN"), "Vieja");
        when(ofreceRepository.findByIds(1L, 10L)).thenReturn(Optional.of(oferta));

        OfreceDTO dto = service.actualizarOferta(1L, 10L, new ActualizarOfertaRequestDTO(" Ciencias de la Salud "));

        assertThat(dto.facultad()).isEqualTo("Ciencias de la Salud");
        verify(ofreceRepository).update(oferta);
    }

    // ---- eliminacion ----

    @Test
    void eliminarCiudadConUniversidadesDa409() {
        when(ciudadRepository.findById(1L)).thenReturn(Optional.of(TestData.ciudad(1L, "Bogota")));
        when(universidadRepository.contarPorCiudad(1L)).thenReturn(2L);

        assertThatThrownBy(() -> service.eliminarCiudad(1L)).isInstanceOf(OperationNotAllowedException.class);
        verify(ciudadRepository, never()).deleteById(any());
    }

    @Test
    void eliminarCiudadSinUniversidadesLaBorra() {
        when(ciudadRepository.findById(1L)).thenReturn(Optional.of(TestData.ciudad(1L, "Bogota")));

        service.eliminarCiudad(1L);

        verify(ciudadRepository).deleteById(1L);
    }

    @Test
    void eliminarUniversidadConEstudiantesDa409() {
        when(universidadRepository.findById(10L))
                .thenReturn(Optional.of(TestData.universidad(10L, "Uni", "UN")));
        when(universidadRepository.contarEstudiantes(10L)).thenReturn(3L);

        assertThatThrownBy(() -> service.eliminarUniversidad(10L)).isInstanceOf(OperationNotAllowedException.class);
        verify(ofreceRepository, never()).eliminarPorUniversidad(any());
        verify(universidadRepository, never()).deleteById(any());
    }

    @Test
    void eliminarUniversidadLibreBorraTambienSuOferta() {
        when(universidadRepository.findById(10L))
                .thenReturn(Optional.of(TestData.universidad(10L, "Uni", "UN")));

        service.eliminarUniversidad(10L);

        verify(ofreceRepository).eliminarPorUniversidad(10L);
        verify(universidadRepository).deleteById(10L);
    }

    @Test
    void eliminarCarreraConEstudiantesDa409YLibreBorraSuOferta() {
        when(carreraRepository.findById(1L)).thenReturn(Optional.of(TestData.carrera(1L, "Medicina")));
        when(carreraRepository.contarEstudiantes(1L)).thenReturn(1L);
        assertThatThrownBy(() -> service.eliminarCarrera(1L)).isInstanceOf(OperationNotAllowedException.class);

        when(carreraRepository.contarEstudiantes(1L)).thenReturn(0L);
        service.eliminarCarrera(1L);
        verify(ofreceRepository).eliminarPorCarrera(1L);
        verify(carreraRepository).deleteById(1L);
    }

    @Test
    void eliminarRedSocialEnUsoDa409() {
        when(redSocialRepository.findById(5L)).thenReturn(Optional.of(TestData.redSocial(5L, "Instagram")));
        when(redSocialRepository.contarUsos(5L)).thenReturn(4L);

        assertThatThrownBy(() -> service.eliminarRedSocial(5L)).isInstanceOf(OperationNotAllowedException.class);
        verify(redSocialRepository, never()).deleteById(any());

        when(redSocialRepository.contarUsos(5L)).thenReturn(0L);
        service.eliminarRedSocial(5L);
        verify(redSocialRepository).deleteById(5L);
    }

    @Test
    void eliminarOfertaConEstudiantesDa409YSiNoExisteDa404() {
        Ofrece oferta = new Ofrece(TestData.carrera(1L, "Medicina"),
                TestData.universidad(10L, "Uni", "UN"), "F");
        when(ofreceRepository.findByIds(1L, 10L)).thenReturn(Optional.of(oferta));
        when(ofreceRepository.contarEstudiantes(1L, 10L)).thenReturn(2L);
        assertThatThrownBy(() -> service.eliminarOferta(1L, 10L)).isInstanceOf(OperationNotAllowedException.class);

        when(ofreceRepository.contarEstudiantes(1L, 10L)).thenReturn(0L);
        service.eliminarOferta(1L, 10L);
        verify(ofreceRepository).delete(1L, 10L);

        assertThatThrownBy(() -> service.eliminarOferta(9L, 9L)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void obtenerPorIdInexistenteDa404() {
        assertThatThrownBy(() -> service.obtenerCiudad(9L)).isInstanceOf(ResourceNotFoundException.class);
        assertThatThrownBy(() -> service.obtenerUniversidad(9L)).isInstanceOf(ResourceNotFoundException.class);
        assertThatThrownBy(() -> service.obtenerCarrera(9L)).isInstanceOf(ResourceNotFoundException.class);
        assertThatThrownBy(() -> service.obtenerRedSocial(9L)).isInstanceOf(ResourceNotFoundException.class);
    }
}
