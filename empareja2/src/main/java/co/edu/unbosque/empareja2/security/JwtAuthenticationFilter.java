package co.edu.unbosque.empareja2.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filtro ejecutado una vez por peticion. Busca el header "Authorization: Bearer
 * token", lo valida y, si es correcto, autentica al usuario para esa
 * peticion. Un token invalido, vencido o de un usuario que ya no existe NO
 * rompe la peticion: simplemente queda sin autenticar (y Spring Security
 * respondera 401 si el endpoint lo exige).
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /** Nombre del header donde viaja el token. */
    private static final String HEADER_NAME = "Authorization";

    /** Texto con el que empieza el valor del header. */
    private static final String BEARER_PREFIX = "Bearer ";

    /** Lee y valida los tokens JWT. */
    private final JwtService jwtService;

    /** Carga los datos de acceso de un usuario. */
    private final CustomUserDetailsService userDetailsService;

    /**
     * Crea el filtro.
     *
     * @param jwtService         el servicio de tokens JWT
     * @param userDetailsService el cargador de datos de acceso
     */
    public JwtAuthenticationFilter(JwtService jwtService, CustomUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Autentica la peticion si trae un token valido y continua con la cadena de
     * filtros.
     *
     * @param request     la peticion recibida
     * @param response    la respuesta que se enviara
     * @param filterChain la cadena de filtros
     * @throws ServletException si falla algun filtro
     * @throws IOException      si falla la lectura o escritura
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader(HEADER_NAME);

        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)
                && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                String jwt = authHeader.substring(BEARER_PREFIX.length());
                String correo = jwtService.extractUsername(jwt);

                if (correo != null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(correo);

                    if (jwtService.isTokenValid(jwt, userDetails)) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            } catch (JwtException | IllegalArgumentException | UsernameNotFoundException ex) {
                logger.debug("Token JWT rechazado: " + ex.getMessage());
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }
}
