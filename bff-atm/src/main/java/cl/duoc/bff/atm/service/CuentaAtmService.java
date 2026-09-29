package cl.duoc.bff.atm.service;

import cl.duoc.bff.atm.dto.CuentaAtmDTO;
import cl.duoc.bff.atm.dto.SagaRetiroResponseDTO;
import cl.duoc.bff.atm.dto.SagaTransaccionEvent;
import cl.duoc.bff.atm.entity.Cuenta;
import cl.duoc.bff.atm.repository.CuentaRepository;
import cl.duoc.bff.atm.saga.SagaAtmProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CuentaAtmService {

    private static final Logger log = LoggerFactory.getLogger(CuentaAtmService.class);

    private final CuentaRepository cuentaRepository;
    private final SagaAtmProducer sagaAtmProducer;

    public CuentaAtmService(CuentaRepository cuentaRepository, SagaAtmProducer sagaAtmProducer) {
        this.cuentaRepository = cuentaRepository;
        this.sagaAtmProducer = sagaAtmProducer;
    }

    public CuentaAtmDTO obtenerCuentaPorId(Integer cuentaId) {
        Cuenta cuenta = cuentaRepository.findById(cuentaId)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada para canal ATM: " + cuentaId));

        return mapToAtmDto(cuenta);
    }

    public CuentaAtmDTO retirar(Integer cuentaId, Integer monto) {
        retirarConSaga(cuentaId, monto, false);
        Cuenta cuenta = cuentaRepository.findById(cuentaId).orElseThrow();
        return mapToAtmDto(cuenta);
    }

    @Transactional
    public SagaRetiroResponseDTO retirarConSaga(Integer cuentaId, Integer monto, boolean forzarFallo) {
        if (monto == null || monto <= 0) {
            throw new IllegalArgumentException("El monto a retirar debe ser mayor a cero.");
        }

        Cuenta cuenta = cuentaRepository.findById(cuentaId)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada para canal ATM: " + cuentaId));

        if (cuenta.getSaldo() < monto) {
            throw new IllegalStateException("Fondos insuficientes. Saldo disponible: " + cuenta.getSaldo());
        }

        String sagaId = "SAGA-ATM-" + UUID.randomUUID().toString().substring(0, 8);
        log.info("[SAGA-ATM] Iniciando transacción Saga {} para cuentaId: {}, monto: ${}", sagaId, cuentaId, monto);

        int saldoAnterior = cuenta.getSaldo();
        cuenta.setSaldo(saldoAnterior - monto);
        cuentaRepository.save(cuenta);

        if (forzarFallo) {
            log.warn("[SAGA-ATM] Fallo simulado detectado en dispensador físico de billetes para {}", sagaId);

            cuenta.setSaldo(saldoAnterior);
            cuentaRepository.save(cuenta);
            log.info("[SAGA-ATM] Compensación ejecutada localmente: Saldo restituido a ${}", saldoAnterior);

            SagaTransaccionEvent compensacionEvent = new SagaTransaccionEvent(
                    sagaId,
                    cuentaId,
                    "BFF_ATM",
                    "REVERSA",
                    monto,
                    "COMPENSADO",
                    "Falla física de dispensador en ATM. Giro cancelado y saldo reintegrado."
            );
            sagaAtmProducer.publicarCompensacion(compensacionEvent);

            return new SagaRetiroResponseDTO(
                    sagaId,
                    cuentaId,
                    monto,
                    saldoAnterior,
                    "COMPENSADA_ROLLBACK",
                    "Transacción compensada: El cajero presentó una falla física. El saldo de $" + monto +
                            " fue reintegrado automáticamente a su cuenta y se notificó a los demás canales."
            );
        }

        SagaTransaccionEvent exitoEvent = new SagaTransaccionEvent(
                sagaId,
                cuentaId,
                "BFF_ATM",
                "RETIRO",
                monto,
                "COMPLETADO",
                "Retiro exitoso dispensado en cajero automático."
        );
        sagaAtmProducer.publicarTransaccion(exitoEvent);

        return new SagaRetiroResponseDTO(
                sagaId,
                cuentaId,
                monto,
                cuenta.getSaldo(),
                "COMPLETADA",
                "Giro completado exitosamente por $" + monto + ". Evento publicado en ActiveMQ para sincronización multicanal."
        );
    }

    private CuentaAtmDTO mapToAtmDto(Cuenta cuenta) {
        return new CuentaAtmDTO(
                cuenta.getCuentaId(),
                cuenta.getSaldo(),
                cuenta.getTipo()
        );
    }
}
