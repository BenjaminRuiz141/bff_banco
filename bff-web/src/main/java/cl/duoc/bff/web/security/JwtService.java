package cl.duoc.bff.web.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    // Clave secreta consistente de 256 bits (HMAC-SHA256)
    private static final String SECRET_KEY = "BancoDuocUcBffSecretKey2026SecureJwtTokenKeyMustBe256BitsLong!";
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 10; // 10 horas

    private SecretKey getSigningKey() {
        byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generarToken(String usuario, String rol) {
        String rolNormalizado = rol.startsWith("ROLE_") ? rol : "ROLE_" + rol;
        return Jwts.builder()
                .subject(usuario)
                .claim("rol", rolNormalizado)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey())
                .compact();
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

    public String extraerRol(String token) {
        return extraerClaims(token).get("rol", String.class);
    }

    public boolean validarToken(String token) {
        try {
            Claims claims = extraerClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
