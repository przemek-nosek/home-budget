package pl.java.homebudget.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.java.homebudget.dto.ExpenseDto;
import pl.java.homebudget.dto.UserLoggedInfo;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.entity.Expense;
import pl.java.homebudget.enums.ExpenseFilterSetting;
import pl.java.homebudget.enums.ExpensesCategory;
import pl.java.homebudget.enums.Month;
import pl.java.homebudget.exception.ExpenseNotFoundException;
import pl.java.homebudget.exception.InvalidDateFormatException;
import pl.java.homebudget.exception.MissingExpenseFilterSettingException;
import pl.java.homebudget.mapper.ExpenseMapper;
import pl.java.homebudget.repository.ExpenseRepository;
import pl.java.homebudget.service.ExpenseService;
import pl.java.homebudget.util.DateFormatValidator;

import java.time.Instant;
import java.time.Year;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserLoggedInfo userLoggedInfo;
    private final ExpenseMapper expenseMapper = Mappers.getMapper(ExpenseMapper.class);

    @Override
    public List<ExpenseDto> getExpenses() {
        log.info("getAssets");

        AppUser loggedAppUser = userLoggedInfo.getLoggedAppUser();

        return expenseRepository.findAllByAppUser(loggedAppUser)
                .stream()
                .map(expenseMapper::fromAssetToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ExpenseDto addExpense(ExpenseDto expenseDto) {
        log.info("addAsset");
        log.debug("expenseDto: {}", expenseDto);

        AppUser loggedAppUser = userLoggedInfo.getLoggedAppUser();

        Expense expense = expenseMapper.fromDtoToAsset(expenseDto, loggedAppUser);

        Expense savedExpense = expenseRepository.save(expense);

        log.info("Expense added");

        return expenseMapper.fromAssetToDto(savedExpense);
    }

    @Override
    public List<ExpenseDto> getExpensesByCategory(ExpensesCategory expensesCategory) {
        log.info("getExpensesByCategory");
        log.debug("expensesCategory: {}", expensesCategory);

        AppUser loggedAppUser = userLoggedInfo.getLoggedAppUser();

        return expenseRepository.findAllByCategoryAndAppUser(expensesCategory, loggedAppUser)
                .stream()
                .map(expenseMapper::fromAssetToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteExpense(ExpenseDto expenseDto) {
        log.info("deleteExpense");
        log.debug("expenseDto {}", expenseDto);

        AppUser loggedAppUser = userLoggedInfo.getLoggedAppUser();

        Expense expense = expenseMapper.fromDtoToAsset(expenseDto, loggedAppUser);

        expenseRepository.delete(expense);
        log.info("Expense deleted");
    }

    @Override
    @Transactional
    public void deleteExpenseById(Long id) {
        log.info("deleteExpenseById");

        boolean existsById = expenseRepository.existsById(id);

        if (!existsById) {
            throw new ExpenseNotFoundException(String.format("Expense with given id %d not found", id));
        }

        expenseRepository.deleteById(id);
    }

    @Override
    public void deleteExpensesByAppUser() {
        log.info("deleteExpensesByAppUser");

        AppUser loggedAppUser = userLoggedInfo.getLoggedAppUser();

        expenseRepository.deleteAllByAppUser(loggedAppUser);
    }

    @Override
    @Transactional
    public ExpenseDto updateExpense(ExpenseDto expenseDto) {
        log.info("updateExpense");
        log.debug("expenseDto: {}", expenseDto);

        AppUser loggedAppUser = userLoggedInfo.getLoggedAppUser();

        Long expenseDtoId = expenseDto.getId();
        Expense expense = expenseRepository.findByIdAndAppUser(expenseDtoId, loggedAppUser)
                .orElseThrow(() -> new ExpenseNotFoundException(String.format("Expense with given id %d not found", expenseDtoId)));

        if (Objects.nonNull(expenseDto.getAmount()) && !expenseDto.getAmount().equals(expense.getAmount())) {
            expense.setAmount(expenseDto.getAmount());
        }

        if (Objects.nonNull(expenseDto.getCategory()) && !expenseDto.getCategory().equals(expense.getCategory())) {
            expense.setCategory(expenseDto.getCategory());
        }

        log.info("Expense updated: {}", expense);
        return expenseMapper.fromAssetToDto(expense);
    }

    @Override
    public List<ExpenseDto> getFilteredExpenses(Map<String, String> filters) {
        log.info("getFilteredExpenses");
        log.debug("filters {}", filters);

        if (containsFromAndToFilters(filters)) {
            log.info("Filter parameters: FROM_DATE, TO_DATE");

            String from = filters.get(ExpenseFilterSetting.FROM_DATE.getSetting());
            String to = filters.get(ExpenseFilterSetting.TO_DATE.getSetting());

            return getExpensesWithinDate(from, to);

        } else if (containsMonthAndYearFilters(filters)) {
            log.info("Filter parameters: MONTH, YEAR");

            String year = filters.get(ExpenseFilterSetting.YEAR.getSetting());
            String month = filters.get(ExpenseFilterSetting.MONTH.getSetting()).toUpperCase();

            String from = Month.valueOf(month).getFirstDayForMonthInYear(year);
            String to = Month.valueOf(month).getLastDayForMonthInYear(year, Year.isLeap(Integer.parseInt(year)));

            return getExpensesWithinDate(from, to);
        }

        return Collections.emptyList();
    }

    private boolean containsMonthAndYearFilters(Map<String, String> filters) {
        if (!filters.containsKey(ExpenseFilterSetting.MONTH.getSetting()) &&
                filters.containsKey(ExpenseFilterSetting.YEAR.getSetting())) {
            throw new MissingExpenseFilterSettingException("Missing filter setting: month");

        } else if (filters.containsKey(ExpenseFilterSetting.MONTH.getSetting()) &&
                !filters.containsKey(ExpenseFilterSetting.YEAR.getSetting())) {
            throw new MissingExpenseFilterSettingException("Missing filter setting: year");
        }

        return filters.containsKey(ExpenseFilterSetting.MONTH.getSetting()) &&
                filters.containsKey(ExpenseFilterSetting.YEAR.getSetting());
    }

    private boolean containsFromAndToFilters(Map<String, String> filters) {
        if (!filters.containsKey(ExpenseFilterSetting.FROM_DATE.getSetting()) &&
                filters.containsKey(ExpenseFilterSetting.TO_DATE.getSetting())) {
            throw new MissingExpenseFilterSettingException("Missing filter setting: from");

        } else if (filters.containsKey(ExpenseFilterSetting.FROM_DATE.getSetting()) &&
                !filters.containsKey(ExpenseFilterSetting.TO_DATE.getSetting())) {
            throw new MissingExpenseFilterSettingException("Missing filter setting: to");
        }

        return filters.containsKey(ExpenseFilterSetting.FROM_DATE.getSetting()) &&
                filters.containsKey(ExpenseFilterSetting.TO_DATE.getSetting());
    }

    private List<ExpenseDto> getExpensesWithinDate(String from, String to) {
        log.info("getExpensesWithinDate");
        log.debug("from: {}, to: {}", from, to);

        final String instantFromDateSuffix = "T00:00:00.000Z";
        final String instantToDateSuffix = "T23:59:59.999Z";

        if (!DateFormatValidator.isDate(from) || !DateFormatValidator.isDate(to)) {
            log.info("Invalid date format: {} or {} is invalid", from, to);
            throw new InvalidDateFormatException("Provided date format is not supported! Supported date format: yyyy-MM-dd");
        }

        Instant fromDate = Instant.parse(from + instantFromDateSuffix);
        Instant toDate = Instant.parse(to + instantToDateSuffix);
        // TODO: isBefore throw new exception or?

        AppUser loggedAppUser = userLoggedInfo.getLoggedAppUser();

        return expenseRepository.findAllByPurchaseDateBetweenAndAppUser(fromDate, toDate, loggedAppUser)
                .stream()
                .map(expenseMapper::fromAssetToDto)
                .collect(Collectors.toList());
    }
}
