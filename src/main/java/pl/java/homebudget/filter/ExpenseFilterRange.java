package pl.java.homebudget.filter;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.entity.Expense;
import pl.java.homebudget.exception.InvalidDateFormatException;
import pl.java.homebudget.repository.ExpenseRepository;
import pl.java.homebudget.validator.DateFormatValidator;

import java.time.Instant;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class ExpenseFilterRange extends FilterRange {

    private final ExpenseRepository expenseRepository;

    @Override
    protected List<Expense> getEntitiesWithinDate(AppUser appUser, String from, String to) {
        log.info("getExpensesWithinDate");
        log.debug("from: {}, to: {}", from, to);

        final String instantFromDateSuffix = "T00:00:00.000Z";
        final String instantToDateSuffix = "T23:59:59.999Z";

        if (!DateFormatValidator.isDate(from) || !DateFormatValidator.isDate(to)) {
            log.info("Invalid date format: {} or {} is invalid", from, to);
            throw new InvalidDateFormatException("Provided date format is not supported! Supported date format: yyyy-MM-dd");
        }

        Instant fromDate = Instant.parse(from + instantFromDateSuffix);
        Instant toDate = Instant.parse(to + instantToDateSuffix);

        return expenseRepository.findAllByPurchaseDateBetweenAndAppUser(fromDate, toDate, appUser);
    }
}
