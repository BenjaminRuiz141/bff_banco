package cl.duoc.bff.web.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "interes")
public class Cuenta {

    @Id
    @Column(name = "id")
    private Integer cuentaId;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "saldo", nullable = false)
    private Integer saldo;

    @Column(name = "edad", nullable = false)
    private Integer edad;

    @Column(name = "tipo", nullable = false)
    private String tipo;

    public Cuenta() {
    }

    public Cuenta(Integer cuentaId, String nombre, Integer saldo, Integer edad, String tipo) {
        this.cuentaId = cuentaId;
        this.nombre = nombre;
        this.saldo = saldo;
        this.edad = edad;
        this.tipo = tipo;
    }

    public Integer getCuentaId() {
        return cuentaId;
    }

    public void setCuentaId(Integer cuentaId) {
        this.cuentaId = cuentaId;
    }

    public Integer getId() {
        return cuentaId;
    }

    public void setId(Integer id) {
        this.cuentaId = id;
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

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
