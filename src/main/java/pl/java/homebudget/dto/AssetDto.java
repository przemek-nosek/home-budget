package pl.java.homebudget.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.java.homebudget.enums.AssetCategory;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

@Getter
@Setter
@ToString
public class AssetDto {
    private Long id;
    private BigDecimal amount;
    private Instant incomeDate;
    private AssetCategory category;

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
