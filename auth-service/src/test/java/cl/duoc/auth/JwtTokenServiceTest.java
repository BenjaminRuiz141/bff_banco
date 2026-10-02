package cl.duoc.auth;

import cl.duoc.auth.service.JwtTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenServiceTest {

    private JwtTokenService jwtTokenService;

    @BeforeEach
    void setUp() {
        jwtTokenService = new JwtTokenService();
        ReflectionTestUtils.setField(jwtTokenService, "secretKey",
                "TestSecretKeyForUnitTestingOnlyMustBe256BitsLongSecureKey!");
        ReflectionTestUtils.setField(jwtTokenService, "expirationTime", 36000000L);
    }

    @Test
    void testGenerarYValidarTokenConRolesMultiples() {
        String usuario = "octocat_github";
        List<String> roles = List.of("ROLE_WEB", "ROLE_MOBILE", "ROLE_ATM");

        String token = jwtTokenService.generarToken(usuario, roles);

        assertNotNull(token);
        assertTrue(jwtTokenService.validarToken(token));
        assertEquals(usuario, jwtTokenService.extraerUsuario(token));

        List<String> rolesExtraidos = jwtTokenService.extraerRoles(token);
        assertEquals(3, rolesExtraidos.size());
        assertTrue(rolesExtraidos.contains("ROLE_WEB"));
        assertTrue(rolesExtraidos.contains("ROLE_MOBILE"));
        assertTrue(rolesExtraidos.contains("ROLE_ATM"));
    }

    @Test
    void testGenerarTokenConRolUnico() {
        String usuario = "cajero_01";
        String token = jwtTokenService.generarToken(usuario, "ATM");

        assertNotNull(token);
        assertTrue(jwtTokenService.validarToken(token));
        assertEquals(usuario, jwtTokenService.extraerUsuario(token));

        List<String> rolesExtraidos = jwtTokenService.extraerRoles(token);
        assertTrue(rolesExtraidos.contains("ROLE_ATM"));
    }

    @Test
    void testTokenInvalido() {
        assertFalse(jwtTokenService.validarToken("token_corrupto_o_falso"));
    }
}
