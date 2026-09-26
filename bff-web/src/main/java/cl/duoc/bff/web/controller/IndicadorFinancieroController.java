package cl.duoc.bff.web.controller;

import cl.duoc.bff.web.dto.IndicadorFinancieroDTO;
import cl.duoc.bff.web.service.IndicadorFinancieroService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class IndicadorFinancieroController {

    private final IndicadorFinancieroService indicadorFinancieroService;

    public IndicadorFinancieroController(IndicadorFinancieroService indicadorFinancieroService) {
        this.indicadorFinancieroService = indicadorFinancieroService;
    }

    @GetMapping("/api/web/cuentas/{cuentaId}/indicadores")
    @PreAuthorize("hasRole('WEB')")
    public ResponseEntity<IndicadorFinancieroDTO> obtenerIndicadoresProtegido(
            @PathVariable Integer cuentaId,
            @RequestParam(defaultValue = "false") boolean forzarFallo) {
        return ResponseEntity.ok(indicadorFinancieroService.obtenerValorizacionCuenta(cuentaId, forzarFallo));
    }

    @GetMapping("/api/web/public/circuit-breaker/test")
    public ResponseEntity<IndicadorFinancieroDTO> testCircuitBreaker(
            @RequestParam(defaultValue = "1") Integer cuentaId,
            @RequestParam(defaultValue = "false") boolean fail) {
        return ResponseEntity.ok(indicadorFinancieroService.obtenerValorizacionCuenta(cuentaId, fail));
    }
}
