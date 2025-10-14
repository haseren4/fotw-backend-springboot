package wis.fotabackend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wis.fotabackend.domains.ActivationPost;
import wis.fotabackend.repositories.ActivationPostRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivationPostServiceImpl implements ActivationPostService {
    private final ActivationPostRepository repo;

    @Override
    public ActivationPost add(ActivationPost post) {
        return repo.save(post);
    }

    @Override
    public List<ActivationPost> getAll() {
        return repo.findAll();
    }

    @Override
    public List<ActivationPost> getByUserId(int userId) {
        return repo.findByUser_Id(userId);
    }

    @Override
    public List<ActivationPost> getByUserCallsign(String callsign) {
        return repo.findByUser_Callsign(callsign);
    }

    @Override
    public List<ActivationPost> getByActivationId(int activationId) {
        return repo.findByActivation_Id(activationId);
    }
}
