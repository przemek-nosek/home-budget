package pl.java.homebudget.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.entity.CyclicExpense;

import java.util.List;

@Repository
public interface CyclicExpenseRepository extends JpaRepository<CyclicExpense, Long> {

    List<CyclicExpense> findAllByAppUser(AppUser appUser);
}
