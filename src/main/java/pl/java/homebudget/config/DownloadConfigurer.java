package pl.java.homebudget.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import pl.java.homebudget.enums.FileSeparator;

@Configuration
@PropertySource(value = "classpath:app.properties")
@Getter
public class DownloadConfigurer {
    @Value("${file.asset.name}")
    private String assetFileName;
    @Value("${file.expense.name}")
    private String expenseFileName;
    @Value("${file.separator.value}")
    private FileSeparator fileSeparator;
}
