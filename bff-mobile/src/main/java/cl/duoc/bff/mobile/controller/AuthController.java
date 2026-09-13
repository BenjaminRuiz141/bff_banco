package cl.duoc.bff.mobile.controller;

import cl.duoc.bff.mobile.dto.AuthRequestDTO;
import cl.duoc.bff.mobile.dto.AuthResponseDTO;
import cl.duoc.bff.mobile.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtService jwtService;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/token")
    public ResponseEntity<AuthResponseDTO> emitirToken(@RequestBody AuthRequestDTO request) {
        String usuario = (request.getUsuario() != null && !request.getUsuario().isBlank())
                ? request.getUsuario()
                : "usuario_prueba";
        String rol = (request.getRol() != null && !request.getRol().isBlank())
                ? request.getRol()
                : "ROLE_MOBILE";

        String token = jwtService.generarToken(usuario, rol);
        return ResponseEntity.ok(new AuthResponseDTO(token, usuario, rol));
    }

    @GetMapping("/token")
    public ResponseEntity<AuthResponseDTO> emitirTokenPorGet(
            @RequestParam(defaultValue = "user_mobile") String usuario,
            @RequestParam(defaultValue = "ROLE_MOBILE") String rol
    ) {
        String token = jwtService.generarToken(usuario, rol);
        return ResponseEntity.ok(new AuthResponseDTO(token, usuario, rol));
    }
}
