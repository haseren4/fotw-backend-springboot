package wis.fotabackend.services;

import wis.fotabackend.domains.Activation;

import java.util.List;

public interface ActivationService {
    Activation add(Activation activation);
    List<Activation> getAll();
    List<Activation> getByUserId(int userId);
    List<Activation> getBySiteCode(int siteCode);
}
