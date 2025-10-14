package wis.fotabackend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wis.fotabackend.domains.Activation;
import wis.fotabackend.repositories.ActivationRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

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
    public List<Activation> getByUserCallsign(String callsign) {
        return repo.findByUser_Callsign(callsign);
    }

    @Override
    public List<Activation> getBySiteCode(int siteCode) {
        return repo.findBySite_Id(siteCode);
    }

    @Override
    public Optional<Activation> getCurrentByUserCallsign(String callsign) {
        return repo.findFirstByUser_CallsignAndEndTimeIsNullOrderByStartTimeDesc(callsign);
    }

    @Override
    public Optional<Activation> getById(int id) {
        return repo.findById(id);
    }

    @Override
    public Activation endActivation(int id) {
        Activation a = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Activation not found: " + id));
        if (a.getEndTime() == null || a.getEndTime().isEmpty()) {
            a.setEndTime(Instant.now().toString());
            a = repo.save(a);
        }
        return a;
    }

    @Override
    public Optional<Activation> endCurrentByCallsign(String callsign) {
        return repo.findFirstByUser_CallsignAndEndTimeIsNullOrderByStartTimeDesc(callsign)
                .map(a -> {
                    a.setEndTime(Instant.now().toString());
                    return repo.save(a);
                });
    }
}
