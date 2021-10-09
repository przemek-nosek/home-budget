package pl.java.homebudget.dto;

import lombok.*;
import pl.java.homebudget.enums.AssetCategory;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AssetDto {

    private Long id;

    @NotNull(message = "Amount is null.")
    private BigDecimal amount;

    @NotNull(message = "Income date is null.")
    private Instant incomeDate;

    @NotNull(message = "Category is null.")
    private AssetCategory category;

    public AssetDto(BigDecimal amount, Instant incomeDate, AssetCategory category) {
        this.amount = amount;
        this.incomeDate = incomeDate;
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AssetDto assetDto)) return false;
        return Objects.equals(getId(), assetDto.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
