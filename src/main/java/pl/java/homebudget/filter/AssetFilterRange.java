package pl.java.homebudget.filter;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.entity.Asset;
import pl.java.homebudget.enums.AssetCategory;
import pl.java.homebudget.repository.AssetRepository;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Component
public class AssetFilterRange extends FilterRange<Asset>{

    private final AssetRepository assetRepository;

    @Override
    protected List<Asset> getEntitiesWithinDate(AppUser appUser, Instant from, Instant to, String category) {
        List<AssetCategory> categories = mapStringToList(category);

        return assetRepository.findAllByIncomeDateBetweenAndAppUserAndCategoryIn(from, to, appUser, categories);
    }

    private List<AssetCategory> mapStringToList(String category) {
        if (Objects.isNull(category)) {
            return Arrays.stream(AssetCategory.values()).toList();
        }
        return List.of(AssetCategory.valueOf(category.toUpperCase()));
    }
}
