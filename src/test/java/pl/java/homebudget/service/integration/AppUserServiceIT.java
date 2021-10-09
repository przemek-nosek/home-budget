package pl.java.homebudget.service.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;
import pl.java.homebudget.dto.AuthenticationRequest;
import pl.java.homebudget.dto.UserLoggedInfo;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.entity.Asset;
import pl.java.homebudget.enums.AssetCategory;
import pl.java.homebudget.exception.UsernameAlreadyExistsException;
import pl.java.homebudget.repository.AppUserRepository;
import pl.java.homebudget.repository.AssetRepository;
import pl.java.homebudget.service.impl.AppUserService;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@WithMockUser(username = "user", password = "password")
class AppUserServiceIT {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private AssetRepository assetRepository;

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
        int assetRepositorySize = 4;

        //when
        List<Asset> assets = assetRepository.findAll();
        assertThat(assets).hasSize(assetRepositorySize);

        boolean existsByUsername = appUserRepository.existsByUsername(USERNAME);
        assertThat(existsByUsername).isTrue();

        appUserService.deleteUserAndHisAssets();

        //then
        boolean shouldNotExist = appUserRepository.existsByUsername(USERNAME);
        assertThat(shouldNotExist).isFalse();

        List<Asset> repositoryAll = assetRepository.findAll();

        assertThat(repositoryAll).hasSize(1);


    }

    private AppUser initDatabaseWithUser() {
        AppUser appUser = new AppUser(USERNAME, PASSWORD);
        return appUserRepository.save(appUser);
    }

    private AppUser initDatabaseWithSecondUser() {
        AppUser appUser = new AppUser("second", "second");
        return appUserRepository.save(appUser);
    }

    private void initDatabaseWithAssetsAndUser() {
        AppUser appUser = initDatabaseWithUser();

        Asset asset = new Asset(BigDecimal.ZERO, Instant.now(), AssetCategory.OTHER, appUser);
        Asset asset2 = new Asset(BigDecimal.TEN, Instant.now(), AssetCategory.BONUS, appUser);
        Asset asset3 = new Asset(BigDecimal.ONE, Instant.now(), AssetCategory.SALARY, appUser);
        assetRepository.save(asset);
        assetRepository.save(asset2);
        assetRepository.save(asset3);

        AppUser secondUser = initDatabaseWithSecondUser();
        Asset asset4 = new Asset(BigDecimal.ZERO, Instant.now(), AssetCategory.OTHER, secondUser);
        assetRepository.save(asset4);
    }
}