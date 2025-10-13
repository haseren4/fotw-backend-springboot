package wis.fotabackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import wis.fotabackend.domains.SiteSubmission;

@Repository
public interface SiteSubmissionRepository extends JpaRepository<SiteSubmission, Integer> {

}
