package wis.fotabackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wis.fotabackend.domains.Site;

@Repository
public interface SiteRepository extends JpaRepository<Site, Integer> {
}
