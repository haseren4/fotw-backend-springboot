package wis.fotabackend.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wis.fotabackend.domains.Users;
import wis.fotabackend.dto.LoginRequest;
import wis.fotabackend.dto.RegisterUserDTO;
import wis.fotabackend.dto.DashboardGreeting;
import wis.fotabackend.services.UsersService;
import wis.fotabackend.services.UsersServiceImpl;

import java.time.Duration;
import java.time.Instant;

@RestController
@RequestMapping("/api/users")
public class UsersController {
    public UsersService userService;
    public UsersController(UsersServiceImpl userService){
        this.userService = userService;
    }

    @PostMapping(value="/register")
    public ResponseEntity<Void> register(@RequestBody RegisterUserDTO registerUserDTO){
        System.out.println("Registering user: " + registerUserDTO);

        Users user = new Users();
        user.setCallsign(registerUserDTO.getCallsign());
        user.setEmail(registerUserDTO.getEmail());
        user.setPassword_hash(registerUserDTO.getPassword());
        user.setCreated_at(Instant.now().toString());

        userService.add(user);
        System.out.println("User registered: " + user);
        return ResponseEntity.accepted().build();
    }

    @PostMapping(value="/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest){
        String callsign = loginRequest.getCallsign();
        String password = loginRequest.getPassword();
        Users user = userService.isCorrect(callsign, password);
        if(user == null) {
            return ResponseEntity.status(401).build();
        }
        System.out.println("Logging in user: " + user.getCallsign() + " | email: "+ user.getEmail());

        // Set a lightweight cookie so the browser can keep the logged-in callsign.
        // Note: SameSite=None requires Secure; in local HTTP dev this cookie will be ignored by browsers.
        ResponseCookie cookie = ResponseCookie.from("callsign", user.getCallsign())
                .httpOnly(false)                 // allow frontend to read if needed
                .secure(true)                    // required for SameSite=None on modern browsers
                .sameSite("None")               // allow cross-site cookie with withCredentials
                .path("/")
                .maxAge(Duration.ofDays(30))
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @GetMapping(value = "/dashboard")
    public ResponseEntity<DashboardGreeting> dashboard(@RequestParam String callsign) {
        Users user = userService.getRequiredByCallsign(callsign);
        String greeting = "Hello " + user.getCallsign();
        String motd = "73s and good DX! " + Instant.now().toString();
        DashboardGreeting payload = new DashboardGreeting(user.getCallsign(), greeting, motd);
        return ResponseEntity.ok(payload);
    }
}
