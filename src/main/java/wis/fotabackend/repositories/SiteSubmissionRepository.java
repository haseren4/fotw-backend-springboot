package wis.fotabackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import wis.fotabackend.domains.SiteSubmission;

public interface SiteSubmissionRepository extends JpaRepository<SiteSubmission, Integer> {

}
