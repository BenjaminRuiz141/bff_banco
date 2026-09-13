package cl.duoc.bff.mobile.controller;

import cl.duoc.bff.mobile.dto.CuentaMobileDTO;
import cl.duoc.bff.mobile.service.CuentaMobileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mobile")
public class CuentaMobileController {

    private final CuentaMobileService cuentaMobileService;

    public CuentaMobileController(CuentaMobileService cuentaMobileService) {
        this.cuentaMobileService = cuentaMobileService;
    }

    @GetMapping({"/cuentas/{cuentaId}", "/accounts/{cuentaId}"})
    @PreAuthorize("hasRole('MOBILE')")
    public ResponseEntity<CuentaMobileDTO> obtenerCuenta(@PathVariable Integer cuentaId) {
        CuentaMobileDTO dto = cuentaMobileService.obtenerCuentaPorId(cuentaId);
        return ResponseEntity.ok(dto);
    }
}
