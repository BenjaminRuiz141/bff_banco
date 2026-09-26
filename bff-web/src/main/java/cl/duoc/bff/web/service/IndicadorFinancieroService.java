package cl.duoc.bff.web.service;

import cl.duoc.bff.web.dto.IndicadorFinancieroDTO;
import cl.duoc.bff.web.entity.Cuenta;
import cl.duoc.bff.web.repository.CuentaRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class IndicadorFinancieroService {

    private static final Logger log = LoggerFactory.getLogger(IndicadorFinancieroService.class);
    private final CuentaRepository cuentaRepository;

    public IndicadorFinancieroService(CuentaRepository cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    @CircuitBreaker(name = "indicadorFinancieroService", fallbackMethod = "fallbackIndicadores")
    public IndicadorFinancieroDTO obtenerValorizacionCuenta(Integer cuentaId, boolean forzarFallo) {
        log.info("Ejecutando llamada a servicio externo de divisas para cuentaId: {}, forzarFallo: {}", cuentaId, forzarFallo);

        if (forzarFallo) {
            log.warn("Disparando excepción intencional para verificar apertura del Circuit Breaker y activación de Fallback");
            throw new RuntimeException("Error simulado: Timeout al conectar con la API externa del Banco Central / Indicadores");
        }

        Cuenta cuenta = cuentaRepository.findById(cuentaId != null ? cuentaId : 1)
                .orElse(new Cuenta(1, "Cliente Demo Web", 1500000, 30, "Ahorro"));

        double valorDolarActual = 945.50;
        double saldoUsd = BigDecimal.valueOf(cuenta.getSaldo() / valorDolarActual)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();

        return new IndicadorFinancieroDTO(
                cuenta.getCuentaId(),
                cuenta.getNombre(),
                cuenta.getSaldo(),
                saldoUsd,
                valorDolarActual,
                "CLOSED (Llamada Externa Exitosa)",
                "Valorización en tiempo real obtenida exitosamente desde el servicio externo de divisas."
        );
    }

    public IndicadorFinancieroDTO fallbackIndicadores(Integer cuentaId, boolean forzarFallo, Throwable ex) {
        log.warn("ACTIVANDO FALLBACK de IndicadorFinancieroService debido a: {}", ex.getMessage());

        Cuenta cuenta = cuentaRepository.findById(cuentaId != null ? cuentaId : 1)
                .orElse(new Cuenta(1, "Cliente Demo Web", 1500000, 30, "Ahorro"));

        return new IndicadorFinancieroDTO(
                cuenta.getCuentaId(),
                cuenta.getNombre(),
                cuenta.getSaldo(),
                null,
                null,
                "FALLBACK_ACTIVADO (Resilience4j Contingencia)",
                "Contingencia Activa: El servicio externo de divisas no se encuentra disponible (" + ex.getMessage() +
                        "). Se muestra el saldo local en CLP de forma segura sin interrupción del servicio."
        );
    }
}
