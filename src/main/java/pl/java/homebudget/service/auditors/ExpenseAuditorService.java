package pl.java.homebudget.service.auditors;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.java.homebudget.dto.AssetDto;
import pl.java.homebudget.dto.AuditDto;
import pl.java.homebudget.dto.ExpenseDto;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.enums.ExpensesCategory;
import pl.java.homebudget.service.impl.ExpenseEstimatePercentageService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ExpenseAuditorService {

    private final ExpenseAuditorCalculator expenseAuditorCalculator;
    private final ExpenseEstimatePercentageService expenseEstimatePercentageService;

    public BigDecimal getAudit(String year, String month) {
        Map<String, String> filters = expenseAuditorCalculator.getFilters(year, month);
        BigDecimal assetAmount = expenseAuditorCalculator.getAssetsInFilterRange(filters).stream()
                .map(AssetDto::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal expenseAmount = expenseAuditorCalculator.getExpensesInFilterRange(filters)
                .stream()
                .map(ExpenseDto::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return assetAmount.subtract(expenseAmount);
    }

    public Map<ExpensesCategory, AuditDto> getEstimateAudit(String year, String month) {

        Map<String, String> filters = expenseAuditorCalculator.getFilters(year, month);

        List<AssetDto> assetsInFilterRange = expenseAuditorCalculator.getAssetsInFilterRange(filters);
        BigDecimal assetSum = assetsInFilterRange.stream()
                .map(AssetDto::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return Arrays.stream(ExpensesCategory.values())
                .collect(Collectors.toMap(
                        expensesCategory -> expensesCategory,
                        expensesCategory -> {
                            BigDecimal realPercentageAudit = expenseAuditorCalculator.getRealPercentageAudit(expensesCategory, filters);
                            BigDecimal plannedPercentageAudit = expenseAuditorCalculator.getPlannedPercentageAudit(expensesCategory, assetSum);

                            return AuditDto.builder()
                                    .percentage(expenseEstimatePercentageService.getEstimations().get(expensesCategory))
                                    .plannedAmount(plannedPercentageAudit)
                                    .realAmount(realPercentageAudit)
                                    .build();
                        }
                ));

    }

    public Map<ExpensesCategory, AuditDto> getEstimateAudit(AppUser appUser, String year, String month) {
        Map<String, String> filters = expenseAuditorCalculator.getFilters(year, month);

        List<AssetDto> assetsInFilterRange = expenseAuditorCalculator.getAssetsInFilterRange(appUser, filters);
        BigDecimal assetSum = assetsInFilterRange.stream()
                .map(AssetDto::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return Arrays.stream(ExpensesCategory.values())
                .collect(Collectors.toMap(
                        expensesCategory -> expensesCategory,
                        expensesCategory -> {
                            BigDecimal realPercentageAudit = expenseAuditorCalculator.getRealPercentageAudit(appUser, expensesCategory, filters);
                            BigDecimal plannedPercentageAudit = expenseAuditorCalculator.getPlannedPercentageAudit(appUser, expensesCategory, assetSum);

                            return AuditDto.builder()
                                    .percentage(expenseEstimatePercentageService.getEstimations(appUser).get(expensesCategory))
                                    .plannedAmount(plannedPercentageAudit)
                                    .realAmount(realPercentageAudit)
                                    .build();
                        }
                ));

    }
}
