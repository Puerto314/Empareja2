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
import co.edu.unbosque.empareja2.dto.ActualizarOfertaRequestDTO;
import co.edu.unbosque.empareja2.dto.CarreraRequestDTO;
import co.edu.unbosque.empareja2.dto.CiudadDTO;
import co.edu.unbosque.empareja2.dto.CiudadRequestDTO;
import co.edu.unbosque.empareja2.dto.OfreceDTO;
import co.edu.unbosque.empareja2.dto.OfreceRequestDTO;
import co.edu.unbosque.empareja2.dto.UniversidadDTO;
import co.edu.unbosque.empareja2.dto.UniversidadRequestDTO;
import co.edu.unbosque.empareja2.entity.Carrera;
import co.edu.unbosque.empareja2.entity.Ciudad;
import co.edu.unbosque.empareja2.entity.Ofrece;
import co.edu.unbosque.empareja2.entity.Universidad;
import co.edu.unbosque.empareja2.exception.CarreraConEstudiantesException;
import co.edu.unbosque.empareja2.exception.CarreraNoEncontradaException;
import co.edu.unbosque.empareja2.exception.CarreraYaExisteException;
import co.edu.unbosque.empareja2.exception.CiudadConUniversidadesException;
import co.edu.unbosque.empareja2.exception.CiudadNoEncontradaException;
import co.edu.unbosque.empareja2.exception.CiudadYaExisteException;
import co.edu.unbosque.empareja2.exception.OfertaConEstudiantesException;
import co.edu.unbosque.empareja2.exception.OfertaNoEncontradaException;
import co.edu.unbosque.empareja2.exception.OfertaYaExisteException;
import co.edu.unbosque.empareja2.exception.UniversidadConEstudiantesException;
import co.edu.unbosque.empareja2.exception.UniversidadNoEncontradaException;
import co.edu.unbosque.empareja2.exception.UniversidadNombreYaExisteException;
import co.edu.unbosque.empareja2.exception.UniversidadSiglaYaExisteException;
import co.edu.unbosque.empareja2.mapper.CatalogoMapper;
import co.edu.unbosque.empareja2.repository.CarreraRepository;
import co.edu.unbosque.empareja2.repository.CiudadRepository;
import co.edu.unbosque.empareja2.repository.OfreceRepository;
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
    private OfreceRepository ofreceRepository;

    private CatalogoServiceImpl service;

    @BeforeEach
    void preparar() {
        service = new CatalogoServiceImpl(ciudadRepository, universidadRepository, carreraRepository,
                ofreceRepository, new CatalogoMapper());
    }

    @Test
    void listarUniversidadesSinTextoTraeTodas() {
        when(universidadRepository.listarTodas())
                .thenReturn(List.of(TestData.universidad(1L, "Universidad El Bosque", "UNBOSQUE")));

        List<UniversidadDTO> lista = service.listarUniversidades("  ");

        assertThat(lista).hasSize(1);
        assertThat(lista.get(0).getSigla()).isEqualTo("UNBOSQUE");
        assertThat(lista.get(0).getCalle()).isEqualTo("Calle 1");
        assertThat(lista.get(0).getCiudad().getNombre()).isEqualTo("Bogota");
        verify(universidadRepository, never()).listarPorTexto(any());
    }

    @Test
    void listarUniversidadesConTextoBuscaSinEspacios() {
        when(universidadRepository.listarPorTexto("bosque"))
                .thenReturn(List.of(TestData.universidad(1L, "Universidad El Bosque", "UNBOSQUE")));

        assertThat(service.listarUniversidades("  bosque ")).hasSize(1);
    }

    @Test
    void crearUniversidadGuardaLaDireccionCompuesta() {
        when(ciudadRepository.buscarPorId(1L)).thenReturn(Optional.of(TestData.ciudad(1L, "Bogota")));
        when(universidadRepository.guardar(any(Universidad.class))).thenAnswer(inv -> inv.getArgument(0));

        UniversidadDTO dto = service
                .crearUniversidad(new UniversidadRequestDTO("Uni Nueva", "UN", "Av 1", "Usaquen", 1L));

        assertThat(dto.getCalle()).isEqualTo("Av 1");
        assertThat(dto.getBarrio()).isEqualTo("Usaquen");
    }

    @Test
    void crearUniversidadRechazaNombreSiglaRepetidosOCiudadInexistente() {
        UniversidadRequestDTO req = new UniversidadRequestDTO("Uni", "UN", "Calle 1", "Centro", 1L);

        when(universidadRepository.existePorNombre("Uni")).thenReturn(true);
        assertThatThrownBy(() -> service.crearUniversidad(req)).isInstanceOf(UniversidadNombreYaExisteException.class);

        when(universidadRepository.existePorNombre("Uni")).thenReturn(false);
        when(universidadRepository.existePorSigla("UN")).thenReturn(true);
        assertThatThrownBy(() -> service.crearUniversidad(req)).isInstanceOf(UniversidadSiglaYaExisteException.class);

        when(universidadRepository.existePorSigla("UN")).thenReturn(false);
        when(ciudadRepository.buscarPorId(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.crearUniversidad(req)).isInstanceOf(CiudadNoEncontradaException.class);
        verify(universidadRepository, never()).guardar(any());
    }

    @Test
    void crearCarreraRechazaRepetida() {
        when(carreraRepository.existePorNombre("Medicina")).thenReturn(true);

        assertThatThrownBy(() -> service.crearCarrera(new CarreraRequestDTO("Medicina", "Facultad de Medicina")))
                .isInstanceOf(CarreraYaExisteException.class);
    }

    @Test
    void crearCarreraGuardaLaFacultad() {
        when(carreraRepository.existePorNombre("Fisica")).thenReturn(false);
        when(carreraRepository.guardar(any(Carrera.class))).thenAnswer(inv -> inv.getArgument(0));

        var dto = service.crearCarrera(new CarreraRequestDTO(" Fisica ", " Facultad de Ciencias "));

        assertThat(dto.getNombre()).isEqualTo("Fisica");
        assertThat(dto.getFacultad()).isEqualTo("Facultad de Ciencias");
    }

    @Test
    void crearOfertaTomaLaFacultadDeLaCarrera() {
        Carrera carrera = TestData.carrera(1L, "Medicina");
        Universidad uni = TestData.universidad(10L, "Universidad El Bosque", "UNBOSQUE");
        when(carreraRepository.buscarPorId(1L)).thenReturn(Optional.of(carrera));
        when(universidadRepository.buscarPorId(10L)).thenReturn(Optional.of(uni));
        when(ofreceRepository.existePorCarreraYUniversidad(1L, 10L)).thenReturn(false);
        when(ofreceRepository.guardar(any(Ofrece.class))).thenAnswer(inv -> inv.getArgument(0));

        OfreceDTO dto = service.crearOferta(new OfreceRequestDTO(1L, 10L));

        assertThat(dto.getFacultad()).isEqualTo("Facultad de Medicina");
        assertThat(dto.getCarrera()).isEqualTo("Medicina");
        assertThat(dto.getUniversidad()).isEqualTo("Universidad El Bosque");
    }

    @Test
    void crearOfertaRepetidaOConReferenciasInexistentesFalla() {
        when(carreraRepository.buscarPorId(1L)).thenReturn(Optional.of(TestData.carrera(1L, "Medicina")));
        when(universidadRepository.buscarPorId(10L))
                .thenReturn(Optional.of(TestData.universidad(10L, "Uni", "U")));
        when(ofreceRepository.existePorCarreraYUniversidad(1L, 10L)).thenReturn(true);
        assertThatThrownBy(() -> service.crearOferta(new OfreceRequestDTO(1L, 10L)))
                .isInstanceOf(OfertaYaExisteException.class);

        assertThatThrownBy(() -> service.crearOferta(new OfreceRequestDTO(9L, 10L)))
                .isInstanceOf(CarreraNoEncontradaException.class);
        assertThatThrownBy(() -> service.crearOferta(new OfreceRequestDTO(1L, 9L)))
                .isInstanceOf(UniversidadNoEncontradaException.class);
    }

    // ---- edicion ----

    @Test
    void actualizarCiudadPermiteConservarSuPropioNombre() {
        when(ciudadRepository.buscarPorId(1L)).thenReturn(Optional.of(TestData.ciudad(1L, "Bogota")));

        CiudadDTO dto = service.actualizarCiudad(1L, new CiudadRequestDTO(" Bogota D.C. ", "Cundinamarca"));

        assertThat(dto.getNombre()).isEqualTo("Bogota D.C.");
        verify(ciudadRepository).actualizar(any(Ciudad.class));
    }

    @Test
    void actualizarCiudadRechazaDuplicadaOInexistente() {
        when(ciudadRepository.buscarPorId(1L)).thenReturn(Optional.of(TestData.ciudad(1L, "Bogota")));
        when(ciudadRepository.existePorNombreYDepartamentoExceptoId("Cali", "Valle", 1L)).thenReturn(true);

        assertThatThrownBy(() -> service.actualizarCiudad(1L, new CiudadRequestDTO("Cali", "Valle")))
                .isInstanceOf(CiudadYaExisteException.class);
        assertThatThrownBy(() -> service.actualizarCiudad(9L, new CiudadRequestDTO("Cali", "Valle")))
                .isInstanceOf(CiudadNoEncontradaException.class);
        verify(ciudadRepository, never()).actualizar(any());
    }

    @Test
    void actualizarUniversidadCambiaDireccionYCiudad() {
        when(universidadRepository.buscarPorId(10L))
                .thenReturn(Optional.of(TestData.universidad(10L, "Uni", "UN")));
        when(ciudadRepository.buscarPorId(2L)).thenReturn(Optional.of(TestData.ciudad(2L, "Medellin")));

        UniversidadDTO dto = service
                .actualizarUniversidad(10L, new UniversidadRequestDTO("Uni Nueva", "UNN", "Av 9", "Centro", 2L));

        assertThat(dto.getNombre()).isEqualTo("Uni Nueva");
        assertThat(dto.getCalle()).isEqualTo("Av 9");
        assertThat(dto.getCiudad().getNombre()).isEqualTo("Medellin");
        verify(universidadRepository).actualizar(any(Universidad.class));
    }

    @Test
    void actualizarUniversidadRechazaNombreOSiglaDeOtra() {
        when(universidadRepository.buscarPorId(10L))
                .thenReturn(Optional.of(TestData.universidad(10L, "Uni", "UN")));
        UniversidadRequestDTO req = new UniversidadRequestDTO("Otra", "OT", "Calle 1", "Centro", 1L);

        when(universidadRepository.existePorNombreExceptoId("Otra", 10L)).thenReturn(true);
        assertThatThrownBy(() -> service.actualizarUniversidad(10L, req))
                .isInstanceOf(UniversidadNombreYaExisteException.class);

        when(universidadRepository.existePorNombreExceptoId("Otra", 10L)).thenReturn(false);
        when(universidadRepository.existePorSiglaExceptoId("OT", 10L)).thenReturn(true);
        assertThatThrownBy(() -> service.actualizarUniversidad(10L, req))
                .isInstanceOf(UniversidadSiglaYaExisteException.class);
        verify(universidadRepository, never()).actualizar(any());
    }

    @Test
    void actualizarCarreraCambiaNombreYFacultad() {
        when(carreraRepository.buscarPorId(1L)).thenReturn(Optional.of(TestData.carrera(1L, "Medicina")));

        var dto = service.actualizarCarrera(1L, new CarreraRequestDTO(" Medicina General ", " Ciencias de la Salud "));

        assertThat(dto.getNombre()).isEqualTo("Medicina General");
        assertThat(dto.getFacultad()).isEqualTo("Ciencias de la Salud");
        verify(carreraRepository).actualizar(any(Carrera.class));
    }

    @Test
    void actualizarOfertaCambiaLaFacultadDeLaCarrera() {
        Ofrece oferta = new Ofrece(TestData.carrera(1L, "Medicina"),
                TestData.universidad(10L, "Uni", "UN"), "Vieja");
        when(ofreceRepository.buscarPorIds(1L, 10L)).thenReturn(Optional.of(oferta));

        OfreceDTO dto = service.actualizarOferta(1L, 10L, new ActualizarOfertaRequestDTO(" Ciencias de la Salud "));

        assertThat(dto.getFacultad()).isEqualTo("Ciencias de la Salud");
        assertThat(oferta.getCarrera().getFacultad()).isEqualTo("Ciencias de la Salud");
        verify(carreraRepository).actualizar(oferta.getCarrera());
    }

    // ---- eliminacion ----

    @Test
    void eliminarCiudadConUniversidadesDa409() {
        when(ciudadRepository.buscarPorId(1L)).thenReturn(Optional.of(TestData.ciudad(1L, "Bogota")));
        when(universidadRepository.contarPorCiudad(1L)).thenReturn(2L);

        assertThatThrownBy(() -> service.eliminarCiudad(1L)).isInstanceOf(CiudadConUniversidadesException.class);
        verify(ciudadRepository, never()).eliminar(any());
    }

    @Test
    void eliminarCiudadSinUniversidadesLaBorra() {
        when(ciudadRepository.buscarPorId(1L)).thenReturn(Optional.of(TestData.ciudad(1L, "Bogota")));

        service.eliminarCiudad(1L);

        verify(ciudadRepository).eliminar(1L);
    }

    @Test
    void eliminarUniversidadConEstudiantesDa409() {
        when(universidadRepository.buscarPorId(10L))
                .thenReturn(Optional.of(TestData.universidad(10L, "Uni", "UN")));
        when(universidadRepository.contarEstudiantes(10L)).thenReturn(3L);

        assertThatThrownBy(() -> service.eliminarUniversidad(10L)).isInstanceOf(UniversidadConEstudiantesException.class);
        verify(ofreceRepository, never()).eliminarPorUniversidad(any());
        verify(universidadRepository, never()).eliminar(any());
    }

    @Test
    void eliminarUniversidadLibreBorraTambienSuOferta() {
        when(universidadRepository.buscarPorId(10L))
                .thenReturn(Optional.of(TestData.universidad(10L, "Uni", "UN")));

        service.eliminarUniversidad(10L);

        verify(ofreceRepository).eliminarPorUniversidad(10L);
        verify(universidadRepository).eliminar(10L);
    }

    @Test
    void eliminarCarreraConEstudiantesDa409YLibreBorraSuOferta() {
        when(carreraRepository.buscarPorId(1L)).thenReturn(Optional.of(TestData.carrera(1L, "Medicina")));
        when(carreraRepository.contarEstudiantes(1L)).thenReturn(1L);
        assertThatThrownBy(() -> service.eliminarCarrera(1L)).isInstanceOf(CarreraConEstudiantesException.class);

        when(carreraRepository.contarEstudiantes(1L)).thenReturn(0L);
        service.eliminarCarrera(1L);
        verify(ofreceRepository).eliminarPorCarrera(1L);
        verify(carreraRepository).eliminar(1L);
    }

    @Test
    void eliminarOfertaConEstudiantesDa409YSiNoExisteDa404() {
        Ofrece oferta = new Ofrece(TestData.carrera(1L, "Medicina"),
                TestData.universidad(10L, "Uni", "UN"), "F");
        when(ofreceRepository.buscarPorIds(1L, 10L)).thenReturn(Optional.of(oferta));
        when(ofreceRepository.contarEstudiantes(1L, 10L)).thenReturn(2L);
        assertThatThrownBy(() -> service.eliminarOferta(1L, 10L)).isInstanceOf(OfertaConEstudiantesException.class);

        when(ofreceRepository.contarEstudiantes(1L, 10L)).thenReturn(0L);
        service.eliminarOferta(1L, 10L);
        verify(ofreceRepository).eliminar(1L, 10L);

        assertThatThrownBy(() -> service.eliminarOferta(9L, 9L)).isInstanceOf(OfertaNoEncontradaException.class);
    }

    @Test
    void obtenerPorIdInexistenteDa404() {
        assertThatThrownBy(() -> service.obtenerCiudad(9L)).isInstanceOf(CiudadNoEncontradaException.class);
        assertThatThrownBy(() -> service.obtenerUniversidad(9L)).isInstanceOf(UniversidadNoEncontradaException.class);
        assertThatThrownBy(() -> service.obtenerCarrera(9L)).isInstanceOf(CarreraNoEncontradaException.class);
    }
}
