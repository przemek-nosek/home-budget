package pl.java.homebudget;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.entity.Asset;
import pl.java.homebudget.enums.AssetCategory;
import pl.java.homebudget.repository.AppUserRepository;
import pl.java.homebudget.repository.AssetRepository;

import java.math.BigDecimal;
import java.time.Instant;

@SpringBootApplication
@PropertySource("classpath:app.properties")
public class HomeBudgetApplication {

    public static void main(String[] args) {
        SpringApplication.run(HomeBudgetApplication.class, args);
    }
//
//    @Bean
//    CommandLineRunner commandLineRunner(AppUserRepository appUserRepository, AssetRepository assetRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
//        AppUser appUser = new AppUser("dingo", "dingo");
//        String admin = bCryptPasswordEncoder.encode("dingo");
//        appUser.setPassword(admin);
//        appUserRepository.save(appUser);
//
//
//        Asset asset = new Asset(BigDecimal.ZERO, Instant.now(), AssetCategory.OTHER, appUser);
//        Asset asset2 = new Asset(BigDecimal.TEN, Instant.now(), AssetCategory.BONUS, appUser);
//        Asset asset3 = new Asset(BigDecimal.ONE, Instant.now(), AssetCategory.SALARY, appUser);
//        assetRepository.save(asset);
//        assetRepository.save(asset2);
//        assetRepository.save(asset3);
//
//        return null;
//    }
}
