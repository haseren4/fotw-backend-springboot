package wis.fotabackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import wis.fotabackend.domains.ActivationPost;

import java.util.List;

public interface ActivationPostRepository extends JpaRepository<ActivationPost, Integer> {
    List<ActivationPost> findByUser_Id(int userId);
    List<ActivationPost> findByActivation_Id(int activationId);
}
