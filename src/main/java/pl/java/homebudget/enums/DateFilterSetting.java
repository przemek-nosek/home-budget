package pl.java.homebudget.enums;

public enum DateFilterSetting {
    FROM_DATE("from"),
    TO_DATE("to"),
    MONTH("month"),
    YEAR("year");

    private final String setting;

    DateFilterSetting(String setting) {
        this.setting = setting;
    }

    public String getSetting() {
        return setting;
    }
}
