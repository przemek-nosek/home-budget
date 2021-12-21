package pl.java.homebudget.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import pl.java.homebudget.enums.FileSeparator;

import java.util.Objects;

@Configuration
@PropertySource(value = "classpath:app.properties")
public class DownloadConfigurer {
    @Value("${file.asset.name}")
    private String assetFileName;
    @Value("${file.expense.name}")
    private String expenseFileName;
    @Value("${file.separator.value}")
    private FileSeparator fileSeparator;


    public String getAssetFileName() {
        if (Objects.isNull(assetFileName)) {
            assetFileName = "assets";
        }
        return assetFileName;
    }

    public String getExpenseFileName() {
        if (Objects.isNull(expenseFileName)) {
            expenseFileName = "expenses";
        }
        return expenseFileName;
    }

    public FileSeparator getFileSeparator() {
        if (Objects.isNull(fileSeparator)) {
            fileSeparator = FileSeparator.COMMA;
        }
        return fileSeparator;
    }
}
