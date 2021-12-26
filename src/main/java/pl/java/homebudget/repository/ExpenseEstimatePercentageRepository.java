package pl.java.homebudget.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.entity.ExpenseEstimatePercentage;

import java.util.List;

@Repository
public interface ExpenseEstimatePercentageRepository extends JpaRepository<ExpenseEstimatePercentage, Long> {

    List<ExpenseEstimatePercentage> findAllByAppUser(AppUser appUser);
}
