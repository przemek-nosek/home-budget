package pl.java.homebudget.validator;

import lombok.extern.slf4j.Slf4j;
import pl.java.homebudget.enums.ExpenseFilterSetting;
import pl.java.homebudget.exception.MissingExpenseFilterSettingException;

import java.util.Map;

@Slf4j
public abstract class FilterParameterValidator {

    public void validateFilters(Map<String, String> filters) {
        containsMonthButNotYearFilter(filters);
        containsYearButNotMonthFilter(filters);
        containsFromButNotToFilter(filters);
        containsToButNotFromFilter(filters);
    }

    private void containsMonthButNotYearFilter(Map<String, String> filters) {
        if (filters.containsKey(ExpenseFilterSetting.MONTH.getSetting()) &&
                !filters.containsKey(ExpenseFilterSetting.YEAR.getSetting())) {
            throw new MissingExpenseFilterSettingException("Missing filter setting: year");
        }
    }

    private void containsYearButNotMonthFilter(Map<String, String> filters) {
        if (!filters.containsKey(ExpenseFilterSetting.MONTH.getSetting()) &&
                filters.containsKey(ExpenseFilterSetting.YEAR.getSetting())) {
            throw new MissingExpenseFilterSettingException("Missing filter setting: month");
        }
    }

    private void containsFromButNotToFilter(Map<String, String> filters) {
        if (filters.containsKey(ExpenseFilterSetting.FROM_DATE.getSetting()) &&
                !filters.containsKey(ExpenseFilterSetting.TO_DATE.getSetting())) {
            throw new MissingExpenseFilterSettingException("Missing filter setting: to");
        }
    }

    private void containsToButNotFromFilter(Map<String, String> filters) {
        if (!filters.containsKey(ExpenseFilterSetting.FROM_DATE.getSetting()) &&
                filters.containsKey(ExpenseFilterSetting.TO_DATE.getSetting())) {
            throw new MissingExpenseFilterSettingException("Missing filter setting: from");
        }
    }
}
