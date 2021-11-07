package pl.java.homebudget.dto;

import lombok.*;
import pl.java.homebudget.enums.ExpensesCategory;
import pl.java.homebudget.enums.Month;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CyclicExpenseDto {
    private Long id;
    private BigDecimal amount;
    private ExpensesCategory expensesCategory;
    private Month month;
    private Integer day;
}
