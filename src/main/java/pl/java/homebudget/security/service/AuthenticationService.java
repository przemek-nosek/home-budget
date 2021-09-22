package pl.java.homebudget.security.service;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    private final AuthenticationManager authenticationManager;

    public AuthenticationJWTToken getAuthenticationToken(AuthenticationUserDto authenticationUserDto) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationUserDto.getUsername(), authenticationUserDto.getPassword()
        ));

        UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationUserDto.getUsername());
        String jwtToken = jwtService.generateJWTToken(userDetails);


        return new AuthenticationJWTToken(jwtToken);
    }
}
