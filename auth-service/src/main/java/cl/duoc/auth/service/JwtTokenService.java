package cl.duoc.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class JwtTokenService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration:36000000}")
    private long expirationTime;

    private SecretKey getSigningKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generarToken(String usuario, List<String> roles) {
        List<String> rolesNormalizados = (roles == null || roles.isEmpty())
                ? List.of("ROLE_WEB", "ROLE_MOBILE", "ROLE_ATM")
                : roles.stream()
                    .map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r)
                    .toList();

        String rolString = String.join(",", rolesNormalizados);

        Date ahora = new Date();
        Date expiracion = new Date(ahora.getTime() + expirationTime);

        return Jwts.builder()
                .subject(usuario)
                .claim("rol", rolString)
                .claim("roles", rolesNormalizados)
                .issuedAt(ahora)
                .expiration(expiracion)
                .signWith(getSigningKey())
                .compact();
    }

    public String generarToken(String usuario, String rol) {
        String rolNormalizado = rol != null && !rol.isBlank()
                ? (rol.startsWith("ROLE_") ? rol : "ROLE_" + rol)
                : "ROLE_WEB";

        if (rolNormalizado.contains(",")) {
            List<String> listaRoles = Arrays.stream(rolNormalizado.split(","))
                    .map(String::trim)
                    .toList();
            return generarToken(usuario, listaRoles);
        }

        return generarToken(usuario, List.of(rolNormalizado));
    }

    public Claims extraerClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extraerUsuario(String token) {
        return extraerClaims(token).getSubject();
    }

    @SuppressWarnings("unchecked")
    public List<String> extraerRoles(String token) {
        Claims claims = extraerClaims(token);
        Object rolesObj = claims.get("roles");
        if (rolesObj instanceof List<?> list) {
            return list.stream().map(Object::toString).toList();
        }

        String rolStr = claims.get("rol", String.class);
        if (rolStr != null && !rolStr.isBlank()) {
            return Arrays.stream(rolStr.split(","))
                    .map(String::trim)
                    .toList();
        }

        return Collections.emptyList();
    }

    public boolean validarToken(String token) {
        try {
            Claims claims = extraerClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public long getExpirationTime() {
        return expirationTime;
    }
}
