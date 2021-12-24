package pl.java.homebudget.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.mapping.Collection;
import org.springframework.stereotype.Service;
import pl.java.homebudget.dto.UserLoggedInfo;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.entity.ExpenseEstimatePercentage;
import pl.java.homebudget.enums.ExpensesCategory;
import pl.java.homebudget.mapper.ExpenseEstimatePercentageMapper;
import pl.java.homebudget.repository.ExpenseEstimatePercentageRepository;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ExpenseEstimatePercentageService {

    private final ExpenseEstimatePercentageRepository expenseEstimatePercentageRepository;
    private final UserLoggedInfo userLoggedInfo;
    private final ExpenseEstimatePercentageMapper expenseEstimatePercentageMapper;


    public Map<ExpensesCategory, BigDecimal> addEstimation(Map<ExpensesCategory, BigDecimal> categoryBigDecimalMap) {
        log.info("addEstimation");
        log.debug("categoryBigDecimalMap: {}", categoryBigDecimalMap);

        AppUser loggedAppUser = userLoggedInfo.getLoggedAppUser();
        List<ExpenseEstimatePercentage> expenseEstimatePercentages = expenseEstimatePercentageMapper.fromMapToEntity(categoryBigDecimalMap, loggedAppUser);

        List<ExpenseEstimatePercentage> savedEstimates = expenseEstimatePercentageRepository.saveAll(expenseEstimatePercentages);

        return savedEstimates.stream()
                .collect(Collectors.toMap(ExpenseEstimatePercentage::getExpensesCategory, ExpenseEstimatePercentage::getPercentage));
    }

    public Map<ExpensesCategory, BigDecimal> getEstimations() {
        log.info("getEstimations");

        AppUser loggedAppUser = userLoggedInfo.getLoggedAppUser();
        Optional<List<ExpenseEstimatePercentage>> estimations = expenseEstimatePercentageRepository.findAllByAppUser(loggedAppUser);

        if (estimations.isPresent()) {
            List<ExpenseEstimatePercentage> expenseEstimatePercentages = estimations.get();
            return expenseEstimatePercentages.stream()
                    .collect(Collectors.toMap(ExpenseEstimatePercentage::getExpensesCategory, ExpenseEstimatePercentage::getPercentage));
        }

        return Collections.emptyMap();
    }
}
