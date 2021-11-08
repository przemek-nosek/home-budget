package pl.java.homebudget.service.downloader.impl;

import org.springframework.stereotype.Service;
import pl.java.homebudget.dto.ExpenseDto;

import java.util.List;

@Service
class ExpenseDownloadBuilder {

    public StringBuilder prepareExpense(List<ExpenseDto> dtos, String separator) {
        StringBuilder builder = new StringBuilder();
        builder
                .append("Amount")
                .append(separator)
                .append("Category")
                .append(separator)
                .append("Purchase Date")
                .append(separator)
                .append("Description")
                .append("\n");


        dtos.forEach(expense -> {
            builder.append(expense.getAmount());
            builder.append(separator);
            builder.append(expense.getCategory());
            builder.append(separator);
            builder.append(expense.getPurchaseDate());
            builder.append(separator);
            builder.append(expense.getDescription());
            builder.append("\n");
        });

        return builder;
    }
}
