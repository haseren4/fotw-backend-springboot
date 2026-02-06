package wis.fotabackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wis.fotabackend.domains.Issue;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Integer> {
}
