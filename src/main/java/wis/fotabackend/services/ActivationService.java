package wis.fotabackend.services;

import wis.fotabackend.domains.Activation;

import java.util.List;
import java.util.Optional;

public interface ActivationService {
    Activation add(Activation activation);
    List<Activation> getAll();
    List<Activation> getByUserId(int userId);
    List<Activation> getByUserCallsign(String callsign);
    List<Activation> getBySiteCode(int siteCode);

    Optional<Activation> getCurrentByUserCallsign(String callsign);

    Optional<Activation> getById(int id);

    Activation endActivation(int id);

    Optional<Activation> endCurrentByCallsign(String callsign);
}
