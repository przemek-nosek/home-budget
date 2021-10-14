package pl.java.homebudget.service;

import pl.java.homebudget.dto.ExpenseDto;
import pl.java.homebudget.enums.ExpensesCategory;

import java.util.List;

public interface ExpenseService {
    List<ExpenseDto> getExpenses();

    ExpenseDto addExpense(ExpenseDto expenseDto);

    void deleteExpense(ExpenseDto expenseDto);

    List<ExpenseDto> getExpensesByCategory(ExpensesCategory expensesCategory);

    void deleteExpenseById(Long id);

    void deleteExpensesByAppUser();

    ExpenseDto updateExpense(ExpenseDto expenseDto);
}
