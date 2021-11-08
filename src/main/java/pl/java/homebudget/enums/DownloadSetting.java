package pl.java.homebudget.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum DownloadSetting {
    ASSET("asset"),
    EXPENSE("expense");

    private String option;
}
