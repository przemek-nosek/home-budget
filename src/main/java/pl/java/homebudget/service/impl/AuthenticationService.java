package pl.java.homebudget.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pl.java.homebudget.dto.AuthenticationRequest;
import pl.java.homebudget.exception.AppUserInvalidUsernameOrPasswordException;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private final JwtService jwtService;
    private final AppUserService appUserService;
    private final AuthenticationManager authenticationManager;

    public String getAuthenticationToken(AuthenticationRequest authenticationRequest) {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(), authenticationRequest.getPassword()
            ));
        } catch (BadCredentialsException | InternalAuthenticationServiceException e) {
            throw new AppUserInvalidUsernameOrPasswordException("Invalid username or password");
        }

        UserDetails userDetails = appUserService.loadUserByUsername(authenticationRequest.getUsername());

        return jwtService.generateToken(userDetails);
    }
}
