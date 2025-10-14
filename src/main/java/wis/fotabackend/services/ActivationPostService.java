package wis.fotabackend.services;

import wis.fotabackend.domains.ActivationPost;

import java.util.List;

public interface ActivationPostService {
    ActivationPost add(ActivationPost post);
    List<ActivationPost> getAll();
    List<ActivationPost> getByUserId(int userId);
    List<ActivationPost> getByActivationId(int activationId);
}
