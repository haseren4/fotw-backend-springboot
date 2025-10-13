package wis.fotabackend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wis.fotabackend.domains.Users;
import wis.fotabackend.dto.RegisterUserDTO;
import wis.fotabackend.services.UsersService;
import wis.fotabackend.services.UsersServiceImpl;

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
}
