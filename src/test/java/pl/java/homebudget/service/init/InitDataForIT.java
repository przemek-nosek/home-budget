package pl.java.homebudget.service.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.repository.AppUserRepository;

@SpringBootTest
@Transactional
@WithMockUser(username = "user", password = "password")
public class InitDataForIT {

    @Autowired
    private AppUserRepository appUserRepository;

    private final static String USERNAME = "user";
    private final static String PASSWORD = "password";

    protected AppUser initDatabaseWithUser() {
        AppUser appUser = new AppUser(USERNAME, PASSWORD);
        return appUserRepository.save(appUser);
    }

    protected AppUser initDatabaseWithSecondUser() {
        AppUser appUser = new AppUser("second", "second");
        return appUserRepository.save(appUser);
    }
}
