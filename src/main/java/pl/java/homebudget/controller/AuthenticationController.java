package pl.java.homebudget.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.java.homebudget.dto.AuthenticationRequest;
import pl.java.homebudget.dto.AuthenticationResponse;
import pl.java.homebudget.service.impl.AuthenticationService;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping
    public AuthenticationResponse getAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        return new AuthenticationResponse(authenticationService.getAuthenticationToken(authenticationRequest));
    }
}
