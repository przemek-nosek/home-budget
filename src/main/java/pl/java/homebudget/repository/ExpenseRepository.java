package pl.java.homebudget.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.entity.Expense;
import pl.java.homebudget.enums.ExpensesCategory;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findAllByAppUser(AppUser appUser);

    List<Expense> findAllByCategoryAndAppUser(ExpensesCategory category, AppUser appUser);

    void deleteAllByAppUser(AppUser appUser);

    Optional<Expense> findByIdAndAppUser(Long id, AppUser appUser);
}
