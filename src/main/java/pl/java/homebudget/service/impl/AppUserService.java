package pl.java.homebudget.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.java.homebudget.dto.AuthenticationRequest;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.exception.UsernameAlreadyExistsException;
import pl.java.homebudget.mapper.AppUserMapper;
import pl.java.homebudget.repository.AppUserRepository;

import java.util.ArrayList;

@Service
@AllArgsConstructor
@Slf4j
public class AppUserService implements UserDetailsService {

    private final AppUserRepository appUserRepository;
    private final AppUserMapper appUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Searching user {}", username);
        AppUser appUser = appUserRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error(String.format("Username [%s] not found", username));
                    throw new UsernameNotFoundException(String.format("Username [%s] not found", username));
                });

        return new User(appUser.getUsername(), appUser.getPassword(), new ArrayList<>());
    }

    public Long saveUser(AuthenticationRequest authenticationRequest) {
        log.info("AuthenticationRequest deatails {}", authenticationRequest);
        String username = authenticationRequest.getUsername();

        validateIfUserExists(username);

        // TODO: Validate credentials

        AppUser appUser = appUserMapper.fromDtoToAppUser(authenticationRequest);

        AppUser savedAppUser = appUserRepository.save(appUser);

        log.info("AppUser saved with id: {}", savedAppUser.getId());
        return savedAppUser.getId();
    }

    private void validateIfUserExists(String username) {
        boolean existsByUsername = appUserRepository.existsByUsername(username);
        if (existsByUsername) {
            log.error(String.format("Username [%s] already exists", username));
            throw new UsernameAlreadyExistsException(String.format("Username [%s] already exists", username));
        }
    }
}
