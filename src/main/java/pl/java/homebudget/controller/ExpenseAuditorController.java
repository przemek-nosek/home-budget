package pl.java.homebudget.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.java.homebudget.service.auditors.ExpenseAuditorService;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/audit")
public class ExpenseAuditorController {

    private final ExpenseAuditorService expenseAuditorService;

    @GetMapping
    public BigDecimal getAudit(@RequestParam Map<String, String> filters) {
        return expenseAuditorService.getAudit(filters);
    }
}
