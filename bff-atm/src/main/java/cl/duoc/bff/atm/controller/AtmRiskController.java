package cl.duoc.bff.atm.controller;

import cl.duoc.bff.atm.dto.RiskValidationDTO;
import cl.duoc.bff.atm.service.AtmRiskValidationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class AtmRiskController {

    private final AtmRiskValidationService atmRiskValidationService;

    public AtmRiskController(AtmRiskValidationService atmRiskValidationService) {
        this.atmRiskValidationService = atmRiskValidationService;
    }

    @GetMapping("/api/atm/cuentas/{cuentaId}/validar-riesgo")
    @PreAuthorize("hasRole('ATM')")
    public ResponseEntity<RiskValidationDTO> validarRiesgoProtegido(
            @PathVariable Integer cuentaId,
            @RequestParam(defaultValue = "30000") Integer monto) {
        return ResponseEntity.ok(atmRiskValidationService.validarRiesgoRetiro(cuentaId, monto));
    }
}
