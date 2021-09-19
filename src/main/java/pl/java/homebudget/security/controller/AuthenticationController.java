package pl.java.homebudget.security.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.java.homebudget.security.dto.AuthenticationJwtToken;
import pl.java.homebudget.security.dto.AuthenticationUserDto;
import pl.java.homebudget.security.service.AuthenticationService;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationJwtToken getAuthenticationToken(AuthenticationUserDto authenticationUserDto) {
        return authenticationService.createAuthenticationToken(authenticationUserDto);
    }
}
