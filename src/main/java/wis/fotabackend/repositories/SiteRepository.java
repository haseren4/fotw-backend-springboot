package wis.fotabackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import wis.fotabackend.domains.Site;

import java.util.List;

@Repository
public interface SiteRepository extends JpaRepository<Site, Integer> {
    @Query("SELECT s FROM Site s WHERE s.category = ?1")
    List<Site> getAllByCatagory(String catagory);
}
