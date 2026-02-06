package wis.fotabackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wis.fotabackend.domains.Issue;
import wis.fotabackend.repositories.IssueRepository;

import java.util.List;

@Service
public class IssueServiceImpl implements IssueService {
    IssueRepository repository;

    @Autowired
    public IssueServiceImpl(IssueRepository repository){
        this.repository = repository;
    }
    @Override
    public Issue add(Issue issue) {
        return repository.save(issue);
    }

    @Override
    public List<Issue> getAll() {
        return repository.findAll();
    }
}
