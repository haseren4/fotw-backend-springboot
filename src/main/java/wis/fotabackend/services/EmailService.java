package wis.fotabackend.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import wis.fotabackend.domains.Issue;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final String fromEmail;

    public EmailService(
            JavaMailSender mailSender,
            @Value("${app.mail.from}") String fromEmail
    ) {
        this.mailSender = mailSender;
        this.fromEmail = fromEmail;
    }

    public void sendIssueNotification(String toEmail, Issue issue) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("admin@fortsontheair.com");
        message.setTo(toEmail);
        message.setSubject("New Issue Submitted - Fortifications on the Air");

        message.setText("""
                A new issue has been submitted.

                Issue ID: %s
                Title: %s
                Description: %s
                Submitted By: %s

                Please review this issue in the admin system.
                """.formatted(
                issue.getId(),
                issue.getTitle(),
                issue.getDescription(),
                issue.getReporter()
        ));

        mailSender.send(message);
    }
}