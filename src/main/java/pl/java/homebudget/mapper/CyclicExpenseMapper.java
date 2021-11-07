package pl.java.homebudget.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import pl.java.homebudget.dto.CyclicExpenseDto;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.entity.CyclicExpense;

import java.util.List;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface CyclicExpenseMapper {

    default List<CyclicExpense> fromDtoListToCyclicExpenseList(List<CyclicExpenseDto> cyclicExpenseDtoList, AppUser appUser) {
        return cyclicExpenseDtoList.stream()
                .map(dto -> fromDtoToCyclicExpense(dto, appUser))
                .toList();
    }

    List<CyclicExpenseDto> fromCyclicExpenseListToDtoList(List<CyclicExpense> cyclicExpenses);

    @Mapping(source = "cyclicExpenseDto.id", target = "id")
    @Mapping(source = "appUser", target = "appUser")
    CyclicExpense fromDtoToCyclicExpense(CyclicExpenseDto cyclicExpenseDto, AppUser appUser);

    List<CyclicExpenseDto> fromCyclicExpenseToDto(List<CyclicExpense> cyclicExpense);
}
