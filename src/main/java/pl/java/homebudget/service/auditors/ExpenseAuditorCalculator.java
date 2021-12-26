package pl.java.homebudget.service.auditors;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.java.homebudget.dto.AssetDto;
import pl.java.homebudget.dto.ExpenseDto;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.enums.ExpensesCategory;
import pl.java.homebudget.enums.FilterParameterSetting;
import pl.java.homebudget.service.AssetService;
import pl.java.homebudget.service.ExpenseService;
import pl.java.homebudget.service.impl.ExpenseEstimatePercentageService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
class ExpenseAuditorCalculator {

    private final ExpenseService expenseService;
    private final AssetService assetService;
    private final ExpenseEstimatePercentageService expenseEstimatePercentageService;

    Map<String, String> getFilters(String year, String month) {
        return new HashMap<>() {{
            put(FilterParameterSetting.MONTH.getSetting(), month);
            put(FilterParameterSetting.YEAR.getSetting(), year);
        }};
    }

    List<ExpenseDto> getExpensesInFilterRange(Map<String, String> filters) {
        return expenseService.getFilteredExpenses(filters);
    }

    List<ExpenseDto> getExpensesInFilterRange(AppUser appUser, Map<String, String> filters) {
        return expenseService.getFilteredExpenses(appUser, filters);
    }

    List<AssetDto> getAssetsInFilterRange(Map<String, String> filters) {
        return assetService.getFilteredAssets(filters);
    }

    List<AssetDto> getAssetsInFilterRange(AppUser appUser, Map<String, String> filters) {
        return assetService.getFilteredAssets(appUser, filters);
    }

    BigDecimal getRealPercentageAudit(ExpensesCategory expensesCategory, Map<String, String> filters) {
        return getExpensesInFilterRange(filters).stream()
                .filter(expenseDto -> expenseDto.getCategory().equals(expensesCategory))
                .map(ExpenseDto::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    BigDecimal getRealPercentageAudit(AppUser appUser, ExpensesCategory expensesCategory, Map<String, String> filters) {
        return getExpensesInFilterRange(appUser, filters).stream()
                .filter(expenseDto -> expenseDto.getCategory().equals(expensesCategory))
                .map(ExpenseDto::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    BigDecimal getPlannedPercentageAudit(ExpensesCategory expensesCategory, BigDecimal amount) {
        BigDecimal percentage = expenseEstimatePercentageService.getEstimations().get(expensesCategory)
                .divide(new BigDecimal("100"));

        return amount.multiply(percentage);
    }

    BigDecimal getPlannedPercentageAudit(AppUser appUser, ExpensesCategory expensesCategory, BigDecimal amount) {
        BigDecimal percentage = expenseEstimatePercentageService.getEstimations(appUser).get(expensesCategory)
                .divide(new BigDecimal("100"));

        return amount.multiply(percentage);
    }
}
