package pl.java.homebudget.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.java.homebudget.dto.AuditDto;
import pl.java.homebudget.enums.ExpensesCategory;
import pl.java.homebudget.service.auditors.ExpenseAuditorService;
import pl.java.homebudget.service.impl.ExpenseEstimatePercentageService;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/audit")
public class ExpenseAuditorController {

    private final ExpenseAuditorService expenseAuditorService;
    private final ExpenseEstimatePercentageService expenseEstimatePercentageService;

    @GetMapping("/{year}/{month}")
    public BigDecimal getAudit(@PathVariable("year") String year, @PathVariable("month") String month) {

        return expenseAuditorService.getAudit(year, month);
    }

    @GetMapping("estimate/{year}/{month}")
    public Map<ExpensesCategory, AuditDto> getEstimateAudit(@PathVariable("year") String year, @PathVariable("month") String month) {

        return expenseAuditorService.getEstimateAudit(year, month);
    }

    @PostMapping("/estimate")
    public Map<ExpensesCategory, BigDecimal> addEstimate(@RequestBody Map<String, String> estimate) {
        Map<ExpensesCategory, BigDecimal> estimateToSave = estimate.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        e -> ExpensesCategory.valueOf(e.getKey().toUpperCase()),
                        e -> new BigDecimal(e.getValue())
                ));

        return expenseEstimatePercentageService.addEstimation(estimateToSave);
    }
}
