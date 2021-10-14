package pl.java.homebudget.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.java.homebudget.dto.ExpenseDto;
import pl.java.homebudget.enums.ExpensesCategory;
import pl.java.homebudget.service.ExpenseService;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/api/v1/expenses")
@AllArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @GetMapping
    public ResponseEntity<List<ExpenseDto>> getExpenses() {
        List<ExpenseDto> expenseDtoList = expenseService.getExpenses();

        return new ResponseEntity<>(expenseDtoList, HttpStatus.OK);
    }

    @GetMapping("/find")
    public ResponseEntity<List<ExpenseDto>> getExpensesByCategory(@PathParam("category") String category) {
        ExpensesCategory expensesCategory = ExpensesCategory.valueOf(category.toUpperCase());

        List<ExpenseDto> expenseDtoList = expenseService.getExpensesByCategory(expensesCategory);

        return new ResponseEntity<>(expenseDtoList, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<ExpenseDto> addExpense(@Valid @RequestBody ExpenseDto expenseDto) {
        ExpenseDto createdExpense = expenseService.addExpense(expenseDto);

        return new ResponseEntity<>(createdExpense, HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteExpense(@RequestBody ExpenseDto expenseDto) {
        expenseService.deleteExpense(expenseDto);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExpenseById(@PathVariable Long id) {
        return null;
    }
}
