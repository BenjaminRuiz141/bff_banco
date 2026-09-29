package cl.duoc.bff.atm.saga;

import cl.duoc.bff.atm.dto.SagaTransaccionEvent;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class SagaAtmProducer {

    private static final Logger log = LoggerFactory.getLogger(SagaAtmProducer.class);

    private final JmsTemplate jmsTemplate;

    @Value("${banco.saga.queues.transacciones:banco.saga.transacciones.queue}")
    private String colaTransacciones;

    @Value("${banco.saga.queues.compensaciones:banco.saga.compensaciones.queue}")
    private String colaCompensaciones;

    public SagaAtmProducer(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @CircuitBreaker(name = "jmsBrokerService", fallbackMethod = "fallbackPublicacionTransaccion")
    @Retry(name = "jmsBrokerService")
    public void publicarTransaccion(SagaTransaccionEvent event) {
        log.info("[SAGA-ATM] Publicando evento transaccional a la cola '{}': {}", colaTransacciones, event);
        jmsTemplate.convertAndSend(colaTransacciones, event);
        log.info("[SAGA-ATM] Evento {} publicado exitosamente en ActiveMQ", event.getSagaId());
    }

    @CircuitBreaker(name = "jmsBrokerService", fallbackMethod = "fallbackPublicacionCompensacion")
    @Retry(name = "jmsBrokerService")
    public void publicarCompensacion(SagaTransaccionEvent event) {
        log.warn("[SAGA-ATM] Publicando evento de COMPENSACIÓN a la cola '{}': {}", colaCompensaciones, event);
        jmsTemplate.convertAndSend(colaCompensaciones, event);
        log.warn("[SAGA-ATM] Compensación {} publicada exitosamente en ActiveMQ", event.getSagaId());
    }

    public void fallbackPublicacionTransaccion(SagaTransaccionEvent event, Throwable ex) {
        log.error("[FALLBACK RESILIENCE4J] ActiveMQ inaccesible al publicar transacción {}. Contingencia activada: {}",
                event.getSagaId(), ex.getMessage());
    }

    public void fallbackPublicacionCompensacion(SagaTransaccionEvent event, Throwable ex) {
        log.error("[FALLBACK RESILIENCE4J] ActiveMQ inaccesible al publicar compensación {}. Contingencia activada: {}",
                event.getSagaId(), ex.getMessage());
    }
}
