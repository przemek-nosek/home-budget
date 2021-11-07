package pl.java.homebudget.dto;

import lombok.*;
import pl.java.homebudget.enums.AssetCategory;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SuggestedAssetDto {
    private BigDecimal cost;
    private AssetCategory assetCategory;
    private String location;
}
