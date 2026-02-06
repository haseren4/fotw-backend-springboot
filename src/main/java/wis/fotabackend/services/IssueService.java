package wis.fotabackend.services;

import wis.fotabackend.domains.Issue;

import java.util.List;

public interface IssueService {
    Issue add(Issue issue);
    List<Issue> getAll();
}
