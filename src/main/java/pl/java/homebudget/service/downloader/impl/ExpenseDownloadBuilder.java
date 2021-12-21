package pl.java.homebudget.service.downloader.impl;

import org.springframework.stereotype.Service;
import pl.java.homebudget.dto.ExpenseDto;

import java.util.List;

@Service
class ExpenseDownloadBuilder {

    public StringBuilder prepareExpense(List<ExpenseDto> dtos, String separator) {
        StringBuilder builder = new StringBuilder("Amount" + separator + "Category" + separator + "Purchase Date" + separator + "Description");

        dtos.forEach(expense -> {
            builder.append("\n");
            builder.append(expense.getAmount());
            builder.append(separator);
            builder.append(expense.getCategory());
            builder.append(separator);
            builder.append(expense.getPurchaseDate());
            builder.append(separator);
            builder.append(expense.getDescription());
        });

        return builder;
    }
}
