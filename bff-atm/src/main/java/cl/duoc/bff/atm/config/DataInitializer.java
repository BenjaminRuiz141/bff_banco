package cl.duoc.bff.atm.config;

import cl.duoc.bff.atm.entity.Cuenta;
import cl.duoc.bff.atm.repository.CuentaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);
    private final CuentaRepository cuentaRepository;

    public DataInitializer(CuentaRepository cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    @Override
    public void run(String... args) {
        if (!cuentaRepository.existsById(1)) {
            log.info("[DATA-INITIALIZER] Inicializando cuenta de prueba (ID 1) en base de datos MySQL (interes)...");
            cuentaRepository.save(new Cuenta(1, "Cliente Demo Banco", 1500000, 30, "Ahorro"));
            log.info("[DATA-INITIALIZER] Cuenta ID 1 creada con saldo inicial de $1.500.000.");
        }
    }
}
