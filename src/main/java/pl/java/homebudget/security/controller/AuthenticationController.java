package pl.java.homebudget.security.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.java.homebudget.security.dto.AuthenticationJWTToken;
import pl.java.homebudget.security.dto.AuthenticationUserDto;
import pl.java.homebudget.security.service.AuthenticationService;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @GetMapping
    public AuthenticationJWTToken getAuthenticationToken(@RequestBody AuthenticationUserDto authenticationUserDto) {
        return authenticationService.getAuthenticationToken(authenticationUserDto);
    }
}
