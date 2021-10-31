package pl.java.homebudget.filter;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.entity.Expense;
import pl.java.homebudget.enums.ExpensesCategory;
import pl.java.homebudget.repository.ExpenseRepository;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
@AllArgsConstructor
public class ExpenseFilterRange extends FilterRange<Expense> {

    private final ExpenseRepository expenseRepository;


    @Override
    protected List<Expense> getEntitiesWithinDate(AppUser appUser, Instant from, Instant to, String category) {
        List<ExpensesCategory> expensesCategories = mapStringToList(category);

        return expenseRepository.findAllByPurchaseDateBetweenAndAppUserAndCategoryIn(from, to, appUser, expensesCategories);
    }

    private List<ExpensesCategory> mapStringToList(String category) {
        if (Objects.isNull(category)) {
            return Arrays.stream(ExpensesCategory.values()).toList();
        }
        return List.of(ExpensesCategory.valueOf(category.toUpperCase()));
    }
}
