package pl.java.homebudget.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.java.homebudget.enums.ExpensesCategory;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "expenses")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Expense {
    @Id
    @GeneratedValue
    @Column(updatable = false, nullable = false)
    private Long id;

    private BigDecimal amount;

    private Instant purchaseDate;

    @Enumerated(value = EnumType.STRING)
    private ExpensesCategory category;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @ToString.Exclude
    private AppUser appUser;

    public Expense(BigDecimal amount, Instant purchaseDate, ExpensesCategory category, AppUser appUser) {
        this.amount = amount;
        this.purchaseDate = purchaseDate;
        this.category = category;
        this.appUser = appUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Expense expense)) return false;
        return Objects.equals(getId(), expense.getId()) && Objects.equals(getAppUser(), expense.getAppUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getAppUser());
    }
}
