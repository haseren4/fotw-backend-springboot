package wis.fotabackend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wis.fotabackend.domains.Contact;
import wis.fotabackend.repositories.ContactRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {
    private final ContactRepository repo;

    @Override
    public List<Contact> saveAll(List<Contact> contacts) {
        return repo.saveAll(contacts);
    }
}
