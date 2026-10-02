package cl.duoc.auth.dto;

import java.util.List;

public class AuthRequestDTO {

    private String usuario;
    private String rol;
    private List<String> roles;

    public AuthRequestDTO() {
    }

    public AuthRequestDTO(String usuario, String rol) {
        this.usuario = usuario;
        this.rol = rol;
    }

    public AuthRequestDTO(String usuario, List<String> roles) {
        this.usuario = usuario;
        this.roles = roles;
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

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
