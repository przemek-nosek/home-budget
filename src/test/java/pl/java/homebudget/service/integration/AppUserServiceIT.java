package pl.java.homebudget.service.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.java.homebudget.dto.AuthenticationRequest;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.entity.Asset;
import pl.java.homebudget.entity.Expense;
import pl.java.homebudget.enums.AssetCategory;
import pl.java.homebudget.enums.ExpensesCategory;
import pl.java.homebudget.exception.UsernameAlreadyExistsException;
import pl.java.homebudget.repository.AppUserRepository;
import pl.java.homebudget.repository.AssetRepository;
import pl.java.homebudget.repository.ExpenseRepository;
import pl.java.homebudget.service.impl.AppUserService;
import pl.java.homebudget.service.init.InitDataForIT;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


class AppUserServiceIT extends InitDataForIT {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    private final static String BCRYPT_PREFIX = "$2a$10$";
    private final static String BCRYPT_REGEX = "^[$]2[abxy]?[$](?:0[4-9]|[12][0-9]|3[01])[$][./0-9a-zA-Z]{53}$";

    private final static String USERNAME = "user";
    private final static String PASSWORD = "password";

    @Test
    void shouldLoadUserByUsername() {
        //given
        initDatabaseWithUser();

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
        initDatabaseWithUser();
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(USERNAME, PASSWORD);

        //when
        //then
        assertThrows(UsernameAlreadyExistsException.class, () -> appUserService.saveUser(authenticationRequest));
    }

    @Test
    void shouldDeleteUserAndAllHIsAssets() {
        //given
        initDatabaseWithAssetsAndUser();

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


    private void initDatabaseWithAssetsAndUser() {
        AppUser appUser = initDatabaseWithUser();

        Asset asset = new Asset(BigDecimal.ZERO, Instant.now(), AssetCategory.OTHER, appUser);
        Asset asset2 = new Asset(BigDecimal.TEN, Instant.now(), AssetCategory.BONUS, appUser);
        Asset asset3 = new Asset(BigDecimal.ONE, Instant.now(), AssetCategory.SALARY, appUser);
        assetRepository.save(asset);
        assetRepository.save(asset2);
        assetRepository.save(asset3);

        Expense expense = new Expense(BigDecimal.ZERO, Instant.now(), ExpensesCategory.OTHER, appUser);
        Expense expense2 = new Expense(BigDecimal.TEN, Instant.now(), ExpensesCategory.FUN, appUser);
        Expense expense3 = new Expense(BigDecimal.ONE, Instant.now(), ExpensesCategory.OTHER, appUser);
        expenseRepository.save(expense);
        expenseRepository.save(expense2);
        expenseRepository.save(expense3);

        AppUser secondUser = initDatabaseWithSecondUser();
        Asset asset4 = new Asset(BigDecimal.ZERO, Instant.now(), AssetCategory.OTHER, secondUser);
        assetRepository.save(asset4);

        Expense expense4 = new Expense(BigDecimal.ONE, Instant.now(), ExpensesCategory.OTHER, secondUser);
        expenseRepository.save(expense4);
    }
}