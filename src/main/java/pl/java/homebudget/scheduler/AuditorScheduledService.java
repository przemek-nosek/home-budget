package pl.java.homebudget.scheduler;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.java.homebudget.dto.AuditDto;
import pl.java.homebudget.enums.ExpensesCategory;

import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
class AuditorScheduledService {

    public boolean shouldSendMail(Map<ExpensesCategory, AuditDto> estimateAudit) {
        Map<ExpensesCategory, AuditDto> difference = estimateAudit.entrySet()
                .stream()
                .filter(estimate -> {
                    AuditDto dto = estimate.getValue();
                    return dto.getRealAmount().compareTo(dto.getPlannedAmount()) > 0;
                })
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue));

        return !difference.isEmpty();
    }
}
