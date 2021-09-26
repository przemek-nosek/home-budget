package pl.java.homebudget.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import pl.java.homebudget.dto.AuthenticationRequest;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.exception.UsernameAlreadyExistsException;
import pl.java.homebudget.repository.AppUserRepository;
import pl.java.homebudget.service.impl.AppUserService;

import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class AppUserServiceIT {

    @Autowired
    private AppUserService service;

    @Autowired
    private AppUserRepository repository;

    private final static String BCRYPT_PREFIX = "$2a$10$";
    private final static String BCRYPT_REGEX = "^[$]2[abxy]?[$](?:0[4-9]|[12][0-9]|3[01])[$][./0-9a-zA-Z]{53}$";

    @Test
    void shouldReturnUsernameAndPasswordFromDatabase() {
        //given
        initDatabase();

        //when
        UserDetails ada = service.loadUserByUsername("ada");

        //then
        assertThat(ada).isNotNull();
        assertThat(ada.getUsername()).isEqualToIgnoringWhitespace("ada");
        assertThat(ada.getPassword()).isEqualToIgnoringWhitespace("ada");
    }

    @Test
    void shouldSaveUserInToDatabase() {
        //given
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("mada", "mada");

        //when
        service.saveUser(authenticationRequest);

        //then
        AppUser appUser = repository.findByUsername("mada").get();

        assertThat(appUser).isNotNull();
        assertThat(appUser.getUsername()).isEqualToIgnoringWhitespace("mada");
        assertThat(appUser.getPassword()).contains(BCRYPT_PREFIX);
        assertThat(appUser.getPassword()).containsPattern(Pattern.compile(BCRYPT_REGEX));
    }

    @Test
    void shouldThrowUsernameNotFoundException() {
        //given
        String username = "userNotExists";

        //when
        //then
        assertThrows(UsernameNotFoundException.class,
                () -> service.loadUserByUsername(username));
    }

    @Test
    void shouldThrowUsernameAlreadyExistsException() {
        //given
        initDatabase();
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("ada", "ada");

        //when
        //then
        assertThrows(UsernameAlreadyExistsException.class,
                () -> service.saveUser(authenticationRequest));
    }

    private void initDatabase() {
        AppUser appUser = new AppUser(5L, "ada", "ada");
        repository.save(appUser);
    }
}
