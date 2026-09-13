package cl.duoc.bff.atm.controller;

import cl.duoc.bff.atm.dto.CuentaAtmDTO;
import cl.duoc.bff.atm.dto.RetiroDTO;
import cl.duoc.bff.atm.service.CuentaAtmService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/atm")
public class CuentaAtmController {

    private final CuentaAtmService cuentaAtmService;

    public CuentaAtmController(CuentaAtmService cuentaAtmService) {
        this.cuentaAtmService = cuentaAtmService;
    }

    @GetMapping({"/cuentas/{cuentaId}", "/accounts/{cuentaId}"})
    @PreAuthorize("hasRole('ATM')")
    public ResponseEntity<CuentaAtmDTO> obtenerCuenta(@PathVariable Integer cuentaId) {
        CuentaAtmDTO dto = cuentaAtmService.obtenerCuentaPorId(cuentaId);
        return ResponseEntity.ok(dto);
    }

    @PostMapping({"/cuentas/{cuentaId}/retiro", "/accounts/{cuentaId}/withdraw"})
    @PreAuthorize("hasRole('ATM')")
    public ResponseEntity<CuentaAtmDTO> retirarPorRuta(
            @PathVariable Integer cuentaId,
            @RequestBody RetiroDTO peticion) {
        CuentaAtmDTO dto = cuentaAtmService.retirar(cuentaId, peticion.getMonto());
        return ResponseEntity.ok(dto);
    }

    @PostMapping({"/cuentas/retiro", "/accounts/withdraw"})
    @PreAuthorize("hasRole('ATM')")
    public ResponseEntity<CuentaAtmDTO> retirarPorCuerpo(
            @RequestBody RetiroDTO peticion) {
        CuentaAtmDTO dto = cuentaAtmService.retirar(peticion.getCuentaId(), peticion.getMonto());
        return ResponseEntity.ok(dto);
    }
}
