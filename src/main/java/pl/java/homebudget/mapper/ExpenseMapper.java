package pl.java.homebudget.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.java.homebudget.dto.ExpenseDto;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.entity.Expense;

import java.util.List;

@Mapper(componentModel = "spring") // TODO: @Autowired
public interface ExpenseMapper {

    @Mapping(source = "dto.id", target = "id")
    Expense fromDtoToExpense(ExpenseDto dto, AppUser appUser);

    @Mapping(target = "amount")
    ExpenseDto fromExpenseToDto(Expense asset);

    default List<Expense> fromDtoListToExpenseList(List<ExpenseDto> expenseDtoList, AppUser appUser) {
        return expenseDtoList.stream()
                .map(expenseDto -> fromDtoToExpense(expenseDto, appUser))
                .toList();
    }

    List<ExpenseDto> fromExpenseListToExpenseDtoList(List<Expense> expenses);
}
