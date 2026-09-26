package cl.duoc.bff.web.dto;

import java.time.LocalDateTime;

public class IndicadorFinancieroDTO {

    private Integer cuentaId;
    private String titular;
    private Integer saldoClp;
    private Double saldoUsd;
    private Double valorDolar;
    private String estadoCircuito;
    private String mensaje;
    private LocalDateTime timestamp;

    public IndicadorFinancieroDTO() {
        this.timestamp = LocalDateTime.now();
    }

    public IndicadorFinancieroDTO(Integer cuentaId, String titular, Integer saldoClp, Double saldoUsd,
                                  Double valorDolar, String estadoCircuito, String mensaje) {
        this.cuentaId = cuentaId;
        this.titular = titular;
        this.saldoClp = saldoClp;
        this.saldoUsd = saldoUsd;
        this.valorDolar = valorDolar;
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

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public Integer getSaldoClp() {
        return saldoClp;
    }

    public void setSaldoClp(Integer saldoClp) {
        this.saldoClp = saldoClp;
    }

    public Double getSaldoUsd() {
        return saldoUsd;
    }

    public void setSaldoUsd(Double saldoUsd) {
        this.saldoUsd = saldoUsd;
    }

    public Double getValorDolar() {
        return valorDolar;
    }

    public void setValorDolar(Double valorDolar) {
        this.valorDolar = valorDolar;
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
