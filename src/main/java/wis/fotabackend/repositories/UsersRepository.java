package wis.fotabackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import wis.fotabackend.domains.Users;

import java.util.Optional;


@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {

    @Query("select u from Users u where u.callsign = :callsign")
    Optional<Users> findByCallsign(@Param("callsign") String callsign);

    @Query("select u from Users u where lower(u.callsign) = lower(:callsign)")
    Optional<Users> findByCallsignCaseInsensitive(@Param("callsign") String callsign);
}
