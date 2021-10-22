package pl.java.homebudget.service.integration;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;
import pl.java.homebudget.dto.ExpenseDto;
import pl.java.homebudget.dto.UserLoggedInfo;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.entity.Expense;
import pl.java.homebudget.enums.ExpensesCategory;
import pl.java.homebudget.exception.ExpenseNotFoundException;
import pl.java.homebudget.mapper.ExpenseMapper;
import pl.java.homebudget.repository.AppUserRepository;
import pl.java.homebudget.repository.ExpenseRepository;
import pl.java.homebudget.service.ExpenseService;
import pl.java.homebudget.service.init.InitDataForIT;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class ExpenseServiceImplIT extends InitDataForIT {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private ExpenseService expenseService;

    private final ExpenseMapper expenseMapper = Mappers.getMapper(ExpenseMapper.class);

    @Test
    void shouldGetAllExpenses_AssociatedWithFirstUser() {
        //given
        initDatabase();

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
        initDatabase();
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
        initDatabase();
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
        initDatabase();
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
        initDatabase();
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
        initDatabase();
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




    private void initDatabase() {
        AppUser firstUser = initDatabaseWithUser();
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
}
