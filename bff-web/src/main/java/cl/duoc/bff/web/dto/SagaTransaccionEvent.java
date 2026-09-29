package cl.duoc.bff.web.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class SagaTransaccionEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private String sagaId;
    private Integer cuentaId;
    private String canalOrigen;
    private String tipoOperacion; // RETIRO, TRANSFERENCIA, REVERSA
    private Integer monto;
    private String estado;        // INICIADO, COMPLETADO, COMPENSADO, FALLIDO
    private String timestamp;
    private String descripcion;

    public SagaTransaccionEvent() {
    }

    public SagaTransaccionEvent(String sagaId, Integer cuentaId, String canalOrigen, String tipoOperacion, Integer monto, String estado, String descripcion) {
        this.sagaId = sagaId;
        this.cuentaId = cuentaId;
        this.canalOrigen = canalOrigen;
        this.tipoOperacion = tipoOperacion;
        this.monto = monto;
        this.estado = estado;
        this.timestamp = LocalDateTime.now().toString();
        this.descripcion = descripcion;
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

    public String getCanalOrigen() {
        return canalOrigen;
    }

    public void setCanalOrigen(String canalOrigen) {
        this.canalOrigen = canalOrigen;
    }

    public String getTipoOperacion() {
        return tipoOperacion;
    }

    public void setTipoOperacion(String tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }

    public Integer getMonto() {
        return monto;
    }

    public void setMonto(Integer monto) {
        this.monto = monto;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "SagaTransaccionEvent{" +
                "sagaId='" + sagaId + '\'' +
                ", cuentaId=" + cuentaId +
                ", canalOrigen='" + canalOrigen + '\'' +
                ", tipoOperacion='" + tipoOperacion + '\'' +
                ", monto=" + monto +
                ", estado='" + estado + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
