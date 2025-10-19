package wis.fotabackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import wis.fotabackend.domains.Contact;

public interface ContactRepository extends JpaRepository<Contact, Integer> {
}
