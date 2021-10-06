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
public class Asset {
    @Id
    @GeneratedValue
    @Column(updatable = false, nullable = false)
    private Long id;

    @NotNull(message = "Amount is null.")
    private BigDecimal amount;

    @NotNull(message = "Income date is null.")
    private Instant incomeDate;

    @Enumerated(value = EnumType.STRING)
    private AssetCategory category;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @ToString.Exclude
    private AppUser appUser;

    public Asset(BigDecimal amount, Instant incomeDate, AssetCategory category, AppUser appUser) {
        this.amount = amount;
        this.incomeDate = incomeDate;
        this.category = category;
        this.appUser = appUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Asset asset)) return false;
        return Objects.equals(getId(), asset.getId()) && Objects.equals(getAppUser(), asset.getAppUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getAppUser());
    }
}
