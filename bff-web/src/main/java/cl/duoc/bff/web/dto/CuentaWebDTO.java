package cl.duoc.bff.web.dto;

public class CuentaWebDTO {

    private Integer cuentaId;
    private String nombre;
    private Integer edad;
    private Integer saldo;
    private String tipo;

    public CuentaWebDTO() {
    }

    public CuentaWebDTO(Integer cuentaId, String nombre, Integer edad, Integer saldo, String tipo) {
        this.cuentaId = cuentaId;
        this.nombre = nombre;
        this.edad = edad;
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

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
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
