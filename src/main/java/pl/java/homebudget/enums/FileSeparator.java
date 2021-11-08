package pl.java.homebudget.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FileSeparator {
    COMMA(","),
    TAB("\t"),
    SEMICOLON(";");

    private final String separator;
}
