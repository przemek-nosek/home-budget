package pl.java.homebudget.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.java.homebudget.dto.CyclicExpenseDto;
import pl.java.homebudget.dto.UserLoggedInfo;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.entity.CyclicExpense;
import pl.java.homebudget.mapper.CyclicExpenseMapper;
import pl.java.homebudget.repository.CyclicExpenseRepository;
import pl.java.homebudget.service.CyclicExpenseService;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class CyclicExpenseServiceImpl implements CyclicExpenseService {

    private final CyclicExpenseRepository cyclicExpenseRepository;
    private final UserLoggedInfo userLoggedInfo;
    private final CyclicExpenseMapper cyclicExpenseMapper;

    @Override
    public List<CyclicExpenseDto> getAllCyclicExpenses() {
        log.info("getAllCyclicExpenses");

        AppUser loggedAppUser = userLoggedInfo.getLoggedAppUser();

        List<CyclicExpense> cyclicExpenses = cyclicExpenseRepository.findAllByAppUser(loggedAppUser);

        return cyclicExpenseMapper.fromCyclicExpenseListToDtoList(cyclicExpenses);
    }

    @Override
    public List<CyclicExpenseDto> addCyclicExpense(List<CyclicExpenseDto> cyclicExpenseDto) {
        log.info("addCyclicExpense");

        AppUser loggedAppUser = userLoggedInfo.getLoggedAppUser();

        List<CyclicExpense> cyclicExpenses = cyclicExpenseMapper.fromDtoListToCyclicExpenseList(cyclicExpenseDto, loggedAppUser);

        List<CyclicExpense> savedCyclicExpense = cyclicExpenseRepository.saveAll(cyclicExpenses);

        return cyclicExpenseMapper.fromCyclicExpenseToDto(savedCyclicExpense);
    }
}
