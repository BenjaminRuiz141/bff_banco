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
    public ResponseEntity<IndicadorFinancieroDTO> obtenerIndicadoresProtegido(@PathVariable Integer cuentaId) {
        return ResponseEntity.ok(indicadorFinancieroService.obtenerValorizacionCuenta(cuentaId));
    }
}
