package pl.java.homebudget.mapper;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.java.homebudget.dto.AuthenticationRequest;
import pl.java.homebudget.entity.AppUser;

@Component
@AllArgsConstructor
public class AppUserMapper {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AppUser fromDtoToAppUser(AuthenticationRequest authenticationRequest) {
        if (authenticationRequest == null) {
            return null;
        }

        AppUser appUser = new AppUser();

        appUser.setUsername(authenticationRequest.getUsername());
        appUser.setPassword(bCryptPasswordEncoder.encode(authenticationRequest.getPassword()));

        return appUser;
    }
}
