package pl.java.homebudget.dto;

import lombok.*;
import pl.java.homebudget.enums.AssetCategory;

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
@Builder
public class AssetDto {

    private Long id;

    @NotNull(message = "Amount is null.")
    @DecimalMin(value = "0.00", inclusive = false)
    private BigDecimal amount;

    @NotNull(message = "Income date is null.")
    private Instant incomeDate;

    @NotNull(message = "Category is null.")
    private AssetCategory category;

    private String description;

    public AssetDto(BigDecimal amount, Instant incomeDate, AssetCategory category) {
        this.amount = amount;
        this.incomeDate = incomeDate;
        this.category = category;
    }

    public AssetDto(BigDecimal amount, AssetCategory category, String description) {
        this.amount = amount;
        this.category = category;
        this.description = description;
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
