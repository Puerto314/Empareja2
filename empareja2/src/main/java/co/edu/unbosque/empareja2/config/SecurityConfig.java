package co.edu.unbosque.empareja2.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import co.edu.unbosque.empareja2.security.CustomUserDetailsService;
import co.edu.unbosque.empareja2.security.JwtAuthenticationFilter;

/**
 * Configuracion de seguridad: sesiones sin estado con token JWT, rutas publicas,
 * rutas solo para ADMIN, cifrado de contrasenas (BCrypt) y CORS para el front.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /** Rutas que no necesitan iniciar sesion. */
    private static final String[] PUBLIC_ENDPOINTS = { "/api/auth/**", "/v3/api-docs/**", "/swagger-ui/**",
            "/swagger-ui.html" };

    /** Carga los datos de acceso de un usuario. */
    private final CustomUserDetailsService userDetailsService;

    /** Filtro que lee el token JWT de cada peticion. */
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Crea la configuracion.
     *
     * @param userDetailsService      el cargador de datos de acceso
     * @param jwtAuthenticationFilter el filtro del token JWT
     */
    public SecurityConfig(CustomUserDetailsService userDetailsService,
            JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /**
     * Define que rutas son publicas, cuales exigen rol ADMIN y cuales solo
     * necesitan iniciar sesion.
     *
     * @param http el constructor de la configuracion HTTP
     * @return la cadena de filtros de seguridad
     * @throws Exception si la configuracion falla
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()).cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth.requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/catalogos/**").permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN").anyRequest().authenticated())
                .exceptionHandling(ex -> ex.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Proveedor que valida usuario y contrasena contra la base de datos.
     *
     * @return el proveedor de autenticacion
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /**
     * Administrador de autenticacion de Spring Security.
     *
     * @param config la configuracion de autenticacion
     * @return el administrador de autenticacion
     * @throws Exception si no se puede obtener
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Cifrador de contrasenas.
     *
     * @return el cifrador BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Permite que el front (cualquier origen) llame a la API.
     *
     * @return la configuracion de CORS
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
