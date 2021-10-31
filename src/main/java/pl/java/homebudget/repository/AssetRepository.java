package pl.java.homebudget.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.entity.Asset;
import pl.java.homebudget.entity.Expense;
import pl.java.homebudget.enums.AssetCategory;
import pl.java.homebudget.enums.ExpensesCategory;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {
    List<Asset> findAllByCategoryAndAppUser(AssetCategory category, AppUser appUser);

    List<Asset> findAllByAppUser(AppUser appUser);

    void deleteAllByAppUser(AppUser appUser);

    Optional<Asset> findByIdAndAppUser(Long id, AppUser appUser);

    List<Asset> findAllByIncomeDateBetweenAndAppUserAndCategoryIn(Instant from, Instant to, AppUser appUser, List<AssetCategory> categories);
}

