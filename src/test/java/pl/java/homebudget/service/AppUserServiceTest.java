package pl.java.homebudget.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.java.homebudget.dto.AuthenticationRequest;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.exception.UsernameAlreadyExistsException;
import pl.java.homebudget.mapper.AppUserMapper;
import pl.java.homebudget.repository.AppUserRepository;
import pl.java.homebudget.service.impl.AppUserService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class AppUserServiceTest {

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private AppUserMapper appUserMapper;

    @InjectMocks
    private AppUserService appUserService;

    @Test
    void shouldLoadUserByUsername() {
        //given
        AppUser appUser = new AppUser("user", "password");
        given(appUserRepository.findByUsername(anyString())).willReturn(Optional.of(appUser));

        //when
        UserDetails userDetails = appUserService.loadUserByUsername(anyString());

        //then
        then(appUserRepository).should().findByUsername(anyString());
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(appUser.getUsername());
        assertThat(userDetails.getPassword()).isEqualTo(appUser.getPassword());
    }

    @Test
    void shouldNotLoadUserByUsername_andThrowUsernameNotFoundException() {
        //given
        given(appUserRepository.findByUsername(anyString())).willThrow(UsernameNotFoundException.class);
        String invalidUser = "invalid";

        //when
        //then
        assertThrows(UsernameNotFoundException.class, () -> appUserService.loadUserByUsername(invalidUser));
    }

    @Test
    void shouldSaveUser() {
        //given
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("user", "password");
        AppUser appUser = new AppUser("user", "password");
        appUser.setId(1L);

        given(appUserRepository.existsByUsername(anyString())).willReturn(false);
        given(appUserRepository.save(appUser)).willReturn(appUser);
        given(appUserMapper.fromDtoToAppUser(authenticationRequest)).willReturn(appUser);
        //when
        Long userId = appUserService.saveUser(authenticationRequest);

        //then
        then(appUserRepository).should().existsByUsername(anyString());
        then(appUserRepository).should().save(appUser);
        assertThat(userId).isNotNull();
        assertThat(userId).isEqualTo(1L);
    }

    @Test
    void shouldNotSaveUser_andThrowUsernameAlreadyExistsException() {
        //given
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("user", "password");
        given(appUserRepository.existsByUsername(anyString())).willReturn(true);

        //when
        //then
        assertThrows(UsernameAlreadyExistsException.class, () -> appUserService.saveUser(authenticationRequest));
    }
}