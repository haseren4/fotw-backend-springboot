package wis.fotabackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import wis.fotabackend.domains.Contact;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Integer> {
    List<Contact> findByActivation_Id(int activationId);
}
