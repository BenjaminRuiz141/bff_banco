package cl.duoc.bff.mobile.dto;

/**
 * DTO para el canal Móvil: Optimizado para transferencia ligera de datos,
 * reduciendo el consumo de ancho de banda celular (excluye edad).
 * Basado en las variables de intereses.csv (cuenta_id, nombre, saldo, tipo).
 */
public class CuentaMobileDTO {

    private Integer cuentaId;
    private String nombre;
    private Integer saldo;
    private String tipo;

    public CuentaMobileDTO() {
    }

    public CuentaMobileDTO(Integer cuentaId, String nombre, Integer saldo, String tipo) {
        this.cuentaId = cuentaId;
        this.nombre = nombre;
        this.saldo = saldo;
        this.tipo = tipo;
    }

    public Integer getCuentaId() {
        return cuentaId;
    }

    public void setCuentaId(Integer cuentaId) {
        this.cuentaId = cuentaId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
