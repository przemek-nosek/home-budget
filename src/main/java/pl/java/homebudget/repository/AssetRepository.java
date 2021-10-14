package pl.java.homebudget.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.entity.Asset;
import pl.java.homebudget.enums.AssetCategory;

import java.util.List;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {
    List<Asset> getAssetEntitiesByCategoryAndAppUser(AssetCategory category, AppUser appUser);

    List<Asset> getAssetsByAppUser(AppUser appUser);

    void deleteAllByAppUser(AppUser appUser);
}

