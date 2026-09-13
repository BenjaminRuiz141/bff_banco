package cl.duoc.bff.web.service;

import cl.duoc.bff.web.dto.CuentaWebDTO;
import cl.duoc.bff.web.entity.Cuenta;
import cl.duoc.bff.web.repository.CuentaRepository;
import org.springframework.stereotype.Service;

@Service
public class CuentaWebService {

    private final CuentaRepository cuentaRepository;

    public CuentaWebService(CuentaRepository cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    public CuentaWebDTO obtenerCuentaPorId(Integer cuentaId) {
        Cuenta cuenta = cuentaRepository.findById(cuentaId)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada con identificador: " + cuentaId));

        return new CuentaWebDTO(
                cuenta.getCuentaId(),
                cuenta.getNombre(),
                cuenta.getEdad(),
                cuenta.getSaldo(),
                cuenta.getTipo()
        );
    }
}
