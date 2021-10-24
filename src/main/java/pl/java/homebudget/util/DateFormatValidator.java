package pl.java.homebudget.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.GenericValidator;

@Slf4j
public class DateFormatValidator {

    private final static String DATEFORMAT = "yyyy-MM-dd";

    public static boolean isDate(String date) {
        log.info("validate date format");
        return GenericValidator.isDate(date, DATEFORMAT, true);
    }
}
