package pl.java.homebudget.entity;

import lombok.*;
import pl.java.homebudget.enums.ExpensesCategory;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;

@Entity
@Builder
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ExpenseEstimatePercentage extends BaseEntity{

    private BigDecimal percentage;

    @Enumerated(value = EnumType.STRING)
    private ExpensesCategory expensesCategory;

    public ExpenseEstimatePercentage(AppUser appUser, BigDecimal percentage, ExpensesCategory expensesCategory) {
        super(appUser);
        this.percentage = percentage;
        this.expensesCategory = expensesCategory;
    }

    public ExpenseEstimatePercentage(BigDecimal percentage, ExpensesCategory expensesCategory) {
        this.percentage = percentage;
        this.expensesCategory = expensesCategory;
    }
}
