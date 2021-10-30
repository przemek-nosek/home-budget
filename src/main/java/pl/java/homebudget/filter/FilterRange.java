package pl.java.homebudget.filter;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.entity.Expense;
import pl.java.homebudget.enums.ExpenseFilterSetting;
import pl.java.homebudget.enums.Month;
import pl.java.homebudget.validator.FilterParameterValidator;

import java.time.Year;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public abstract class FilterRange {

    @Autowired
    private FilterParameterValidator filterParameterValidator;


    public List<Expense> getAllByFilter(AppUser appUser, Map<String, String> filters) {// TODO: isBefore throw new exception or?
        filterParameterValidator.validateFilters(filters);

        if (containsFromAndToFilters(filters)) {
            log.info("Filter parameters: FROM_DATE, TO_DATE");

            String from = filters.get(ExpenseFilterSetting.FROM_DATE.getSetting());
            String to = filters.get(ExpenseFilterSetting.TO_DATE.getSetting());

            return getEntitiesWithinDate(appUser, from, to);

        } else if (containsMonthAndYearFilters(filters)) {
            log.info("Filter parameters: MONTH, YEAR");

            String year = filters.get(ExpenseFilterSetting.YEAR.getSetting());
            String month = filters.get(ExpenseFilterSetting.MONTH.getSetting()).toUpperCase();

            String from = Month.valueOf(month).getFirstDayForMonthInYear(year);
            String to = Month.valueOf(month).getLastDayForMonthInYear(year, Year.isLeap(Integer.parseInt(year)));

            return getEntitiesWithinDate(appUser, from, to);
        }

        return Collections.emptyList();
    }

    protected abstract List<Expense> getEntitiesWithinDate(AppUser appUser, String from, String to);

    private boolean containsMonthAndYearFilters(Map<String, String> filters) {
        log.info("containsMonthAndYearFilters, {}", filters);
        return filters.containsKey(ExpenseFilterSetting.MONTH.getSetting()) &&
                filters.containsKey(ExpenseFilterSetting.YEAR.getSetting());
    }

    private boolean containsFromAndToFilters(Map<String, String> filters) {
        log.info("containsFromAndToFilters, {}", filters);
        return filters.containsKey(ExpenseFilterSetting.FROM_DATE.getSetting()) &&
                filters.containsKey(ExpenseFilterSetting.TO_DATE.getSetting());
    }
}
