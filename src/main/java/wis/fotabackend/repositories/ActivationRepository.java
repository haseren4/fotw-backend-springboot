package wis.fotabackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import wis.fotabackend.domains.Activation;

import java.util.List;
import java.util.Optional;

public interface ActivationRepository extends JpaRepository<Activation, Integer> {
    List<Activation> findByUser_Id(int userId);
    List<Activation> findByUser_Callsign(String callsign);
    List<Activation> findBySite_Id(int siteCode);

    // Current activation helpers
    List<Activation> findByUser_CallsignAndEndTimeIsNull(String callsign);
    Optional<Activation> findFirstByUser_CallsignAndEndTimeIsNullOrderByStartTimeDesc(String callsign);
}
