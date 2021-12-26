package pl.java.homebudget.service.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;
import pl.java.homebudget.dto.AuthenticationRequest;
import pl.java.homebudget.entity.*;
import pl.java.homebudget.enums.AssetCategory;
import pl.java.homebudget.enums.ExpensesCategory;
import pl.java.homebudget.enums.RoomSize;
import pl.java.homebudget.repository.*;
import pl.java.homebudget.service.impl.user.AppUserService;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
@WithMockUser(username = "user", password = "password")
public abstract class InitDataForIT {

    @Autowired
    protected AppUserRepository appUserRepository;

    @Autowired
    protected AssetRepository assetRepository;

    @Autowired
    protected ExpenseRepository expenseRepository;

    @Autowired
    protected PropertyRepository propertyRepository;

    @Autowired
    protected RoomRepository roomRepository;

    @Autowired
    protected AppUserService appUserService;

    @Autowired
    protected ExpenseEstimatePercentageRepository expenseEstimatePercentageRepository;

    protected final static String USERNAME = "user";
    protected final static String PASSWORD = "password";

    protected AppUser initDatabaseWithFirstUser() {
        AppUser appUser = new AppUser(USERNAME, PASSWORD);
        return appUserRepository.save(appUser);
    }

    protected AppUser initDatabaseWithSecondUser() {
        AppUser appUser = new AppUser("second", "second");
        return appUserRepository.save(appUser);
    }

    protected AuthenticationRequest initDatabaseWithAuthenticationRequest() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(USERNAME, PASSWORD);
        appUserService.saveUser(authenticationRequest);
        return authenticationRequest;
    }

    protected void initDatabaseWithTwoUsersAndAssets() {
        AppUser firstUser = initDatabaseWithFirstUser();
        List<Asset> assets = new ArrayList<>();
        assets.add(new Asset(BigDecimal.ZERO, Instant.now(), AssetCategory.OTHER, firstUser));
        assets.add(new Asset(BigDecimal.ONE, Instant.now(), AssetCategory.SALARY, firstUser));
        assets.add(new Asset(BigDecimal.TEN, Instant.now(), AssetCategory.BONUS, firstUser));
        assets.add(new Asset(BigDecimal.valueOf(105L), Instant.now(), AssetCategory.BONUS, firstUser));

        AppUser secondUser = initDatabaseWithSecondUser();
        assets.add(new Asset(BigDecimal.valueOf(150L), Instant.now(), AssetCategory.BONUS, secondUser));
        assets.add(new Asset(BigDecimal.valueOf(300L), Instant.now(), AssetCategory.LOAN_RETURNED, secondUser));

        assetRepository.saveAll(assets);
    }

    protected void initDatabaseWithTwoUsersAndExpenses() {
        AppUser firstUser = initDatabaseWithFirstUser();
        List<Expense> expenses = new ArrayList<>();


        expenses.add(new Expense(BigDecimal.ZERO, Instant.now(), ExpensesCategory.OTHER, firstUser));
        expenses.add(new Expense(BigDecimal.ONE, Instant.now(), ExpensesCategory.EDUCATION, firstUser));
        expenses.add(new Expense(BigDecimal.TEN, Instant.now(), ExpensesCategory.FUN, firstUser));
        expenses.add(new Expense(BigDecimal.valueOf(105L), Instant.now(), ExpensesCategory.EDUCATION, firstUser));

        AppUser secondUser = initDatabaseWithSecondUser();
        expenses.add(new Expense(BigDecimal.valueOf(150L), Instant.now(), ExpensesCategory.EDUCATION, secondUser));
        expenses.add(new Expense(BigDecimal.valueOf(300L), Instant.now(), ExpensesCategory.FOR_LIFE, secondUser));

        expenseRepository.saveAll(expenses);
    }

    protected void initDatabaseWithExpenseAndUser(AppUser appUser, String date) {
        final String instantFromDateSuffix = "T00:00:00.000Z";
        Expense expense = new Expense(BigDecimal.ZERO, Instant.parse(date + instantFromDateSuffix), ExpensesCategory.OTHER, appUser);
        expenseRepository.save(expense);
    }

    protected ExpenseEstimatePercentage initDatabaseWithExpenseEstimateAndUser(AppUser appUser, ExpenseEstimatePercentage expenseEstimatePercentage) {
        return expenseEstimatePercentageRepository.save(expenseEstimatePercentage);
    }

    protected Room initDatabaseWithRoomAndUser(AppUser appUser) {
        Room room = new Room(appUser, RoomSize.ROOM_XXL, BigDecimal.TEN);
        roomRepository.save(room);
        return room;
    }

    protected void initDatabaseWithExpenseAndUserByCategory(AppUser appUser, String date, ExpensesCategory category) {
        final String instantFromDateSuffix = "T00:00:00.000Z";
        Expense expense = new Expense(BigDecimal.ZERO, Instant.parse(date + instantFromDateSuffix), category, appUser);
        expenseRepository.save(expense);
    }

    protected void initDatabaseWithAssetAndUser(AppUser appUser, String date) {
        final String instantFromDateSuffix = "T00:00:00.000Z";
        Asset asset = new Asset(BigDecimal.ZERO, Instant.parse(date + instantFromDateSuffix), AssetCategory.OTHER, appUser);
        assetRepository.save(asset);
    }

    protected void initDatabaseWithPropertyAndUser(AppUser appUser, Boolean sold) {
        Property property = new Property(appUser, new ArrayList<>(), true, "szczecin", "11-111", "nowa", "15", sold);
        propertyRepository.save(property);
    }

    protected Room initDatabaseWithRoomAndUser(Room room) {
        return roomRepository.save(room);
    }

    protected List<Room> initDatabaseWithRoomsAndUser(List<Room> rooms) {
        return roomRepository.saveAll(rooms);
    }

    protected Property initDatabaseWithPropertyAndUser(Property property) {
        return propertyRepository.save(property);
    }

    protected void initDatabaseWithTwoUsersAndRelatedData() {
        AppUser firstUser = initDatabaseWithFirstUser();

        Asset asset = new Asset(BigDecimal.ZERO, Instant.now(), AssetCategory.OTHER, firstUser);
        Asset asset2 = new Asset(BigDecimal.TEN, Instant.now(), AssetCategory.BONUS, firstUser);
        Asset asset3 = new Asset(BigDecimal.ONE, Instant.now(), AssetCategory.SALARY, firstUser);
        assetRepository.saveAll(List.of(asset, asset2, asset3));

        Expense expense = new Expense(BigDecimal.ZERO, Instant.now(), ExpensesCategory.OTHER, firstUser);
        Expense expense2 = new Expense(BigDecimal.TEN, Instant.now(), ExpensesCategory.FUN, firstUser);
        Expense expense3 = new Expense(BigDecimal.ONE, Instant.now(), ExpensesCategory.OTHER, firstUser);
        expenseRepository.saveAll(List.of(expense, expense2, expense3));


        AppUser secondUser = initDatabaseWithSecondUser();
        Asset asset4 = new Asset(BigDecimal.ZERO, Instant.now(), AssetCategory.OTHER, secondUser);
        assetRepository.save(asset4);

        Expense expense4 = new Expense(BigDecimal.ONE, Instant.now(), ExpensesCategory.OTHER, secondUser);
        expenseRepository.save(expense4);
    }
}
