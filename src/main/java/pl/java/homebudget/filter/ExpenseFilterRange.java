package pl.java.homebudget.filter;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.entity.Expense;
import pl.java.homebudget.repository.ExpenseRepository;

import java.time.Instant;
import java.util.List;

@Component
@AllArgsConstructor
public class ExpenseFilterRange extends FilterRange<Expense> {

    private final ExpenseRepository expenseRepository;

    @Override
    protected List<Expense> getEntitiesWithinDate(AppUser appUser, Instant from, Instant to) {
        return expenseRepository.findAllByPurchaseDateBetweenAndAppUser(from, to, appUser);
    }
}
