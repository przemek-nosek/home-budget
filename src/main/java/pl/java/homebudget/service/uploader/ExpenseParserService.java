package pl.java.homebudget.service.uploader;

import org.springframework.stereotype.Component;
import pl.java.homebudget.dto.ExpenseDto;
import pl.java.homebudget.enums.ExpensesCategory;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
class ExpenseParserService {

    public List<ExpenseDto> parseToExpense(String content) {
        return Arrays.stream(content.split("\n"))
                .skip(1)
                .map(s -> s.split(";"))
                .map(array -> ExpenseDto.builder()
                        .amount(new BigDecimal(array[0]))
                        .category(ExpensesCategory.valueOf(array[1]))
                        .purchaseDate(Instant.parse(array[2] + "T00:00:00.000Z"))
                        .description(array[3])
                        .build())
                .collect(Collectors.toList());
    }
}
