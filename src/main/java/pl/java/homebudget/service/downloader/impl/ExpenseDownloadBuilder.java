package pl.java.homebudget.service.downloader.impl;

import org.springframework.stereotype.Service;
import pl.java.homebudget.dto.ExpenseDto;

import java.util.List;

@Service
class ExpenseDownloadBuilder {

    private final static String SEPARATOR = ";";

    public StringBuilder prepareExpense(List<ExpenseDto> dtos) {
        StringBuilder builder = new StringBuilder();

        dtos.forEach(expense -> {
            builder.append(expense.getAmount());
            builder.append(SEPARATOR);
            builder.append(expense.getCategory());
            builder.append(SEPARATOR);
            builder.append(expense.getPurchaseDate());
            builder.append(SEPARATOR);
            builder.append(expense.getDescription());
            builder.append("\n");
        });

        return builder;
    }
}
