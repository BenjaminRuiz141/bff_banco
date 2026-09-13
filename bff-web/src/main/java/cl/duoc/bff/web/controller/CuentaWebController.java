package cl.duoc.bff.web.controller;

import cl.duoc.bff.web.dto.CuentaWebDTO;
import cl.duoc.bff.web.service.CuentaWebService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/web")
public class CuentaWebController {

    private final CuentaWebService cuentaWebService;

    public CuentaWebController(CuentaWebService cuentaWebService) {
        this.cuentaWebService = cuentaWebService;
    }

    @GetMapping({"/cuentas/{cuentaId}", "/accounts/{cuentaId}"})
    @PreAuthorize("hasRole('WEB')")
    public ResponseEntity<CuentaWebDTO> obtenerCuenta(@PathVariable Integer cuentaId) {
        CuentaWebDTO dto = cuentaWebService.obtenerCuentaPorId(cuentaId);
        return ResponseEntity.ok(dto);
    }
}
