package cl.duoc.bff.web.dto;

public class AuthRequestDTO {

    private String usuario;
    private String rol;

    public AuthRequestDTO() {
    }

    public AuthRequestDTO(String usuario, String rol) {
        this.usuario = usuario;
        this.rol = rol;
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
}
