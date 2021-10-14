package pl.java.homebudget.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.java.homebudget.enums.ExpensesCategory;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "expenses")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Expense {
    @Id
    @GeneratedValue
    private Long id;

    private BigDecimal amount;

    private Instant purchaseDate;

    @Enumerated
    private ExpensesCategory category;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @ToString.Exclude
    private AppUser appUser;
}
