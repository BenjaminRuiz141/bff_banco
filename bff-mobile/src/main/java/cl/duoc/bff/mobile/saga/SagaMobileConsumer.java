package cl.duoc.bff.mobile.saga;

import cl.duoc.bff.mobile.dto.NotificacionMobileDTO;
import cl.duoc.bff.mobile.dto.SagaTransaccionEvent;
import cl.duoc.bff.mobile.service.NotificacionMobileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class SagaMobileConsumer {

    private static final Logger log = LoggerFactory.getLogger(SagaMobileConsumer.class);

    private final NotificacionMobileService notificacionMobileService;

    public SagaMobileConsumer(NotificacionMobileService notificacionMobileService) {
        this.notificacionMobileService = notificacionMobileService;
    }

    @JmsListener(destination = "${banco.saga.queues.transacciones:banco.saga.transacciones.queue}")
    public void procesarTransaccionMobile(SagaTransaccionEvent event) {
        log.info("[SAGA-MOBILE-CONSUMER] Evento transaccional recibido desde ActiveMQ: {}", event);

        String textoAlerta = String.format("Aviso Banco: Se ha realizado un %s por $%d desde el canal %s.",
                event.getTipoOperacion(), event.getMonto(), event.getCanalOrigen());

        NotificacionMobileDTO pushResult = notificacionMobileService.enviarNotificacion(
                event.getCuentaId(),
                textoAlerta
        );

        log.info("[SAGA-MOBILE-CONSUMER] Notificación push despachada exitosamente para sagaId {}: {}",
                event.getSagaId(), pushResult.getDetalle());
    }

    @JmsListener(destination = "${banco.saga.queues.compensaciones:banco.saga.compensaciones.queue}")
    public void procesarCompensacionMobile(SagaTransaccionEvent event) {
        log.warn("[SAGA-MOBILE-CONSUMER] Alerta urgente de COMPENSACIÓN recibida desde ActiveMQ: {}", event);

        String textoAlertaUrgente = String.format("ALERTA DE SEGURIDAD: Su %s por $%d fue cancelado debido a: '%s'. Los fondos fueron restituidos a su cuenta.",
                event.getTipoOperacion(), event.getMonto(), event.getDescripcion());

        NotificacionMobileDTO pushResult = notificacionMobileService.enviarNotificacion(
                event.getCuentaId(),
                textoAlertaUrgente
        );

        log.warn("[SAGA-MOBILE-CONSUMER] Notificación de reversa despachada al smartphone: {}",
                pushResult.getDetalle());
    }
}
