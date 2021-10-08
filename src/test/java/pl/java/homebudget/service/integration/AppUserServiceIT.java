package pl.java.homebudget.service.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import pl.java.homebudget.dto.AuthenticationRequest;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.exception.UsernameAlreadyExistsException;
import pl.java.homebudget.mapper.AppUserMapper;
import pl.java.homebudget.repository.AppUserRepository;
import pl.java.homebudget.service.impl.AppUserService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class AppUserServiceIT {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private AppUserMapper appUserMapper;

    @Autowired
    private AppUserService appUserService;

    private final static String BCRYPT_PREFIX = "$2a$10$";
    private final static String BCRYPT_REGEX = "^[$]2[abxy]?[$](?:0[4-9]|[12][0-9]|3[01])[$][./0-9a-zA-Z]{53}$";

    private final static String USERNAME = "user";
    private final static String PASSWORD = "password";

    @Test
    void shouldLoadUserByUsername() {
        //given
        initDatabase();

        //when
        UserDetails userDetails = appUserService.loadUserByUsername(USERNAME);

        //then
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualToIgnoringWhitespace(USERNAME);
        assertThat(userDetails.getPassword()).isEqualToIgnoringWhitespace(PASSWORD);
    }

    @Test
    void shouldNotLoadUserByUsername_andThrowUsernameNotFoundException() {
        //given
        String username = "INVALID_USER";

        //when
        //then
        assertThrows(UsernameNotFoundException.class, () -> appUserService.loadUserByUsername(username));
    }

    @Test
    void shouldSaveUser() {
        //given
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(USERNAME, PASSWORD);

        //when
        appUserService.saveUser(authenticationRequest);

        //then
        AppUser appUser = appUserRepository.findByUsername(USERNAME).get();

        assertThat(appUser).isNotNull();
        assertThat(appUser.getPassword()).startsWith(BCRYPT_PREFIX);
        assertThat(appUser.getPassword()).containsPattern(BCRYPT_REGEX);
        assertThat(appUser.getUsername()).isEqualTo(USERNAME);
    }

    @Test
    void shouldNotSaveUser_andThrowUsernameAlreadyExistsException() {
        //given
        initDatabase();
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(USERNAME, PASSWORD);

        //when
        //then
        assertThrows(UsernameAlreadyExistsException.class, () -> appUserService.saveUser(authenticationRequest));
    }

    private void initDatabase() {
        AppUser appUser = new AppUser(USERNAME, PASSWORD);
        appUserRepository.save(appUser);
    }
}