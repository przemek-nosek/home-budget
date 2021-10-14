package pl.java.homebudget.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import pl.java.homebudget.dto.ExpenseDto;
import pl.java.homebudget.dto.UserLoggedInfo;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.entity.Expense;
import pl.java.homebudget.enums.ExpensesCategory;
import pl.java.homebudget.mapper.ExpenseMapper;
import pl.java.homebudget.repository.ExpenseRepository;
import pl.java.homebudget.service.ExpenseService;

import java.util.List;
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

        expenseRepository.save(expense);

        log.info("Expense added");

        return expenseMapper.fromAssetToDto(expense);
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
    public List<ExpenseDto> getExpensesByCategory(ExpensesCategory expensesCategory) {
        log.info("getExpensesByCategory");
        log.debug("expensesCategory: {}", expensesCategory);

        AppUser loggedAppUser = userLoggedInfo.getLoggedAppUser();

        return expenseRepository.findAllByCategoryAndAppUser(expensesCategory, loggedAppUser)
                .stream()
                .map(expenseMapper::fromAssetToDto)
                .collect(Collectors.toList());
    }
}
