package pl.java.homebudget.filter;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.entity.Asset;
import pl.java.homebudget.repository.AssetRepository;

import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@Component
public class AssetFilterRange extends FilterRange<Asset>{

    private final AssetRepository assetRepository;

    @Override
    protected List<Asset> getEntitiesWithinDate(AppUser appUser, Instant from, Instant to) {
        return assetRepository.findAllByIncomeDateBetweenAndAppUser(from, to, appUser);
    }
}
