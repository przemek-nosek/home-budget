package pl.java.homebudget.enums;

public enum Month {
    JANUARY("01"),
    FEBRUARY("02"),
    MARCH("03"),
    APRIL("04"),
    MAY("05"),
    JUNE("06"),
    JULY("07"),
    AUGUST("08"),
    SEPTEMBER("09"),
    OCTOBER("10"),
    NOVEMBER("11"),
    DECEMBER("12");

    private final String monthNumber;

    Month(String monthNumber) {
        this.monthNumber = monthNumber;
    }

    public int daysInMonth(boolean leapYear) {
        return switch (this) {
            case FEBRUARY -> (leapYear ? 29 : 28);
            case APRIL, JUNE, SEPTEMBER, NOVEMBER -> 30;
            default -> 31;
        };
    }

    public String getFirstDayForMonthInYear(String year) {
        return year + "-" + monthNumber + "-01";
    }

    public String getLastDayForMonthInYear(String year, boolean isLeapYear) {
        return year + "-" + monthNumber + "-" + daysInMonth(isLeapYear);
    }
}
