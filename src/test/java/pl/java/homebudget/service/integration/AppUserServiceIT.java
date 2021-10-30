package pl.java.homebudget.service.integration;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.java.homebudget.dto.AuthenticationRequest;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.entity.Asset;
import pl.java.homebudget.entity.Expense;
import pl.java.homebudget.exception.UsernameAlreadyExistsException;
import pl.java.homebudget.service.init.InitDataForIT;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


class AppUserServiceIT extends InitDataForIT {

    private final static String BCRYPT_PREFIX = "$2a$10$";
    private final static String BCRYPT_REGEX = "^[$]2[abxy]?[$](?:0[4-9]|[12][0-9]|3[01])[$][./0-9a-zA-Z]{53}$";

    @Test
    void shouldLoadUserByUsername() {
        //given
        initDatabaseWithFirstUser();

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
        initDatabaseWithFirstUser();
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(USERNAME, PASSWORD);

        //when
        //then
        assertThrows(UsernameAlreadyExistsException.class, () -> appUserService.saveUser(authenticationRequest));
    }

    @Test
    void shouldDeleteUserAndAllHIsAssets() {
        //given
        initDatabaseWithTwoUsersAndRelatedData();

        AppUser appUser = appUserRepository.findByUsername(USERNAME).get();

        List<Asset> assets = assetRepository.findAllByAppUser(appUser);
        List<Expense> expenses = expenseRepository.findAllByAppUser(appUser);

        assertThat(assets).isNotEmpty();
        assertThat(expenses).isNotEmpty();

        //when
        appUserService.deleteUserAndAllHisData();

        //then
        assertThat(appUserRepository.existsByUsername(USERNAME)).isFalse();
        assertThat(assetRepository.findAllByAppUser(appUser)).isEmpty();
        assertThat(expenseRepository.findAllByAppUser(appUser)).isEmpty();

    }
}