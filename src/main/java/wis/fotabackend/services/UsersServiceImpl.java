package wis.fotabackend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wis.fotabackend.domains.Users;
import wis.fotabackend.repositories.UsersRepository;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {
    private final UsersRepository userRepo;

    public Users getRequiredByCallsign(String callsign) {
        return userRepo.findByCallsignCaseInsensitive(callsign)
                .orElseThrow(() -> new IllegalArgumentException("Unknown callsign: " + callsign));
    }
    public Users getOrCreateByCallsign(String callsign) {
        return userRepo.findByCallsignCaseInsensitive(callsign)
                .orElseGet(() -> userRepo.save(
                        Users.builder()
                                .callsign(callsign)
                                .email(callsign.toLowerCase() + "@example.invalid")
                                .password_hash("{noop}")         // or a sentinel; don’t allow login
                                .created_at(Instant.now().toString())
                                .build()
                ));
    }
}
