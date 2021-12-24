package pl.java.homebudget.service.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.entity.ExpenseEstimatePercentage;
import pl.java.homebudget.enums.ExpensesCategory;
import pl.java.homebudget.service.impl.ExpenseEstimatePercentageService;
import pl.java.homebudget.service.init.InitDataForIT;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ExpenseEstimatePercentageServiceTestIT extends InitDataForIT {

    @Autowired
    private ExpenseEstimatePercentageService expenseEstimatePercentageService;

    @Test
    void shouldAddEstimation() {
        //given
        initDatabaseWithFirstUser();

        BigDecimal funPercentage = new BigDecimal("25.00");
        BigDecimal otherPercentage = new BigDecimal("35.00");
        Map<ExpensesCategory, BigDecimal> estimatePercentageBigDecimalMap =
                Map.of(ExpensesCategory.FUN, funPercentage,
                        ExpensesCategory.OTHER, otherPercentage);

        //when
        Map<ExpensesCategory, BigDecimal> savedEstimates = expenseEstimatePercentageService.addEstimation(estimatePercentageBigDecimalMap);

        //then
        assertThat(savedEstimates).hasSize(2)
                .containsExactlyEntriesOf(estimatePercentageBigDecimalMap);
    }

    @Test
    void shouldGetAllEstimations() {
        //given
        AppUser appUser = initDatabaseWithFirstUser();
        BigDecimal funPercentage = new BigDecimal("25.00");
        BigDecimal otherPercentage = new BigDecimal("35.00");
        var expenseEstimatePercentage = new ExpenseEstimatePercentage(appUser, funPercentage, ExpensesCategory.OTHER);
        var expenseEstimatePercentage2 = new ExpenseEstimatePercentage(appUser, otherPercentage, ExpensesCategory.FUN);

        var savedEstimate1 = initDatabaseWithExpenseEstimateAndUser(appUser, expenseEstimatePercentage);
        var savedEstimate2 = initDatabaseWithExpenseEstimateAndUser(appUser, expenseEstimatePercentage2);

        Map<ExpensesCategory, BigDecimal> retrievedEstimates = Map.of(
                savedEstimate1.getExpensesCategory(), savedEstimate1.getPercentage(),
                savedEstimate2.getExpensesCategory(), savedEstimate2.getPercentage());

        assertThat(expenseEstimatePercentageRepository.count()).isEqualTo(2L);

        //when
        Map<ExpensesCategory, BigDecimal> estimations = expenseEstimatePercentageService.getEstimations();

        //then
        assertThat(estimations)
                .hasSize(2)
                .containsExactlyInAnyOrderEntriesOf(retrievedEstimates);
    }
}