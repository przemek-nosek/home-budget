package pl.java.homebudget.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.java.homebudget.dto.AdditionalUserDataDto;
import pl.java.homebudget.dto.AuditDto;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.enums.ExpensesCategory;
import pl.java.homebudget.repository.AppUserRepository;
import pl.java.homebudget.service.auditors.ExpenseAuditorService;
import pl.java.homebudget.service.impl.user.AdditionalUserDataService;
import pl.java.homebudget.service.mail.MailSenderService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
@EnableScheduling
public class ScheduleMailService {

    private final AuditorScheduledService auditorScheduledService;
    private final MailSenderService mailSenderService;
    private final AppUserRepository appUserRepository;
    private final ExpenseAuditorService expenseAuditorService;
    private final AdditionalUserDataService additionalUserDataService;

    @Scheduled(cron = "1 59 23 29-31 * *")
    public void scheduleMail() {
        LocalDateTime localDateTime = LocalDateTime.now();


        List<AppUser> allUsers = appUserRepository.findAll();

        allUsers.forEach(appUser -> {

            Map<ExpensesCategory, AuditDto> estimateAudit = expenseAuditorService.getEstimateAudit(
                    appUser,
                    String.valueOf(localDateTime.getYear()),
                    localDateTime.getMonth().name());

            boolean shouldSendMail = auditorScheduledService.shouldSendMail(estimateAudit);

            if(shouldSendMail) {
                AdditionalUserDataDto additionalUserData = additionalUserDataService.getAdditionalUserData(appUser);
                mailSenderService.sendMail(additionalUserData.getEmail(), estimateAudit);
            }
        });

    }
}
