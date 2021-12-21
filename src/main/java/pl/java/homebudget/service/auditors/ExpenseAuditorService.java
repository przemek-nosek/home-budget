package pl.java.homebudget.service.auditors;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.java.homebudget.dto.AssetDto;
import pl.java.homebudget.dto.ExpenseDto;
import pl.java.homebudget.service.AssetService;
import pl.java.homebudget.service.ExpenseService;

import java.math.BigDecimal;
import java.util.Map;

@Service
@AllArgsConstructor
public class ExpenseAuditorService {

    private final ExpenseService expenseService;
    private final AssetService assetService;

    public BigDecimal getAudit(Map<String, String > filters) {
        BigDecimal assetAmount = assetService.getFilteredAssets(filters)
                .stream()
                .map(AssetDto::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal expenseAmount = expenseService.getFilteredExpenses(filters)
                .stream()
                .map(ExpenseDto::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return assetAmount.subtract(expenseAmount);
    }
}
