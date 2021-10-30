package pl.java.homebudget.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.enums.DateFilterSetting;
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
            String from = filters.get(DateFilterSetting.FROM_DATE.getSetting());
            String to = filters.get(DateFilterSetting.TO_DATE.getSetting());

            return getEntitiesWithinDate(appUser, parseFromDateToInstant(from), parseToDateToInstant(to));

        } else if (containsMonthAndYearFilters(filters)) {
            log.info("Filter parameters: MONTH, YEAR");

            String year = filters.get(DateFilterSetting.YEAR.getSetting());
            String month = filters.get(DateFilterSetting.MONTH.getSetting()).toUpperCase();

            String from = Month.valueOf(month).getFirstDayForMonthInYear(year);
            String to = Month.valueOf(month).getLastDayForMonthInYear(year, Year.isLeap(Integer.parseInt(year)));

            return getEntitiesWithinDate(appUser, parseFromDateToInstant(from), parseToDateToInstant(to));
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
        return filters.containsKey(DateFilterSetting.MONTH.getSetting()) &&
                filters.containsKey(DateFilterSetting.YEAR.getSetting());
    }

    private boolean containsFromAndToFilters(Map<String, String> filters) {
        log.info("containsFromAndToFilters, {}", filters);
        return filters.containsKey(DateFilterSetting.FROM_DATE.getSetting()) &&
                filters.containsKey(DateFilterSetting.TO_DATE.getSetting());
    }

    protected abstract List<T> getEntitiesWithinDate(AppUser appUser, Instant from, Instant to);
}
