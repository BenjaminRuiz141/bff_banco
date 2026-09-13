package cl.duoc.bff.atm.service;

import cl.duoc.bff.atm.dto.CuentaAtmDTO;
import cl.duoc.bff.atm.entity.Cuenta;
import cl.duoc.bff.atm.repository.CuentaRepository;
import org.springframework.stereotype.Service;

@Service
public class CuentaAtmService {

    private final CuentaRepository cuentaRepository;

    public CuentaAtmService(CuentaRepository cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    public CuentaAtmDTO obtenerCuentaPorId(Integer cuentaId) {
        Cuenta cuenta = cuentaRepository.findById(cuentaId)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada para canal ATM: " + cuentaId));

        return mapToAtmDto(cuenta);
    }

    public CuentaAtmDTO retirar(Integer cuentaId, Integer monto) {
        if (monto == null || monto <= 0) {
            throw new IllegalArgumentException("El monto a retirar debe ser mayor a cero.");
        }

        Cuenta cuenta = cuentaRepository.findById(cuentaId)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada para canal ATM: " + cuentaId));

        if (cuenta.getSaldo() < monto) {
            throw new IllegalStateException("Fondos insuficientes. Saldo disponible: " + cuenta.getSaldo());
        }

        cuenta.setSaldo(cuenta.getSaldo() - monto);
        cuentaRepository.save(cuenta);

        return mapToAtmDto(cuenta);
    }

    private CuentaAtmDTO mapToAtmDto(Cuenta cuenta) {
        return new CuentaAtmDTO(
                cuenta.getCuentaId(),
                cuenta.getSaldo(),
                cuenta.getTipo()
        );
    }
}
