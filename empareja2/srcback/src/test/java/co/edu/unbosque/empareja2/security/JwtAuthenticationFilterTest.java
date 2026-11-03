package co.edu.unbosque.empareja2.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import io.jsonwebtoken.MalformedJwtException;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;
    @Mock
    private CustomUserDetailsService userDetailsService;

    private JwtAuthenticationFilter filter;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockFilterChain chain;

    private final UserDetails ana = User.builder().username("ana@mail.com").password("x")
            .authorities("ROLE_ESTUDIANTE").build();

    @BeforeEach
    void preparar() {
        SecurityContextHolder.clearContext();
        filter = new JwtAuthenticationFilter(jwtService, userDetailsService);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        chain = new MockFilterChain();
    }

    @AfterEach
    void limpiar() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void sinHeaderContinuaSinAutenticar() throws Exception {
        filter.doFilter(request, response, chain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        assertThat(chain.getRequest()).isNotNull();
    }

    @Test
    void tokenValidoAutenticaAlUsuario() throws Exception {
        request.addHeader("Authorization", "Bearer abc");
        when(jwtService.extractUsername("abc")).thenReturn("ana@mail.com");
        when(userDetailsService.loadUserByUsername("ana@mail.com")).thenReturn(ana);
        when(jwtService.isTokenValid("abc", ana)).thenReturn(true);

        filter.doFilter(request, response, chain);

        assertThat(SecurityContextHolder.getContext().getAuthentication().getName()).isEqualTo("ana@mail.com");
        assertThat(chain.getRequest()).isNotNull();
    }

    @Test
    void tokenInvalidoNoRompeLaPeticion() throws Exception {
        request.addHeader("Authorization", "Bearer basura");
        when(jwtService.extractUsername("basura")).thenThrow(new MalformedJwtException("mal"));

        filter.doFilter(request, response, chain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        assertThat(chain.getRequest()).isNotNull();
    }

    @Test
    void usuarioEliminadoNoRompeLaPeticion() throws Exception {
        request.addHeader("Authorization", "Bearer abc");
        when(jwtService.extractUsername("abc")).thenReturn("fantasma@mail.com");
        when(userDetailsService.loadUserByUsername(any())).thenThrow(new UsernameNotFoundException("no existe"));

        filter.doFilter(request, response, chain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        assertThat(chain.getRequest()).isNotNull();
    }

    @Test
    void tokenValidoPeroNoCoincideNoAutentica() throws Exception {
        request.addHeader("Authorization", "Bearer abc");
        when(jwtService.extractUsername("abc")).thenReturn("ana@mail.com");
        when(userDetailsService.loadUserByUsername("ana@mail.com")).thenReturn(ana);
        when(jwtService.isTokenValid("abc", ana)).thenReturn(false);

        filter.doFilter(request, response, chain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }
}
