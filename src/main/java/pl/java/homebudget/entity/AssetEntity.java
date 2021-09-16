package pl.java.homebudget.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.java.homebudget.enums.AssetCategory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "assets")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AssetEntity {
    @Id
    @GeneratedValue
    @Column(updatable = false, nullable = false)
    private Long id;

    @NotNull(message = "Amount is null.")
    private BigDecimal amount;

    @NotNull(message = "Income date is null.")
    private Instant incomeDate;

    private AssetCategory category;

    public AssetEntity(BigDecimal amount, Instant incomeDate, AssetCategory category) {
        this.amount = amount;
        this.incomeDate = incomeDate;
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AssetEntity that)) return false;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
