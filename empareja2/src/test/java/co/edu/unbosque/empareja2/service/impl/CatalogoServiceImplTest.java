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
import co.edu.unbosque.empareja2.dto.request.CarreraRequestDTO;
import co.edu.unbosque.empareja2.dto.request.OfreceRequestDTO;
import co.edu.unbosque.empareja2.dto.request.UniversidadRequestDTO;
import co.edu.unbosque.empareja2.dto.response.OfreceDTO;
import co.edu.unbosque.empareja2.dto.response.UniversidadDTO;
import co.edu.unbosque.empareja2.entity.Carrera;
import co.edu.unbosque.empareja2.entity.Ofrece;
import co.edu.unbosque.empareja2.entity.Universidad;
import co.edu.unbosque.empareja2.exception.DuplicateResourceException;
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
}
