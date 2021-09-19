package pl.java.homebudget.security.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import pl.java.homebudget.security.dto.AuthenticationJWTToken;
import pl.java.homebudget.security.dto.AuthenticationUserDto;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private final UserDetailsService userDetailsService;
    private final JWTService jwtService;

    public AuthenticationJWTToken getAuthenticationToken(AuthenticationUserDto authenticationUserDto) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationUserDto.getUsername());
        String jwtToken = jwtService.generateJWTToken(userDetails);

        return new AuthenticationJWTToken(jwtToken);
    }
}
