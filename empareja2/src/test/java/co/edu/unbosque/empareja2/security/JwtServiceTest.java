package co.edu.unbosque.empareja2.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;

class JwtServiceTest {

    private static final String SECRET = "Y2xhdmUtZGUtcHJ1ZWJhLXBhcmEtZW1wYXJlamEyLWNvbi1tYXMtZGUtMzItYnl0ZXM=";

    private JwtService jwtService;
    private final UserDetails ana = User.builder().username("ana@mail.com").password("x")
            .authorities("ROLE_ESTUDIANTE").build();

    @BeforeEach
    void preparar() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secret", SECRET);
        ReflectionTestUtils.setField(jwtService, "expirationMs", 60_000L);
    }

    @Test
    void generaUnTokenDelQueSePuedeExtraerElUsuario() {
        String token = jwtService.generateToken(ana);

        assertThat(jwtService.extractUsername(token)).isEqualTo("ana@mail.com");
        assertThat(jwtService.isTokenValid(token, ana)).isTrue();
    }

    @Test
    void unTokenNoEsValidoParaOtroUsuario() {
        String token = jwtService.generateToken(ana);
        UserDetails otro = User.builder().username("luis@mail.com").password("x").authorities("ROLE_ESTUDIANTE")
                .build();

        assertThat(jwtService.isTokenValid(token, otro)).isFalse();
    }

    @Test
    void unTokenVencidoSeRechaza() {
        ReflectionTestUtils.setField(jwtService, "expirationMs", -1_000L);
        String token = jwtService.generateToken(ana);

        assertThatThrownBy(() -> jwtService.isTokenValid(token, ana)).isInstanceOf(ExpiredJwtException.class);
    }

    @Test
    void unTokenManipuladoSeRechaza() {
        String token = jwtService.generateToken(ana);
        String manipulado = token.substring(0, token.length() - 3) + (token.endsWith("AAA") ? "BBB" : "AAA");

        assertThatThrownBy(() -> jwtService.extractUsername(manipulado)).isInstanceOf(JwtException.class);
    }

    @Test
    void exponeLaExpiracionConfigurada() {
        assertThat(jwtService.getExpirationMs()).isEqualTo(60_000L);
    }
}
