package co.edu.unbosque.empareja2.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * Genera y valida los tokens JWT con los que los usuarios se autentican. El
 * secreto y la vigencia salen de application.properties (app.jwt.secret y
 * app.jwt.expiration-ms).
 */
@Service
public class JwtService {

    /** Secreto (en Base64) con el que se firman los tokens. */
    @Value("${app.jwt.secret}")
    private String secret;

    /** Milisegundos de vigencia de un token. */
    @Value("${app.jwt.expiration-ms}")
    private long expirationMs;

    /**
     * Genera un token para el usuario, sin datos extra.
     *
     * @param userDetails los datos de acceso del usuario
     * @return el token firmado
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Genera un token para el usuario con datos extra (claims).
     *
     * @param extraClaims los datos extra que viajan en el token
     * @param userDetails los datos de acceso del usuario
     * @return el token firmado
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder().claims(extraClaims).subject(userDetails.getUsername()).issuedAt(now).expiration(expiry)
                .signWith(getSigningKey()).compact();
    }

    /**
     * Devuelve la vigencia de los tokens.
     *
     * @return los milisegundos de vigencia
     */
    public long getExpirationMs() {
        return expirationMs;
    }

    /**
     * Lee el nombre de usuario (correo) guardado en el token.
     *
     * @param token el token
     * @return el correo del usuario
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Indica si el token pertenece al usuario y todavia no vencio.
     *
     * @param token       el token
     * @param userDetails los datos de acceso del usuario
     * @return true si el token es valido para ese usuario
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * Indica si el token ya vencio.
     *
     * @param token el token
     * @return true si ya vencio
     */
    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    /**
     * Lee un dato del token.
     *
     * @param <T>            el tipo del dato
     * @param token          el token
     * @param claimsResolver la funcion que toma el dato de los claims
     * @return el dato leido
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Valida la firma del token y devuelve todos sus datos.
     *
     * @param token el token
     * @return los claims del token
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
    }

    /**
     * Construye la llave de firma a partir del secreto.
     *
     * @return la llave de firma
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
