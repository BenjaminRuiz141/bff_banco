package cl.duoc.bff.mobile.repository;

import cl.duoc.bff.mobile.entity.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, Integer> {
    Optional<Cuenta> findByCuentaId(Integer cuentaId);
}
