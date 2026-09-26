package cl.duoc.bff.atm.dto;

import java.time.LocalDateTime;

public class RiskValidationDTO {

    private Integer cuentaId;
    private Integer monto;
    private String estadoAutorizacion;
    private String estadoCircuito;
    private String mensaje;
    private LocalDateTime timestamp;

    public RiskValidationDTO() {
        this.timestamp = LocalDateTime.now();
    }

    public RiskValidationDTO(Integer cuentaId, Integer monto, String estadoAutorizacion,
                             String estadoCircuito, String mensaje) {
        this.cuentaId = cuentaId;
        this.monto = monto;
        this.estadoAutorizacion = estadoAutorizacion;
        this.estadoCircuito = estadoCircuito;
        this.mensaje = mensaje;
        this.timestamp = LocalDateTime.now();
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

    public String getEstadoAutorizacion() {
        return estadoAutorizacion;
    }

    public void setEstadoAutorizacion(String estadoAutorizacion) {
        this.estadoAutorizacion = estadoAutorizacion;
    }

    public String getEstadoCircuito() {
        return estadoCircuito;
    }

    public void setEstadoCircuito(String estadoCircuito) {
        this.estadoCircuito = estadoCircuito;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
