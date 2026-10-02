package cl.duoc.bff.atm.service;

import cl.duoc.bff.atm.dto.RiskValidationDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AtmRiskValidationService {

    private static final Logger log = LoggerFactory.getLogger(AtmRiskValidationService.class);

    @CircuitBreaker(name = "atmRiskValidationService", fallbackMethod = "fallbackValidacionRiesgo")
    public RiskValidationDTO validarRiesgoRetiro(Integer cuentaId, Integer monto) {
        log.info("Consultando servicio externo de riesgo/fraude para cuentaId: {}, monto: {}", cuentaId, monto);

        return new RiskValidationDTO(
                cuentaId,
                monto,
                "APROBADO_NORMAL",
                "CLOSED (Servicio Antifraude Operativo)",
                "Evaluación de riesgo completada exitosamente sin alertas de fraude."
        );
    }

    public RiskValidationDTO fallbackValidacionRiesgo(Integer cuentaId, Integer monto, Throwable ex) {
        log.warn("ACTIVANDO FALLBACK de AtmRiskValidationService debido a: {}", ex.getMessage());

        boolean permiteContingencia = monto != null && monto <= 50000;
        String autorizacion = permiteContingencia ? "APROBADO_CONTINGENCIA_OFFLINE" : "RECHAZADO_POR_SEGURIDAD";
        String mensaje = permiteContingencia
                ? "Modo de contingencia ATM activado: Servicio antifraude no disponible (" + ex.getMessage() +
                  "). Retiro menor o igual a $50.000 pre-autorizado bajo política de resiliencia local."
                : "Modo de contingencia ATM activado: Servicio antifraude no disponible. Retiro superior a $50.000 denegado temporalmente por seguridad.";

        return new RiskValidationDTO(
                cuentaId,
                monto,
                autorizacion,
                "FALLBACK_ACTIVADO (Resilience4j Contingencia)",
                mensaje
        );
    }
}
