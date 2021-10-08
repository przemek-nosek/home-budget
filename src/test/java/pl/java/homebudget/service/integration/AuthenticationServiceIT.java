package pl.java.homebudget.service.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import pl.java.homebudget.dto.AuthenticationRequest;
import pl.java.homebudget.exception.AppUserInvalidUsernameOrPasswordException;
import pl.java.homebudget.service.impl.AppUserService;
import pl.java.homebudget.service.impl.AuthenticationService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@TestPropertySource("classpath:app.properties")
public class AuthenticationServiceIT {

    private static final String USER = "user";
    private static final String PASSWORD = "password";
    private static final String JWT_PREFIX = "eyJhbGciOiJIUzI1NiJ9";
    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private AppUserService appUserService;

    @Test
    void shouldGetAuthenticationToken() {
        //given
        AuthenticationRequest authenticationRequest = initDatabase();

        //when
        String authenticationToken = authenticationService.getAuthenticationToken(authenticationRequest);

        //then
        assertThat(authenticationToken).startsWith(JWT_PREFIX);
    }

    @Test
    void shouldNotGetToken_whenUsernameIsInvalid() {
        //given
        initDatabase();
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("invalid", PASSWORD);

        //when
        //then
        assertThrows(AppUserInvalidUsernameOrPasswordException.class, () -> authenticationService.getAuthenticationToken(authenticationRequest));
    }

    @Test
    void shouldNotGetToken_whenPasswordIsInvalid() {
        //given
        initDatabase();
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(USER, "invalid");

        //when
        //then
        assertThrows(AppUserInvalidUsernameOrPasswordException.class, () -> authenticationService.getAuthenticationToken(authenticationRequest));
    }

    private AuthenticationRequest initDatabase() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(USER, PASSWORD);
        appUserService.saveUser(authenticationRequest);
        return authenticationRequest;
    }
}
