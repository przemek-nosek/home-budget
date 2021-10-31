package pl.java.homebudget.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.enums.FilterParameterSetting;
import pl.java.homebudget.enums.Month;
import pl.java.homebudget.exception.InvalidDateFormatException;
import pl.java.homebudget.validator.DateFormatValidator;
import pl.java.homebudget.validator.FilterParameterValidator;

import java.time.Instant;
import java.time.Year;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
public abstract class FilterRange<T> {

    @Autowired
    private FilterParameterValidator filterParameterValidator;

    private final static String INSTANT_FROM_DATE_SUFFIX = "T00:00:00.000Z";
    private final static String INSTANT_TO_DATE_SUFFIX = "T23:59:59.999Z";


    public List<T> getAllByFilter(AppUser appUser, Map<String, String> filters) {// TODO: isBefore throw new exception or?
        filterParameterValidator.validateFilters(filters);

        if (containsFromAndToFilters(filters)) {
            log.info("Filter parameters: FROM_DATE, TO_DATE");
            String from = filters.get(FilterParameterSetting.FROM_DATE.getSetting());
            String to = filters.get(FilterParameterSetting.TO_DATE.getSetting());

            return getEntitiesWithinDate(appUser, parseFromDateToInstant(from), parseToDateToInstant(to), filters.get(FilterParameterSetting.CATEGORY.getSetting()));

        } else if (containsMonthAndYearFilters(filters)) {
            log.info("Filter parameters: MONTH, YEAR");

            String year = filters.get(FilterParameterSetting.YEAR.getSetting());
            String month = filters.get(FilterParameterSetting.MONTH.getSetting()).toUpperCase();

            String from = Month.valueOf(month).getFirstDayForMonthInYear(year);
            String to = Month.valueOf(month).getLastDayForMonthInYear(year, Year.isLeap(Integer.parseInt(year)));

            return getEntitiesWithinDate(appUser, parseFromDateToInstant(from), parseToDateToInstant(to), filters.get(FilterParameterSetting.CATEGORY.getSetting()));
        }

        return Collections.emptyList();
    }

    private Instant parseFromDateToInstant(String date) {
        validateDateFormat(date);

        return Instant.parse(date + INSTANT_FROM_DATE_SUFFIX);
    }

    private Instant parseToDateToInstant(String date) {
        validateDateFormat(date);

        return Instant.parse(date + INSTANT_TO_DATE_SUFFIX);
    }

    private void validateDateFormat(String date) {
        if (!DateFormatValidator.isDate(date)) {
            log.info("Invalid date format: {}", date);
            throw new InvalidDateFormatException("Provided date format is not supported! Supported date format: yyyy-MM-dd");
        }
    }


    private boolean containsMonthAndYearFilters(Map<String, String> filters) {
        log.info("containsMonthAndYearFilters, {}", filters);
        return filters.containsKey(FilterParameterSetting.MONTH.getSetting()) &&
                filters.containsKey(FilterParameterSetting.YEAR.getSetting());
    }

    private boolean containsFromAndToFilters(Map<String, String> filters) {
        log.info("containsFromAndToFilters, {}", filters);
        return filters.containsKey(FilterParameterSetting.FROM_DATE.getSetting()) &&
                filters.containsKey(FilterParameterSetting.TO_DATE.getSetting());
    }

    protected abstract List<T> getEntitiesWithinDate(AppUser appUser, Instant from, Instant to, String category);
}
