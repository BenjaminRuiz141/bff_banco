package cl.duoc.auth.dto;

import java.util.Date;
import java.util.List;

public class TokenValidationResponseDTO {

    private boolean valido;
    private String usuario;
    private List<String> roles;
    private Date emitidoEn;
    private Date expiraEn;
    private String mensaje;

    public TokenValidationResponseDTO() {
    }

    public TokenValidationResponseDTO(boolean valido, String usuario, List<String> roles, Date emitidoEn, Date expiraEn, String mensaje) {
        this.valido = valido;
        this.usuario = usuario;
        this.roles = roles;
        this.emitidoEn = emitidoEn;
        this.expiraEn = expiraEn;
        this.mensaje = mensaje;
    }

    public static TokenValidationResponseDTO invalido(String mensaje) {
        TokenValidationResponseDTO dto = new TokenValidationResponseDTO();
        dto.setValido(false);
        dto.setMensaje(mensaje);
        return dto;
    }

    public boolean isValido() {
        return valido;
    }

    public void setValido(boolean valido) {
        this.valido = valido;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public Date getEmitidoEn() {
        return emitidoEn;
    }

    public void setEmitidoEn(Date emitidoEn) {
        this.emitidoEn = emitidoEn;
    }

    public Date getExpiraEn() {
        return expiraEn;
    }

    public void setExpiraEn(Date expiraEn) {
        this.expiraEn = expiraEn;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
