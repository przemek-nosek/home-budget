package pl.java.homebudget;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:app.properties")
public class HomeBudgetApplication {

    public static void main(String[] args) {
        SpringApplication.run(HomeBudgetApplication.class, args);
    }

//    @Bean
//    CommandLineRunner commandLineRunner(AppUserRepository appUserRepository,
//                                        AssetRepository assetRepository,
//                                        ExpenseRepository expenseRepository,
//                                        BCryptPasswordEncoder bCryptPasswordEncoder) {
//
//        AppUser appUser = new AppUser("dingo1", "dingo1");
//        String admin = bCryptPasswordEncoder.encode("dingo1");
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
//
//        Expense expense = new Expense(BigDecimal.ZERO, Instant.now(), ExpensesCategory.OTHER, appUser);
//        Expense expense2 = new Expense(BigDecimal.TEN, Instant.now(), ExpensesCategory.EDUCATION, appUser);
//        Expense expense3 = new Expense(BigDecimal.TEN, Instant.now(), ExpensesCategory.FUN, appUser);
//        expenseRepository.save(expense);
//        expenseRepository.save(expense2);
//        expenseRepository.save(expense3);
//
//        return null;
//    }
}
