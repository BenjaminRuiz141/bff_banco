package cl.duoc.bff.mobile.service;

import cl.duoc.bff.mobile.dto.NotificacionMobileDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificacionMobileService {

    private static final Logger log = LoggerFactory.getLogger(NotificacionMobileService.class);

    @CircuitBreaker(name = "notificacionMobileService", fallbackMethod = "fallbackNotificacion")
    public NotificacionMobileDTO enviarNotificacion(Integer cuentaId, String mensaje) {
        log.info("Enviando push notification externa para cuentaId: {}, mensaje: {}", cuentaId, mensaje);

        return new NotificacionMobileDTO(
                cuentaId,
                mensaje != null ? mensaje : "Actualización de saldo en tu cuenta",
                "FIREBASE_CLOUD_MESSAGING (FCM)",
                "CLOSED (Llamada Gateway Exitosa)",
                "Notificación push enviada en tiempo real al dispositivo móvil."
        );
    }

    public NotificacionMobileDTO fallbackNotificacion(Integer cuentaId, String mensaje, Throwable ex) {
        log.warn("ACTIVANDO FALLBACK de NotificacionMobileService debido a: {}", ex.getMessage());

        return new NotificacionMobileDTO(
                cuentaId,
                mensaje,
                "COLA_OFFLINE_SMS_BACKUP",
                "FALLBACK_ACTIVADO (Resilience4j Contingencia)",
                "Contingencia Activa: Gateway push externo inaccesible (" + ex.getMessage() +
                        "). Notificación encolada en bandeja local / SMS diferido para entrega garantizada."
        );
    }
}
