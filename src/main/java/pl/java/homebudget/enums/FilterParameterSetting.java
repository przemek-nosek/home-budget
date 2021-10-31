package pl.java.homebudget.enums;

public enum FilterParameterSetting {
    FROM_DATE("from"),
    TO_DATE("to"),
    MONTH("month"),
    YEAR("year"),
    CATEGORY("category");

    private final String setting;

    FilterParameterSetting(String setting) {
        this.setting = setting;
    }

    public String getSetting() {
        return setting;
    }
}
