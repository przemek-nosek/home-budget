package pl.java.homebudget.service;

import pl.java.homebudget.dto.ExpenseDto;
import pl.java.homebudget.enums.ExpensesCategory;

import java.util.List;
import java.util.Map;

public interface ExpenseService {
    List<ExpenseDto> getExpenses();

    ExpenseDto addExpense(ExpenseDto expenseDto);

    List<ExpenseDto> saveAllExpenses(List<ExpenseDto> expenseDtos);

    void deleteExpense(ExpenseDto expenseDto);

    List<ExpenseDto> getExpensesByCategory(ExpensesCategory expensesCategory);

    void deleteExpensesByAppUser();

    ExpenseDto updateExpense(ExpenseDto expenseDto);

    List<ExpenseDto> getFilteredExpenses(Map<String, String> filters);
}
