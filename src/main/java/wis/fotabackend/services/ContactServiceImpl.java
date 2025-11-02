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

    @Override
    public List<Contact> getAll() {
        return repo.findAll();
    }

    @Override
    public List<Contact> getByActivationId(int activationId) {
        return repo.findByActivation_Id(activationId);
    }
}
