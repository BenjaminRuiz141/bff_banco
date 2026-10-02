package cl.duoc.auth.security;

import cl.duoc.auth.service.JwtTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger log = LoggerFactory.getLogger(OAuth2AuthenticationSuccessHandler.class);
    private final JwtTokenService jwtTokenService;

    public OAuth2AuthenticationSuccessHandler(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        Object loginAttr = attributes.get("login");
        String usuario = (loginAttr != null && !loginAttr.toString().isBlank())
                ? loginAttr.toString()
                : "usuario_github";

        List<String> roles = List.of("ROLE_WEB", "ROLE_MOBILE", "ROLE_ATM");
        String token = jwtTokenService.generarToken(usuario, roles);

        log.info("Autenticacion GitHub exitosa para usuario: {}. Token emitido con roles: {}",
                usuario, roles);

        response.setContentType("text/html;charset=UTF-8");
        String html = """
                <!DOCTYPE html>
                <html lang="es">
                <head>
                    <meta charset="UTF-8">
                    <title>Autenticacion Exitosa</title>
                </head>
                <body>
                    <h2>Autenticacion Exitosa con GitHub</h2>
                    <p><strong>Usuario:</strong> {{USUARIO}}</p>
                    <p><strong>Roles:</strong> ROLE_WEB, ROLE_MOBILE, ROLE_ATM</p>
                    <p><strong>Token JWT:</strong></p>
                    <textarea rows="6" cols="100" readonly onclick="this.select()">{{TOKEN}}</textarea>
                </body>
                </html>
                """
                .replace("{{USUARIO}}", HtmlUtils.htmlEscape(usuario))
                .replace("{{TOKEN}}", token);

        response.getWriter().write(html);
    }
}
