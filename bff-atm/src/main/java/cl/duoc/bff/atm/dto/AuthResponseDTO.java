package cl.duoc.bff.atm.dto;

public class AuthResponseDTO {

    private String token;
    private String usuario;
    private String rol;
    private String tipo = "Bearer";

    public AuthResponseDTO() {
    }

    public AuthResponseDTO(String token, String usuario, String rol) {
        this.token = token;
        this.usuario = usuario;
        this.rol = rol;
        this.tipo = "Bearer";
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
