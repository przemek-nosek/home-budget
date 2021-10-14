package pl.java.homebudget.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.java.homebudget.dto.AuthenticationRequest;
import pl.java.homebudget.dto.AuthenticationResponse;
import pl.java.homebudget.service.impl.AppUserService;
import pl.java.homebudget.service.impl.AuthenticationService;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final AppUserService appUserService;

    @PostMapping
    public AuthenticationResponse getAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        return new AuthenticationResponse(authenticationService.getAuthenticationToken(authenticationRequest));
    }


    @PostMapping("/register")
    public Long saveUserDetails(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        return appUserService.saveUser(authenticationRequest);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUserAndHisAssets() { // TODO: DELETE USER AND ALL STUFF RELATED TO HIM
        appUserService.deleteUserAndHisAssets();

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
