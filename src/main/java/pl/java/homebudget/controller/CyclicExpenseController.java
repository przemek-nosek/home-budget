package pl.java.homebudget.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.java.homebudget.dto.CyclicExpenseDto;
import pl.java.homebudget.service.CyclicExpenseService;

import java.util.List;

@RestController
@RequestMapping("api/v1/cyclic")
@RequiredArgsConstructor
public class CyclicExpenseController {

    private final CyclicExpenseService cyclicExpenseService;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<CyclicExpenseDto> getAllCyclicExpenses() {
        return cyclicExpenseService.getAllCyclicExpenses();
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public List<CyclicExpenseDto> addCyclicExpense(@RequestBody List<CyclicExpenseDto> cyclicExpenseDtoList) {
        return cyclicExpenseService.addCyclicExpense(cyclicExpenseDtoList);
    }
}
