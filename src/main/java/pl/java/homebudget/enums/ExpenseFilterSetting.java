package pl.java.homebudget.enums;

public enum ExpenseFilterSetting {
    FROM_DATE("from"),
    TO_DATE("to"),
    MONTH("month"),
    YEAR("year");

    private final String setting;

    ExpenseFilterSetting(String setting) {
        this.setting = setting;
    }

    public String getSetting() {
        return setting;
    }
}
