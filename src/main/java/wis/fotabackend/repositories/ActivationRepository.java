package wis.fotabackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import wis.fotabackend.domains.Activation;

import java.util.List;

public interface ActivationRepository extends JpaRepository<Activation, Integer> {
    List<Activation> findByUser_Id(int userId);
    List<Activation> findBySite_Id(int siteCode);
}
