package wis.fotabackend.services;

import wis.fotabackend.domains.Users;

public interface UsersService {

    public Users getRequiredByCallsign(String callsign);
    public Users getOrCreateByCallsign(String callsign);

    Users add(Users user);

    Users isCorrect(String callsign, String password);
}
