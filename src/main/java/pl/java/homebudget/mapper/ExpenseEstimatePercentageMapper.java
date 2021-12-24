package pl.java.homebudget.mapper;

import org.mapstruct.Mapper;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.entity.ExpenseEstimatePercentage;
import pl.java.homebudget.enums.ExpensesCategory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring")
public interface ExpenseEstimatePercentageMapper {

    default List<ExpenseEstimatePercentage> fromMapToEntity(Map<ExpensesCategory, BigDecimal> categoryBigDecimalMap, AppUser appUser) {
        return categoryBigDecimalMap.entrySet()
                .stream()
                .map(e -> ExpenseEstimatePercentage.builder()
                        .expensesCategory(e.getKey())
                        .percentage(e.getValue())
                        .build())
                .toList();
    }
}
