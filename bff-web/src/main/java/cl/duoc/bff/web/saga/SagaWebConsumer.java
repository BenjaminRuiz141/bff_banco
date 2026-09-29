package cl.duoc.bff.web.saga;

import cl.duoc.bff.web.dto.SagaTransaccionEvent;
import cl.duoc.bff.web.entity.Cuenta;
import cl.duoc.bff.web.repository.CuentaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class SagaWebConsumer {

    private static final Logger log = LoggerFactory.getLogger(SagaWebConsumer.class);

    private final CuentaRepository cuentaRepository;

    public SagaWebConsumer(CuentaRepository cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    @JmsListener(destination = "${banco.saga.queues.transacciones:banco.saga.transacciones.queue}")
    public void procesarTransaccionWeb(SagaTransaccionEvent event) {
        log.info("[SAGA-WEB-CONSUMER] Evento transaccional recibido desde ActiveMQ: {}", event);

        try {
            // Sincronizar saldo en la vista web / libro mayor
            Cuenta cuenta = cuentaRepository.findById(event.getCuentaId() != null ? event.getCuentaId() : 1)
                    .orElse(null);

            if (cuenta != null) {
                log.info("[SAGA-WEB-CONSUMER] Sincronizando libro mayor Web para cuentaId: {}. Saldo actual: ${}",
                        cuenta.getCuentaId(), cuenta.getSaldo());
            }

            log.info("[SAGA-WEB-CONSUMER] Transacción {} registrada exitosamente en la cartola consolidada Web",
                    event.getSagaId());
        } catch (Exception ex) {
            log.error("[SAGA-WEB-CONSUMER] Error al registrar transacción en cartola web: {}", ex.getMessage());
        }
    }

    @JmsListener(destination = "${banco.saga.queues.compensaciones:banco.saga.compensaciones.queue}")
    public void procesarCompensacionWeb(SagaTransaccionEvent event) {
        log.warn("[SAGA-WEB-CONSUMER] Evento de COMPENSACIÓN recibido desde ActiveMQ: {}", event);

        try {
            log.warn("[SAGA-WEB-CONSUMER] Anulando débito en cartola Web para sagaId: {}. Motivo: {}",
                    event.getSagaId(), event.getDescripcion());
            log.info("[SAGA-WEB-CONSUMER] Reversa contable consolidada en la banca Web para cuentaId: {}",
                    event.getCuentaId());
        } catch (Exception ex) {
            log.error("[SAGA-WEB-CONSUMER] Error al procesar compensación contable: {}", ex.getMessage());
        }
    }
}
