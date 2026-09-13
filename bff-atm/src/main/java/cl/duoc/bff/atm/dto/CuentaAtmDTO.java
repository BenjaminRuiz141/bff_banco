package cl.duoc.bff.atm.dto;

/**
 * DTO para el canal ATM (Cajeros Automáticos): Estricto y transaccional.
 * Protege la privacidad del titular en cajeros públicos omitiendo nombre y edad.
 * Basado en las variables de intereses.csv (cuenta_id, saldo, tipo).
 */
public class CuentaAtmDTO {

    private Integer cuentaId;
    private Integer saldo;
    private String tipo;

    public CuentaAtmDTO() {
    }

    public CuentaAtmDTO(Integer cuentaId, Integer saldo, String tipo) {
        this.cuentaId = cuentaId;
        this.saldo = saldo;
        this.tipo = tipo;
    }

    public Integer getCuentaId() {
        return cuentaId;
    }

    public void setCuentaId(Integer cuentaId) {
        this.cuentaId = cuentaId;
    }

    public Integer getSaldo() {
        return saldo;
    }

    public void setSaldo(Integer saldo) {
        this.saldo = saldo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
