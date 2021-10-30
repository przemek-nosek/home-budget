package pl.java.homebudget.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.java.homebudget.dto.ExpenseDto;
import pl.java.homebudget.dto.UserLoggedInfo;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.entity.Expense;
import pl.java.homebudget.enums.ExpensesCategory;
import pl.java.homebudget.exception.AssetNotFoundException;
import pl.java.homebudget.exception.ExpenseNotFoundException;
import pl.java.homebudget.exception.InvalidDateFormatException;
import pl.java.homebudget.exception.MissingExpenseFilterSettingException;
import pl.java.homebudget.filter.FilterRange;
import pl.java.homebudget.mapper.ExpenseMapper;
import pl.java.homebudget.repository.ExpenseRepository;
import pl.java.homebudget.service.impl.ExpenseServiceImpl;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceImplTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private UserLoggedInfo userLoggedInfo;

    @InjectMocks
    private ExpenseServiceImpl expenseService;

    @Mock
    private ExpenseMapper expenseMapper;

    @Mock
    private FilterRange<Expense> expenseFilterRange;

    @Test
    void shouldGetAllExpenses() {
        //given
        AppUser appUser = getAppUser();
        List<Expense> expenseList = List.of(
                new Expense(BigDecimal.ZERO, Instant.now(), ExpensesCategory.OTHER, appUser),
                new Expense(BigDecimal.ONE, Instant.now(), ExpensesCategory.EDUCATION, appUser),
                new Expense(BigDecimal.TEN, Instant.now(), ExpensesCategory.FUN, appUser)
        );

        given(userLoggedInfo.getLoggedAppUser()).willReturn(any());
        given(expenseRepository.findAllByAppUser(appUser)).willReturn(expenseList);

        //when
        List<ExpenseDto> expenses = expenseService.getExpenses();

        //then
        assertThat(expenses).hasSize(expenseList.size());
        then(userLoggedInfo).should().getLoggedAppUser();
        then(expenseRepository).should().findAllByAppUser(any());

    }

    @Test
    void shouldAddExpense() {
        //given
        ExpenseDto expenseDto = new ExpenseDto(1L, BigDecimal.ZERO, Instant.now(), ExpensesCategory.OTHER);
        AppUser appUser = getAppUser();
        Expense expense = new Expense(BigDecimal.ZERO, Instant.now(), ExpensesCategory.OTHER, appUser);

        given(expenseRepository.save(any())).willReturn(expense);

        //when
        ExpenseDto addedExpenseDto = expenseService.addExpense(expenseDto);

        //then
        then(expenseRepository).should().save(any());
        assertThat(addedExpenseDto).isNotNull();
        assertThat(addedExpenseDto.getAmount()).isEqualTo(expense.getAmount());
        assertThat(addedExpenseDto.getPurchaseDate()).isEqualTo(expense.getPurchaseDate());
        assertThat(addedExpenseDto.getCategory()).isEqualTo(expense.getCategory());
    }

    @Test
    void shouldGetExpensesByCategory() {
        //given
        ExpensesCategory expensesCategory = ExpensesCategory.OTHER;
        AppUser appUser = getAppUser();
        List<Expense> expenseList = List.of(
                new Expense(BigDecimal.ZERO, Instant.now(), ExpensesCategory.OTHER, appUser),
                new Expense(BigDecimal.TEN, Instant.now(), ExpensesCategory.OTHER, appUser)
        );

        given(userLoggedInfo.getLoggedAppUser()).willReturn(appUser);
        given(expenseRepository.findAllByCategoryAndAppUser(expensesCategory, appUser)).willReturn(expenseList);

        //when
        List<ExpenseDto> expensesByCategory = expenseService.getExpensesByCategory(expensesCategory);

        //then
        assertThat(expensesByCategory).hasSize(2);
        expensesByCategory
                .forEach(expenseDto -> assertThat(expenseDto.getCategory()).isEqualTo(expensesCategory));
    }

    @Test
    void shouldDeleteExpense() {
        //given
        ExpenseDto expenseDto = new ExpenseDto();

        //when
        expenseService.deleteExpense(expenseDto);

        //then
        then(expenseRepository).should().delete(any());
    }

    @Test
    void shouldDeleteExpenseById_successfully_whenAssetExistsById() {
        //given
        Long id = 1L;
        given(expenseRepository.existsById(anyLong())).willReturn(true);

        //when
        expenseService.deleteExpenseById(id);

        //then
        then(expenseRepository).should().deleteById(anyLong());
    }

    @Test
    void deleteExpenseById_fails_whenAssetDoesNotExistsById_andThrow_AssetNotFoundException() {
        //given
        given(expenseRepository.existsById(anyLong())).willReturn(false);

        //when
        //then
        assertThrows(ExpenseNotFoundException.class, () -> expenseService.deleteExpenseById(anyLong()));
        then(expenseRepository).should().existsById(anyLong());
        then(expenseRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void shouldDeleteExpenseByAppUser() {
        //given
        //when
        expenseService.deleteExpensesByAppUser();

        //then
        then(expenseRepository).should().deleteAllByAppUser(any());
    }

    @Test
    void updateExpense_successfully() {
        //given
        ExpenseDto expenseDto = new ExpenseDto(1L, BigDecimal.TEN, Instant.now(), ExpensesCategory.OTHER);
        AppUser appUser = new AppUser("username", "password");
        Expense expense = new Expense(BigDecimal.ZERO, Instant.now(), ExpensesCategory.OTHER, appUser);
//        asset.setId(1L);

        given(userLoggedInfo.getLoggedAppUser()).willReturn(appUser);
        given(expenseRepository.findByIdAndAppUser(expenseDto.getId(), appUser)).willReturn(Optional.of(expense));

        //when
        ExpenseDto updateExpense = expenseService.updateExpense(expenseDto);

        //then
        assertThat(updateExpense.getCategory()).isEqualTo(expenseDto.getCategory());
        assertThat(updateExpense.getAmount()).isEqualTo(expenseDto.getAmount());
        then(expenseRepository).should().findByIdAndAppUser(anyLong(), any());
        then(expenseRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void updateAsset_fails_andThrowsAssetNotFoundException() {
        //given
        ExpenseDto expenseDto = new ExpenseDto(-1L, BigDecimal.TEN, Instant.now(), ExpensesCategory.OTHER);
        given(expenseRepository.findByIdAndAppUser(anyLong(), any())).willThrow(AssetNotFoundException.class);

        //when
        //then
        assertThrows(AssetNotFoundException.class, () -> expenseService.updateExpense(expenseDto));
        then(expenseRepository).should().findByIdAndAppUser(anyLong(), any());
    }

    @ParameterizedTest(name = "Filters: {0} and {1}")
    @MethodSource(value = "getFilters")
    void shouldGetFilteredExpenses_byFilters(String firstFilter, String secondFilter, Map<String, String> filters) {
        //given


        AppUser appUser = getAppUser();
        List<Expense> expenseList = List.of(
                new Expense(BigDecimal.ZERO, Instant.now(), ExpensesCategory.OTHER, appUser),
                new Expense(BigDecimal.ONE, Instant.now(), ExpensesCategory.EDUCATION, appUser),
                new Expense(BigDecimal.TEN, Instant.now(), ExpensesCategory.FUN, appUser)
        );
        given(userLoggedInfo.getLoggedAppUser()).willReturn(appUser);
        given(expenseFilterRange.getAllByFilter(appUser, filters)).willReturn(expenseList);

        //when
        List<ExpenseDto> filteredExpenses = expenseService.getFilteredExpenses(filters);


        //then
        assertThat(filteredExpenses).hasSize(3);
    }


    @ParameterizedTest(name = "existing filter: {0}, missing filter: {1}")
    @MethodSource(value = "getFiltersWithOneMissingFilter")
    void shouldNotGetFilteredExpenses_andThrowMissingExpenseFilterSettingException(String existingFilter, String missingFilter, Map<String, String> filters) {
        //given
        AppUser appUser = getAppUser();
        given(userLoggedInfo.getLoggedAppUser()).willReturn(appUser);
        doThrow(new MissingExpenseFilterSettingException("Missing filter setting: " + missingFilter)).when(expenseFilterRange).getAllByFilter(appUser, filters);

        //when
        MissingExpenseFilterSettingException ex = assertThrows(MissingExpenseFilterSettingException.class, () -> expenseService.getFilteredExpenses(filters));

        //then
        assertThat(ex.getMessage()).isEqualTo("Missing filter setting: " + missingFilter);
    }

    private static Stream<Arguments> getFiltersWithOneMissingFilter() {
        return Stream.of(
                Arguments.of("from", "to", Map.of("from", "2021-10-01", "missingTo", "2021-10-30")),
                Arguments.of("to", "from", Map.of("missingFrom", "2021-10-01", "to", "2021-10-30")),
                Arguments.of("month", "year", Map.of("month", "october", "missingYear", "2021")),
                Arguments.of("year", "month", Map.of("missingMonth", "october", "year", "2021")));
    }

    private static Stream<Arguments> getFilters() {
        return Stream.of(
          Arguments.of("from", "to", Map.of("from", "2021-10-01", "to", "2021-10-30")),
          Arguments.of("month", "year", Map.of("month", "october", "year", "2021")));
    }

    private AppUser getAppUser() {
        return new AppUser("user", "password");
    }
}