package cl.duoc.bff.atm.dto;

public class SagaRetiroResponseDTO {

    private String sagaId;
    private Integer cuentaId;
    private Integer monto;
    private Integer saldoActual;
    private String estadoSaga;
    private String mensaje;

    public SagaRetiroResponseDTO() {
    }

    public SagaRetiroResponseDTO(String sagaId, Integer cuentaId, Integer monto, Integer saldoActual, String estadoSaga, String mensaje) {
        this.sagaId = sagaId;
        this.cuentaId = cuentaId;
        this.monto = monto;
        this.saldoActual = saldoActual;
        this.estadoSaga = estadoSaga;
        this.mensaje = mensaje;
    }

    public String getSagaId() {
        return sagaId;
    }

    public void setSagaId(String sagaId) {
        this.sagaId = sagaId;
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

    public Integer getSaldoActual() {
        return saldoActual;
    }

    public void setSaldoActual(Integer saldoActual) {
        this.saldoActual = saldoActual;
    }

    public String getEstadoSaga() {
        return estadoSaga;
    }

    public void setEstadoSaga(String estadoSaga) {
        this.estadoSaga = estadoSaga;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
