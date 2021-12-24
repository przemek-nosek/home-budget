package pl.java.homebudget.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.entity.ExpenseEstimatePercentage;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseEstimatePercentageRepository extends JpaRepository<ExpenseEstimatePercentage, Long> {

    Optional<List<ExpenseEstimatePercentage>> findAllByAppUser(AppUser appUser);
}
