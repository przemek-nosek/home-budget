package pl.java.homebudget.validator;

import org.springframework.stereotype.Component;
import pl.java.homebudget.enums.DateFilterSetting;
import pl.java.homebudget.exception.MissingExpenseFilterSettingException;

import java.util.Map;

@Component
public class FilterParameterValidator {

    public void validateFilters(Map<String, String> filters) {
        containsMonthButNotYearFilter(filters);
        containsYearButNotMonthFilter(filters);
        containsFromButNotToFilter(filters);
        containsToButNotFromFilter(filters);
    }

    private void containsMonthButNotYearFilter(Map<String, String> filters) {
        if (filters.containsKey(DateFilterSetting.MONTH.getSetting()) &&
                !filters.containsKey(DateFilterSetting.YEAR.getSetting())) {
            throw new MissingExpenseFilterSettingException("Missing filter setting: year");
        }
    }

    private void containsYearButNotMonthFilter(Map<String, String> filters) {
        if (!filters.containsKey(DateFilterSetting.MONTH.getSetting()) &&
                filters.containsKey(DateFilterSetting.YEAR.getSetting())) {
            throw new MissingExpenseFilterSettingException("Missing filter setting: month");
        }
    }

    private void containsFromButNotToFilter(Map<String, String> filters) {
        if (filters.containsKey(DateFilterSetting.FROM_DATE.getSetting()) &&
                !filters.containsKey(DateFilterSetting.TO_DATE.getSetting())) {
            throw new MissingExpenseFilterSettingException("Missing filter setting: to");
        }
    }

    private void containsToButNotFromFilter(Map<String, String> filters) {
        if (!filters.containsKey(DateFilterSetting.FROM_DATE.getSetting()) &&
                filters.containsKey(DateFilterSetting.TO_DATE.getSetting())) {
            throw new MissingExpenseFilterSettingException("Missing filter setting: from");
        }
    }
}
