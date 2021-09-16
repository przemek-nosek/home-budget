package pl.java.homebudget.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.java.homebudget.entity.AssetEntity;

@Repository
public interface AssetRepository extends JpaRepository<AssetEntity, Long> {

}
