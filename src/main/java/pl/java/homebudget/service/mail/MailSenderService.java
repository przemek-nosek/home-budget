package pl.java.homebudget.service.mail;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import pl.java.homebudget.dto.AuditDto;
import pl.java.homebudget.enums.ExpensesCategory;

import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class MailSenderService {

    private final JavaMailSender javaMailSender;

    public void sendMail(String email, Map<ExpensesCategory, AuditDto> estimateAudit) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setTo(email);
        simpleMailMessage.setText(estimateAudit.toString());
        simpleMailMessage.setSubject("Przekroczono wydatki");

        javaMailSender.send(simpleMailMessage);
    }
}
