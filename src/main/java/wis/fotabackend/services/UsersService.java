package wis.fotabackend.services;

import wis.fotabackend.domains.Users;

import java.util.List;

public interface UsersService {

    public Users getRequiredByCallsign(String callsign);
    public Users getOrCreateByCallsign(String callsign);

    Users add(Users user);

    Users isCorrect(String callsign, String password);

    List<Users> getAllByRole(String admin);
}
