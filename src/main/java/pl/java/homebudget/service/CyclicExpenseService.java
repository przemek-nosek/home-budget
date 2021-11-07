package pl.java.homebudget.service;

import pl.java.homebudget.dto.CyclicExpenseDto;

import java.util.List;

public interface CyclicExpenseService {
    List<CyclicExpenseDto> getAllCyclicExpenses();

    List<CyclicExpenseDto> addCyclicExpense(List<CyclicExpenseDto> cyclicExpenseDto);
}
