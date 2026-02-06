package wis.fotabackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wis.fotabackend.domains.Issue;
import wis.fotabackend.dto.CreateIssueDTO;
import wis.fotabackend.services.IssueService;
import wis.fotabackend.services.IssueServiceImpl;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/issue")
public class IssuesController {
    private IssueService issueService;

    @Autowired
    public IssuesController(IssueServiceImpl issueService) {
        this.issueService = issueService;
    }

    @PostMapping
    public Issue createIssue(@RequestBody CreateIssueDTO dto) {
        Issue issue = new Issue();
        issue.setReporter(dto.getEmail());
        issue.setTitle(dto.getTitle());
        issue.setDescription(dto.getBody());
        issue.setReportDate(LocalDateTime.now());
        return issueService.add(issue);
    }
}
