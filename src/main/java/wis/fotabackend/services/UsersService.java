package wis.fotabackend.services;

import wis.fotabackend.domains.Users;

import java.util.Optional;

public interface UsersService {

    public Users getRequiredByCallsign(String callsign);
    public Users getOrCreateByCallsign(String callsign);
}
