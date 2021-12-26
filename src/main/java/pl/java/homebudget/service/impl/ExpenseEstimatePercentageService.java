package pl.java.homebudget.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.entity.ExpenseEstimatePercentage;
import pl.java.homebudget.enums.ExpensesCategory;
import pl.java.homebudget.mapper.ExpenseEstimatePercentageMapper;
import pl.java.homebudget.repository.ExpenseEstimatePercentageRepository;
import pl.java.homebudget.service.impl.user.UserLoggedInfoService;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ExpenseEstimatePercentageService {

    private final ExpenseEstimatePercentageRepository expenseEstimatePercentageRepository;
    private final UserLoggedInfoService userLoggedInfoService;
    private final ExpenseEstimatePercentageMapper expenseEstimatePercentageMapper;

    @Transactional
    public Map<ExpensesCategory, BigDecimal> addEstimation(Map<ExpensesCategory, BigDecimal> categoryBigDecimalMap) {
        log.info("addEstimation");
        log.debug("categoryBigDecimalMap: {}", categoryBigDecimalMap);

        AppUser loggedAppUser = userLoggedInfoService.getLoggedAppUser();
        List<ExpenseEstimatePercentage> expenseEstimatePercentages = expenseEstimatePercentageMapper.fromMapToEntity(categoryBigDecimalMap, loggedAppUser);

        List<ExpenseEstimatePercentage> savedEstimates = expenseEstimatePercentageRepository.saveAll(expenseEstimatePercentages);

        return savedEstimates.stream()
                .collect(Collectors.toMap(ExpenseEstimatePercentage::getExpensesCategory, ExpenseEstimatePercentage::getPercentage));
    }

    public Map<ExpensesCategory, BigDecimal> getEstimations() {
        log.info("getEstimations");

        AppUser loggedAppUser = userLoggedInfoService.getLoggedAppUser();

        return getEstimations(loggedAppUser);
    }

    public Map<ExpensesCategory, BigDecimal> getEstimations(AppUser appUser) {
        log.info("getEstimations");

        List<ExpenseEstimatePercentage> estimations = expenseEstimatePercentageRepository.findAllByAppUser(appUser);

        if (estimations.size() != 0) {
            return estimations.stream()
                    .collect(Collectors.toMap(ExpenseEstimatePercentage::getExpensesCategory, ExpenseEstimatePercentage::getPercentage));
        }

        return mapWithZeroValues();
    }

    private Map<ExpensesCategory, BigDecimal> mapWithZeroValues() {
        return Arrays.stream(ExpensesCategory.values())
                .collect(Collectors.toMap(
                        expensesCategory -> expensesCategory,
                        expensesCategory -> BigDecimal.ZERO
                ));
    }
}
