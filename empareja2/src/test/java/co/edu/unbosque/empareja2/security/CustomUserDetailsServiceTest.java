package co.edu.unbosque.empareja2.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import co.edu.unbosque.empareja2.TestData;
import co.edu.unbosque.empareja2.entity.Usuario;
import co.edu.unbosque.empareja2.exception.UsuarioDeSesionNoEncontradoException;
import co.edu.unbosque.empareja2.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    private CustomUserDetailsService service;

    @BeforeEach
    void preparar() {
        service = new CustomUserDetailsService(usuarioRepository);
    }

    @Test
    void cargaElCorreoLaContrasenaCifradaYElRol() {
        Usuario ana = TestData.usuario(1L, "ana@mail.com", "Ana", "Garcia");
        when(usuarioRepository.buscarPorCorreo("ana@mail.com")).thenReturn(Optional.of(ana));

        UserDetails detalles = service.loadUserByUsername("ana@mail.com");

        assertThat(detalles.getUsername()).isEqualTo("ana@mail.com");
        assertThat(detalles.getPassword()).isEqualTo("hash-1");
        assertThat(detalles.getAuthorities()).extracting(Object::toString).containsExactly("ROLE_ESTUDIANTE");
    }

    @Test
    void siElCorreoNoExisteLanzaUsuarioDeSesionNoEncontrado() {
        when(usuarioRepository.buscarPorCorreo("fantasma@mail.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.loadUserByUsername("fantasma@mail.com"))
                .isInstanceOf(UsuarioDeSesionNoEncontradoException.class)
                .isInstanceOf(UsernameNotFoundException.class).hasMessageContaining("fantasma@mail.com");
    }
}
