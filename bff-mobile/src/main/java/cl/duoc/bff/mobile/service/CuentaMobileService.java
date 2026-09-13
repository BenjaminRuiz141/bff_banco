package cl.duoc.bff.mobile.service;

import cl.duoc.bff.mobile.dto.CuentaMobileDTO;
import cl.duoc.bff.mobile.entity.Cuenta;
import cl.duoc.bff.mobile.repository.CuentaRepository;
import org.springframework.stereotype.Service;

@Service
public class CuentaMobileService {

    private final CuentaRepository cuentaRepository;

    public CuentaMobileService(CuentaRepository cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    public CuentaMobileDTO obtenerCuentaPorId(Integer cuentaId) {
        Cuenta cuenta = cuentaRepository.findById(cuentaId)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada con identificador: " + cuentaId));

        return new CuentaMobileDTO(
                cuenta.getCuentaId(),
                cuenta.getNombre(),
                cuenta.getSaldo(),
                cuenta.getTipo()
        );
    }
}
