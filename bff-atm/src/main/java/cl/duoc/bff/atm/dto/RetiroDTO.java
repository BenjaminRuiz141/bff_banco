package cl.duoc.bff.atm.dto;

public class RetiroDTO {

    private Integer cuentaId;
    private Integer monto;

    public RetiroDTO() {
    }

    public RetiroDTO(Integer monto) {
        this.monto = monto;
    }

    public RetiroDTO(Integer cuentaId, Integer monto) {
        this.cuentaId = cuentaId;
        this.monto = monto;
    }

    public Integer getCuentaId() {
        return cuentaId;
    }

    public void setCuentaId(Integer cuentaId) {
        this.cuentaId = cuentaId;
    }

    public Integer getMonto() {
        return monto;
    }

    public void setMonto(Integer monto) {
        this.monto = monto;
    }
}
