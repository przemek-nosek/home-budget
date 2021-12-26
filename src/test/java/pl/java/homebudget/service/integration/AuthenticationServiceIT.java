package pl.java.homebudget.service.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import pl.java.homebudget.dto.AuthenticationRequest;
import pl.java.homebudget.exception.AppUserInvalidUsernameOrPasswordException;
import pl.java.homebudget.service.impl.AuthenticationService;
import pl.java.homebudget.service.init.InitDataForIT;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@TestPropertySource("classpath:app.properties")
public class AuthenticationServiceIT extends InitDataForIT {

    private static final String JWT_PREFIX = "eyJhbGciOiJIUzI1NiJ9";

    @Autowired
    private AuthenticationService authenticationService;


    @Test
    void shouldGetAuthenticationToken() {
        //given
        AuthenticationRequest authenticationRequest = initDatabaseWithAuthenticationRequest();

        //when
        String authenticationToken = authenticationService.getAuthenticationToken(authenticationRequest);

        //then
        assertThat(authenticationToken).startsWith(JWT_PREFIX);
    }

    @Test
    void shouldNotGetToken_whenUsernameIsInvalid() {
        //given
        initDatabaseWithAuthenticationRequest();
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("invalid", PASSWORD);

        //when
        //then
        assertThrows(AppUserInvalidUsernameOrPasswordException.class, () -> authenticationService.getAuthenticationToken(authenticationRequest));
    }

    @Test
    void shouldNotGetToken_whenPasswordIsInvalid() {
        //given
        initDatabaseWithAuthenticationRequest();
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(USERNAME, "invalid");

        //when
        //then
        assertThrows(AppUserInvalidUsernameOrPasswordException.class, () -> authenticationService.getAuthenticationToken(authenticationRequest));
    }

}
