package cl.duoc.auth.dto;

import java.util.List;

public class AuthResponseDTO {

    private String token;
    private String tipo = "Bearer";
    private String usuario;
    private String rol;
    private List<String> roles;
    private long expiracionMs;
    private String mensaje;

    public AuthResponseDTO() {
    }

    public AuthResponseDTO(String token, String usuario, String rol) {
        this.token = token;
        this.usuario = usuario;
        this.rol = rol;
        this.roles = List.of(rol);
    }

    public AuthResponseDTO(String token, String usuario, List<String> roles, long expiracionMs) {
        this.token = token;
        this.usuario = usuario;
        this.roles = roles;
        this.rol = String.join(",", roles);
        this.expiracionMs = expiracionMs;
    }

    public AuthResponseDTO(String token, String usuario, List<String> roles, long expiracionMs, String mensaje) {
        this.token = token;
        this.usuario = usuario;
        this.roles = roles;
        this.rol = String.join(",", roles);
        this.expiracionMs = expiracionMs;
        this.mensaje = mensaje;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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

    public long getExpiracionMs() {
        return expiracionMs;
    }

    public void setExpiracionMs(long expiracionMs) {
        this.expiracionMs = expiracionMs;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
