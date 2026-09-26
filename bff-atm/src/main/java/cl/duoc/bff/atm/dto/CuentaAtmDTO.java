package cl.duoc.bff.atm.dto;

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
