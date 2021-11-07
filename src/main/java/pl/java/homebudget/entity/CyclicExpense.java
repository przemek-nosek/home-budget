package pl.java.homebudget.entity;

import lombok.*;
import pl.java.homebudget.enums.ExpensesCategory;
import pl.java.homebudget.enums.Month;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "cyclic_expenses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CyclicExpense extends BaseEntity {

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private ExpensesCategory expensesCategory;

    @Enumerated(EnumType.STRING)
    private Month month;

    private Integer day;
}
