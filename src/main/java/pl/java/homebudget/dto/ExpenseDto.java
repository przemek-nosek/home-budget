package pl.java.homebudget.dto;

import lombok.*;
import pl.java.homebudget.enums.ExpensesCategory;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseDto {
    private Long id;

    @NotNull(message = "Amount is null.")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be higher than 0.00")
    private BigDecimal amount;

    @NotNull(message = "Purchase date is null.")
    private Instant purchaseDate;

    @NotNull(message = "Category is null.")
    private ExpensesCategory category;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExpenseDto that)) return false;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
