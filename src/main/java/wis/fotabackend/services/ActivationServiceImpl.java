package wis.fotabackend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wis.fotabackend.domains.Activation;
import wis.fotabackend.repositories.ActivationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivationServiceImpl implements ActivationService {
    private final ActivationRepository repo;

    @Override
    public Activation add(Activation activation) {
        return repo.save(activation);
    }

    @Override
    public List<Activation> getAll() {
        return repo.findAll();
    }

    @Override
    public List<Activation> getByUserId(int userId) {
        return repo.findByUser_Id(userId);
    }

    @Override
    public List<Activation> getBySiteCode(int siteCode) {
        return repo.findBySite_Id(siteCode);
    }
}
