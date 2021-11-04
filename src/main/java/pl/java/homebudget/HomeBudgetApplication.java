package pl.java.homebudget;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.entity.Asset;
import pl.java.homebudget.entity.Property;
import pl.java.homebudget.entity.Room;
import pl.java.homebudget.enums.RoomSize;
import pl.java.homebudget.repository.*;

import java.math.BigDecimal;
import java.util.List;

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
//                                        RoomRepository roomRepository,
//                                        PropertyRepository propertyRepository,
//                                        BCryptPasswordEncoder bCryptPasswordEncoder) {
//
//
//        AppUser dingo1 = appUserRepository.findByUsername("dingo").get();
//        Room room = new Room(dingo1, RoomSize.ROOM_M, BigDecimal.TEN);
//        Room room2 = new Room(dingo1, RoomSize.ROOM_XL, BigDecimal.ONE);
//        Room room3 = new Room(dingo1, RoomSize.ROOM_L, BigDecimal.ZERO);
//
//
//        List<Room> room1 = List.of(room, room2, room3);
//        Property property = new Property(room1,true, "Szczecin", "512-12", "Ulica", "House", dingo1);
//
//        propertyRepository.save(property);
//
//
//        AppUser appUser = new AppUser("dingo1", "dingo1");
//        String admin = bCryptPasswordEncoder.encode("dingo1");
//        appUser.setPassword(admin);
//        appUserRepository.save(appUser);


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
