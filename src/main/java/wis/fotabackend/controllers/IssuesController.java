package wis.fotabackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wis.fotabackend.domains.Issue;
import wis.fotabackend.domains.Users;
import wis.fotabackend.dto.CreateIssueDTO;
import wis.fotabackend.services.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/issue")
public class IssuesController {
    private IssueService issueService;
    private EmailService emailService;
    private UsersService usersService;

    @Autowired
    public IssuesController(UsersServiceImpl usersService,IssueServiceImpl issueService, EmailService emailService) {
        this.issueService = issueService;
        this.emailService = emailService;
        this.usersService = usersService;
    }

    @PostMapping
    public Issue createIssue(@RequestBody CreateIssueDTO dto) {
        Issue issue = new Issue();
        issue.setReporter(dto.getEmail());
        issue.setTitle(dto.getTitle());
        issue.setDescription(dto.getBody());
        issue.setReportDate(LocalDateTime.now());
        List<Users> adminUsers = usersService.getAllByRole("ADMIN");
        /*for (Users admin : adminUsers) {
            emailService.sendIssueNotification(admin.getEmail(), issue);
        }*/
        emailService.sendIssueNotification("haseren4@gmail.com", issue);
        return issueService.add(issue);
    }
}
