package wis.fotabackend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import wis.fotabackend.domains.Users;
import wis.fotabackend.repositories.UsersRepository;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {
    private final UsersRepository userRepo;
    private final PasswordEncoder passwordEncoder;

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

    @Override
    public Users add(Users user) {
        return userRepo.findByCallsignCaseInsensitive(user.getCallsign())
                .orElseGet(() -> {
                    // Encode password if it looks like a raw value
                    String ph = user.getPassword_hash();
                    if (ph != null && !ph.isBlank() && !looksLikeBCrypt(ph) && !"{noop}".equals(ph)) {
                        user.setPassword_hash(passwordEncoder.encode(ph));
                    }
                    return userRepo.save(user);
                });
    }

    @Override
    public Users isCorrect(String callsign, String password) {
        return userRepo.findByCallsignCaseInsensitive(callsign)
                .filter(u -> passwordMatches(u.getPassword_hash(), password))
                .orElse(null);
    }

    private boolean passwordMatches(String storedHash, String rawPassword) {
        if (storedHash == null || storedHash.isBlank()) return false;
        if ("{noop}".equals(storedHash)) return false; // explicitly disallow sentinel
        if (looksLikeBCrypt(storedHash)) {
            return passwordEncoder.matches(rawPassword, storedHash);
        }
        // Legacy plain-text fallback
        return storedHash.equals(rawPassword);
    }

    private boolean looksLikeBCrypt(String value) {
        // BCrypt hashes start with $2a, $2b, or $2y and are 60 chars long
        return value != null && value.length() >= 60 && (value.startsWith("$2a$") || value.startsWith("$2b$") || value.startsWith("$2y$"));
    }
}
