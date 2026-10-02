package cl.duoc.auth.controller;

import cl.duoc.auth.dto.AuthRequestDTO;
import cl.duoc.auth.dto.AuthResponseDTO;
import cl.duoc.auth.dto.TokenValidationResponseDTO;
import cl.duoc.auth.service.JwtTokenService;
import io.jsonwebtoken.Claims;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
public class AuthController {

    private final JwtTokenService jwtTokenService;

    public AuthController(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> inicio() {
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("servicio", "Banco Duoc UC - Auth Service");
        info.put("estado", "UP");
        info.put("descripcion", "Microservicio de Autenticacion Centralizada con OAuth2 GitHub y JWT");
        info.put("endpoints", Map.of(
                "oauth2_login_github", "/oauth2/authorization/github",
                "generar_token_postman", "/api/auth/token",
                "validar_token", "/api/auth/validate"
        ));
        info.put("canales_protegidos", Map.of(
                "bff_web", "http://localhost:8081 (ROLE_WEB)",
                "bff_mobile", "http://localhost:8082 (ROLE_MOBILE)",
                "bff_atm", "http://localhost:8083 (ROLE_ATM)"
        ));
        return ResponseEntity.ok(info);
    }

    @PostMapping("/api/auth/token")
    public ResponseEntity<AuthResponseDTO> emitirTokenPost(@RequestBody(required = false) AuthRequestDTO request) {
        String usuario = (request != null && request.getUsuario() != null && !request.getUsuario().isBlank())
                ? request.getUsuario()
                : "usuario_dev";

        List<String> roles = (request != null && request.getRoles() != null && !request.getRoles().isEmpty())
                ? request.getRoles()
                : (request != null && request.getRol() != null && !request.getRol().isBlank())
                    ? List.of(request.getRol())
                    : List.of("ROLE_WEB", "ROLE_MOBILE", "ROLE_ATM");

        String token = jwtTokenService.generarToken(usuario, roles);
        return ResponseEntity.ok(new AuthResponseDTO(
                token,
                usuario,
                roles,
                jwtTokenService.getExpirationTime(),
                "Token emitido exitosamente para pruebas/Postman"
        ));
    }

    @GetMapping("/api/auth/token")
    public ResponseEntity<AuthResponseDTO> emitirTokenGet(
            @RequestParam(defaultValue = "profesor") String usuario,
            @RequestParam(required = false) String rol
    ) {
        List<String> roles = (rol != null && !rol.isBlank())
                ? List.of(rol)
                : List.of("ROLE_WEB", "ROLE_MOBILE", "ROLE_ATM");

        String token = jwtTokenService.generarToken(usuario, roles);
        return ResponseEntity.ok(new AuthResponseDTO(
                token,
                usuario,
                roles,
                jwtTokenService.getExpirationTime(),
                "Token emitido exitosamente para pruebas/curl"
        ));
    }

    @GetMapping("/api/auth/validate")
    public ResponseEntity<TokenValidationResponseDTO> validarToken(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam(value = "token", required = false) String tokenParam
    ) {
        String token = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        } else if (tokenParam != null && !tokenParam.isBlank()) {
            token = tokenParam;
        }

        if (token == null || !jwtTokenService.validarToken(token)) {
            return ResponseEntity.badRequest().body(TokenValidationResponseDTO.invalido("Token ausente, invalido o expirado"));
        }

        Claims claims = jwtTokenService.extraerClaims(token);
        String usuario = claims.getSubject();
        List<String> roles = jwtTokenService.extraerRoles(token);

        return ResponseEntity.ok(new TokenValidationResponseDTO(
                true,
                usuario,
                roles,
                claims.getIssuedAt(),
                claims.getExpiration(),
                "Token valido y activo"
        ));
    }
}
