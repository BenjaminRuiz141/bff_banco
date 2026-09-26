package cl.duoc.bff.mobile.dto;

import java.time.LocalDateTime;

public class NotificacionMobileDTO {

    private Integer cuentaId;
    private String mensajeNotificacion;
    private String canalPush;
    private String estadoCircuito;
    private String detalle;
    private LocalDateTime timestamp;

    public NotificacionMobileDTO() {
        this.timestamp = LocalDateTime.now();
    }

    public NotificacionMobileDTO(Integer cuentaId, String mensajeNotificacion, String canalPush,
                                 String estadoCircuito, String detalle) {
        this.cuentaId = cuentaId;
        this.mensajeNotificacion = mensajeNotificacion;
        this.canalPush = canalPush;
        this.estadoCircuito = estadoCircuito;
        this.detalle = detalle;
        this.timestamp = LocalDateTime.now();
    }

    public Integer getCuentaId() {
        return cuentaId;
    }

    public void setCuentaId(Integer cuentaId) {
        this.cuentaId = cuentaId;
    }

    public String getMensajeNotificacion() {
        return mensajeNotificacion;
    }

    public void setMensajeNotificacion(String mensajeNotificacion) {
        this.mensajeNotificacion = mensajeNotificacion;
    }

    public String getCanalPush() {
        return canalPush;
    }

    public void setCanalPush(String canalPush) {
        this.canalPush = canalPush;
    }

    public String getEstadoCircuito() {
        return estadoCircuito;
    }

    public void setEstadoCircuito(String estadoCircuito) {
        this.estadoCircuito = estadoCircuito;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
