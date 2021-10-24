package pl.java.homebudget.service.integration;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import pl.java.homebudget.dto.ExpenseDto;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.entity.Expense;
import pl.java.homebudget.enums.ExpensesCategory;
import pl.java.homebudget.exception.ExpenseNotFoundException;
import pl.java.homebudget.mapper.ExpenseMapper;
import pl.java.homebudget.service.ExpenseService;
import pl.java.homebudget.service.init.InitDataForIT;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class ExpenseServiceImplIT extends InitDataForIT {

    @Autowired
    private ExpenseService expenseService;

    private final ExpenseMapper expenseMapper = Mappers.getMapper(ExpenseMapper.class);

    @Test
    void shouldGetAllExpenses_AssociatedWithFirstUser() {
        //given
        initDatabaseWithTwoUsersAndExpenses();

        //when
        List<ExpenseDto> expenses = expenseService.getExpenses();

        //then
        assertThat(expenses).hasSize(4);

        List<Expense> allExpenses = expenseRepository.findAll();
        assertThat(allExpenses).hasSize(6);
    }

    @Test
    void shouldAddExpense() {
        //given
        initDatabaseWithUser();
        ExpenseDto expenseDto = new ExpenseDto(1L, BigDecimal.ZERO, Instant.now(), ExpensesCategory.OTHER);

        //when
        ExpenseDto addedExpense = expenseService.addExpense(expenseDto);

        //then
        assertThat(addedExpense.getAmount()).isEqualTo(addedExpense.getAmount());
        assertThat(addedExpense.getPurchaseDate()).isEqualTo(addedExpense.getPurchaseDate());
        assertThat(addedExpense.getCategory()).isEqualTo(addedExpense.getCategory());
    }

    @Test
    void shouldDeleteExpense() {
        //given
        initDatabaseWithTwoUsersAndExpenses();
        List<Expense> expenses = expenseRepository.findAll();
        Expense expense = expenses.get(0);
        ExpenseDto expenseDto = expenseMapper.fromAssetToDto(expense);

        //when
        expenseService.deleteExpense(expenseDto);

        //then
        int size = expenseRepository.findAll().size();

        assertThat(size).isEqualTo(expenses.size() - 1);
    }

    @Test
    void shouldDeleteExpenseById() {
        //given
        initDatabaseWithTwoUsersAndExpenses();
        List<Expense> expenses = expenseRepository.findAll();
        Expense expense = expenses.get(0);

        Long id = expense.getId();

        //when
        expenseService.deleteExpenseById(id);

        //then
        assertThat(expenseRepository.existsById(id)).isFalse();
    }

    @Test
    void shouldNotDeleteById_WhenIdDoesNotExist() {
        //given
        Long notExistsId = -52L;


        //when
        //then
        assertThrows(ExpenseNotFoundException.class, () -> expenseService.deleteExpenseById(notExistsId));
    }

    @Test
    void shouldUpdateExpense() {
        //given
        initDatabaseWithTwoUsersAndExpenses();
        Expense expense = expenseRepository.findAll().get(0);

        ExpenseDto expenseDto = new ExpenseDto(expense.getId(), BigDecimal.valueOf(51283L), Instant.now(), ExpensesCategory.OTHER);

        //when
        ExpenseDto updatedExpense = expenseService.updateExpense(expenseDto);

        //then
        assertThat(updatedExpense.getAmount()).isEqualTo(expenseDto.getAmount());
        assertThat(updatedExpense.getCategory()).isEqualTo(expenseDto.getCategory());
    }

    @Test
    void shouldNotUpdateExpense_WhenItDoesNotExistById() {
        //given
        initDatabaseWithUser();
        Long notExistsId = -52L;
        ExpenseDto expenseDto = new ExpenseDto(notExistsId, BigDecimal.ZERO, Instant.now(), ExpensesCategory.OTHER);

        //when
        //then
        assertThrows(ExpenseNotFoundException.class, () -> expenseService.updateExpense(expenseDto));
    }

    @Test
    void shouldGetExpenseByCategory() {
        //given
        initDatabaseWithTwoUsersAndExpenses();
        ExpensesCategory expensesCategory = ExpensesCategory.OTHER;

        //when
        List<ExpenseDto> expensesByCategory = expenseService.getExpensesByCategory(expensesCategory);

        //then
        assertThat(expensesByCategory.size()).isNotZero();
        expensesByCategory
                .forEach(expenseDto -> assertThat(expenseDto.getCategory()).isEqualTo(expensesCategory));
    }

    @Test
    void shouldDeleteAllExpensesByAppUser() {
        //given
        initDatabaseWithTwoUsersAndExpenses();
        AppUser appUser = appUserRepository.findByUsername("user").get();
        //when
        List<Expense> expenses = expenseRepository.findAllByAppUser(appUser);
        assertThat(expenses).isNotEmpty();

        expenseService.deleteExpensesByAppUser();

        //then
        List<Expense> afterDeletion = expenseRepository.findAllByAppUser(appUser);
        assertThat(afterDeletion).isEmpty();

        List<Expense> allAssets = expenseRepository.findAll();
        assertThat(allAssets).isNotEmpty();
    }

    @Test
    void shouldGetExpensesWithinDate() {
        //given
        final String instantSuffix = "T00:00:00.000Z";
        AppUser appUser = initDatabaseWithUser();
        String firstDate = "2021-10-10";
        String secondDate = "2021-10-15";
        String thirdDate = "2021-10-20";
        String outOfRangeDate = "2015-01-09";

        initDatabaseWithExpenseAndUser(appUser, firstDate + instantSuffix);
        initDatabaseWithExpenseAndUser(appUser, secondDate + instantSuffix);
        initDatabaseWithExpenseAndUser(appUser, thirdDate + instantSuffix);
        initDatabaseWithExpenseAndUser(appUser, outOfRangeDate + instantSuffix);

        //when
        List<ExpenseDto> expensesWithinDate = expenseService.getExpensesWithinDate(firstDate, thirdDate);

        //then
        assertThat(expensesWithinDate).hasSize(3);

        List<String> datesWithinRange = expensesWithinDate.stream()
                .map(expenseDto -> expenseDto.getPurchaseDate().toString().substring(0, firstDate.length()))
                .collect(Collectors.toList());

        assertThat(datesWithinRange).containsExactly(firstDate, secondDate, thirdDate);
        assertThat(datesWithinRange).doesNotContain(outOfRangeDate);


    }

}
