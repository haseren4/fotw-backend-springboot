package wis.fotabackend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wis.fotabackend.domains.Activation;
import wis.fotabackend.domains.ActivationPost;
import wis.fotabackend.domains.Users;
import wis.fotabackend.dto.CreateActivationPostDTO;
import wis.fotabackend.repositories.ActivationRepository;
import wis.fotabackend.repositories.UsersRepository;
import wis.fotabackend.services.ActivationPostService;
import wis.fotabackend.services.ActivationPostServiceImpl;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/activation-posts")
public class ActivationPostController {
    private final ActivationPostService postService;
    private final UsersRepository usersRepository;
    private final ActivationRepository activationRepository;

    public ActivationPostController(ActivationPostServiceImpl postService,
                                    UsersRepository usersRepository,
                                    ActivationRepository activationRepository) {
        this.postService = postService;
        this.usersRepository = usersRepository;
        this.activationRepository = activationRepository;
    }

    @PostMapping
    public ResponseEntity<ActivationPost> create(@RequestBody CreateActivationPostDTO dto) {
        Users user = usersRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Unknown user id: " + dto.getUserId()));
        Activation activation = activationRepository.findById(dto.getActivationId())
                .orElseThrow(() -> new IllegalArgumentException("Unknown activation id: " + dto.getActivationId()));

        ActivationPost post = new ActivationPost();
        post.setUser(user);
        post.setActivation(activation);
        post.setPostTime(dto.getPostTime());
        post.setTitle(dto.getTitle());
        post.setBody(dto.getBody());

        ActivationPost saved = postService.add(post);
        return ResponseEntity.created(URI.create("/api/activation-posts/" + saved.getId())).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<ActivationPost>> list(@RequestParam(required = false) Integer userId,
                                                     @RequestParam(required = false) Integer activationId) {
        if (userId != null) {
            return ResponseEntity.ok(postService.getByUserId(userId));
        }
        if (activationId != null) {
            return ResponseEntity.ok(postService.getByActivationId(activationId));
        }
        return ResponseEntity.ok(postService.getAll());
    }
}
