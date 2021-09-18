package pl.java.homebudget.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.java.homebudget.entity.AssetEntity;
import pl.java.homebudget.enums.AssetCategory;

import java.util.List;

@Repository
public interface AssetRepository extends JpaRepository<AssetEntity, Long> {
    List<AssetEntity> getAssetEntitiesByCategory(AssetCategory category);
}
