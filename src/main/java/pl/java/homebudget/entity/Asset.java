package pl.java.homebudget.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.java.homebudget.enums.AssetCategory;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "assets")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Asset extends BaseEntity {

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private Instant incomeDate;

    @Enumerated(value = EnumType.STRING)
    private AssetCategory category;

    private String description;

    public Asset(BigDecimal amount, Instant incomeDate, AssetCategory category, AppUser appUser) {
        super(appUser);
        this.amount = amount;
        this.incomeDate = incomeDate;
        this.category = category;
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
