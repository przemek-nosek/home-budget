package pl.java.homebudget.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import pl.java.homebudget.dto.AuthenticationRequest;
import pl.java.homebudget.exception.AppUserInvalidUsernameOrPasswordException;
import pl.java.homebudget.service.impl.user.AppUserService;
import pl.java.homebudget.service.impl.AuthenticationService;
import pl.java.homebudget.service.impl.JwtService;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private AppUserService appUserService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    private final static String JWT_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTYzMzcxNTc4NywiZXhwIjoxNjMzNzI0NDI3LCJ1c2VyIjoiYWRtaW4ifQ.H-YBqtKSJRNdHY45kcgfHLExs9qRg4jWHeRA0LVXlVo";


    @Test
    void shouldGetAuthenticationToken() {
        //given
        AuthenticationRequest request = new AuthenticationRequest("user", "password");

        Authentication usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                request.getUsername(), request.getPassword()
        );

        UserDetails userDetails = new User(request.getUsername(), request.getPassword(), new ArrayList<>());

        given(authenticationManager.authenticate(usernamePasswordAuthenticationToken)).willReturn(usernamePasswordAuthenticationToken);
        given(appUserService.loadUserByUsername(anyString())).willReturn(userDetails);
        given(jwtService.generateToken(userDetails)).willReturn(JWT_TOKEN);

        //when
        String authenticationToken = authenticationService.getAuthenticationToken(request);

        //then
        assertThat(authenticationToken).startsWith(JWT_TOKEN);
        then(authenticationManager).should().authenticate(usernamePasswordAuthenticationToken);
        then(appUserService).should().loadUserByUsername("user");
        then(jwtService).should().generateToken(userDetails);
        then(authenticationManager).shouldHaveNoMoreInteractions();
        then(appUserService).shouldHaveNoMoreInteractions();
        then(jwtService).shouldHaveNoMoreInteractions();
    }

    @Test
    void getAuthenticationToken_shouldThrowAppUserInvalidUsernameOrPasswordException() {
        //given
        AuthenticationRequest request = new AuthenticationRequest("user", "password");
        Authentication usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                request.getUsername(), request.getPassword()
        );
        given(authenticationManager.authenticate(usernamePasswordAuthenticationToken)).willThrow(AppUserInvalidUsernameOrPasswordException.class);

        //when
        //then
        assertThrows(AppUserInvalidUsernameOrPasswordException.class, () -> authenticationService.getAuthenticationToken(request));
    }
}