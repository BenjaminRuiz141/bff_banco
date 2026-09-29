package cl.duoc.bff.web.saga;

import cl.duoc.bff.web.dto.SagaTransaccionEvent;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class SagaWebProducer {

    private static final Logger log = LoggerFactory.getLogger(SagaWebProducer.class);

    private final JmsTemplate jmsTemplate;

    @Value("${banco.saga.queues.transacciones:banco.saga.transacciones.queue}")
    private String colaTransacciones;

    public SagaWebProducer(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @CircuitBreaker(name = "jmsBrokerService", fallbackMethod = "fallbackPublicacionWeb")
    @Retry(name = "jmsBrokerService")
    public void publicarTransferenciaWeb(SagaTransaccionEvent event) {
        log.info("[SAGA-WEB-PRODUCER] Publicando transferencia web a la cola '{}': {}", colaTransacciones, event);
        jmsTemplate.convertAndSend(colaTransacciones, event);
        log.info("[SAGA-WEB-PRODUCER] Transferencia {} publicada exitosamente en ActiveMQ", event.getSagaId());
    }

    public void fallbackPublicacionWeb(SagaTransaccionEvent event, Throwable ex) {
        log.error("[FALLBACK RESILIENCE4J WEB] ActiveMQ inaccesible al publicar transferencia {}. Contingencia activada: {}",
                event.getSagaId(), ex.getMessage());
    }
}
