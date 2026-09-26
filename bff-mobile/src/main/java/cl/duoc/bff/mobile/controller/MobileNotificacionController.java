package cl.duoc.bff.mobile.controller;

import cl.duoc.bff.mobile.dto.NotificacionMobileDTO;
import cl.duoc.bff.mobile.service.NotificacionMobileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class MobileNotificacionController {

    private final NotificacionMobileService notificacionMobileService;

    public MobileNotificacionController(NotificacionMobileService notificacionMobileService) {
        this.notificacionMobileService = notificacionMobileService;
    }

    @PostMapping("/api/mobile/cuentas/{cuentaId}/notificaciones")
    @PreAuthorize("hasRole('ROLE_MOBILE')")
    public ResponseEntity<NotificacionMobileDTO> enviarNotificacionProtegido(
            @PathVariable Integer cuentaId,
            @RequestParam(defaultValue = "Movimiento registrado en tu cuenta") String mensaje,
            @RequestParam(defaultValue = "false") boolean forzarFallo) {
        return ResponseEntity.ok(notificacionMobileService.enviarNotificacion(cuentaId, mensaje, forzarFallo));
    }

    @GetMapping("/api/mobile/public/circuit-breaker/test")
    public ResponseEntity<NotificacionMobileDTO> testCircuitBreaker(
            @RequestParam(defaultValue = "1") Integer cuentaId,
            @RequestParam(defaultValue = "Alerta de prueba móvil") String mensaje,
            @RequestParam(defaultValue = "false") boolean fail) {
        return ResponseEntity.ok(notificacionMobileService.enviarNotificacion(cuentaId, mensaje, fail));
    }
}
