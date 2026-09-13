package cl.duoc.bff.atm.controller;

import cl.duoc.bff.atm.dto.AuthRequestDTO;
import cl.duoc.bff.atm.dto.AuthResponseDTO;
import cl.duoc.bff.atm.security.JwtService;
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
                : "ROLE_ATM";

        String token = jwtService.generarToken(usuario, rol);
        return ResponseEntity.ok(new AuthResponseDTO(token, usuario, rol));
    }

    @GetMapping("/token")
    public ResponseEntity<AuthResponseDTO> emitirTokenPorGet(
            @RequestParam(defaultValue = "user_atm") String usuario,
            @RequestParam(defaultValue = "ROLE_ATM") String rol
    ) {
        String token = jwtService.generarToken(usuario, rol);
        return ResponseEntity.ok(new AuthResponseDTO(token, usuario, rol));
    }
}
