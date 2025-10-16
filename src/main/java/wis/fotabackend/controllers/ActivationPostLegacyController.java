package wis.fotabackend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wis.fotabackend.domains.Activation;
import wis.fotabackend.domains.ActivationPost;
import wis.fotabackend.domains.Users;
import wis.fotabackend.repositories.ActivationRepository;
import wis.fotabackend.repositories.UsersRepository;
import wis.fotabackend.services.ActivationPostService;

import java.net.URI;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Compatibility controller to support the legacy frontend endpoint
 * GET /api/activation_post?activation_id=...&limit=...&sort=desc|asc
 * and POST /api/activation_post to create a new activation post using
 * snake_case parameter names.
 *
 * The modern endpoint is /api/activation-posts with camelCase params, but
 * the extension UI calls the legacy snake_case path. This controller
 * translates the request and delegates to the service layer.
 */
@RestController
@RequestMapping("/api/activation_post")
public class ActivationPostLegacyController {
    private final ActivationPostService postService;
    private final UsersRepository usersRepository;
    private final ActivationRepository activationRepository;

    public ActivationPostLegacyController(ActivationPostService postService,
                                          UsersRepository usersRepository,
                                          ActivationRepository activationRepository) {
        this.postService = postService;
        this.usersRepository = usersRepository;
        this.activationRepository = activationRepository;
    }

    @GetMapping
    public ResponseEntity<List<ActivationPost>> listByActivation(
            @RequestParam(name = "activation_id") int activationId,
            @RequestParam(name = "limit", required = false) Integer limit,
            @RequestParam(name = "sort", required = false, defaultValue = "desc") String sortDirection
    ) {
        List<ActivationPost> posts = postService.getByActivationId(activationId);

        // Sort by post_time (string ISO-8601) descending by default; fallback to id if post_time is null
        Comparator<ActivationPost> comparator = Comparator
                .comparing((ActivationPost p) -> p.getPostTime() == null ? "" : p.getPostTime())
                .thenComparingInt(ActivationPost::getId);

        String dir = sortDirection == null ? "desc" : sortDirection.toLowerCase(Locale.ROOT);
        if (!"asc".equals(dir)) {
            // default to desc for any non-"asc" value
            comparator = comparator.reversed();
        }

        List<ActivationPost> sorted = posts.stream()
                .sorted(comparator)
                .collect(Collectors.toList());

        if (limit != null && limit > 0 && limit < sorted.size()) {
            sorted = sorted.subList(0, limit);
        }

        return ResponseEntity.ok(sorted);
    }

    @PostMapping(consumes = {"application/json", "application/x-www-form-urlencoded", "text/plain"})
    public ResponseEntity<ActivationPost> createLegacy(
            @RequestParam(name = "activation_id", required = false) Integer activationIdParam,
            @RequestParam(name = "callsign", required = false) String callsignParam,
            @RequestParam(name = "author_callsign", required = false) String authorCallsignParam,
            @RequestParam(name = "title", required = false) String titleParam,
            @RequestParam(name = "body", required = false) String bodyParam,
            @RequestBody(required = false) Map<String, Object> json
    ) {
        // Pull values from either params (form) or JSON body (snake_case keys)
        final Integer resolvedActivationId;
        String resolvedCallsign;
        String resolvedTitle;
        String resolvedBody;
        String resolvedPostTime = null;

        if (json == null) {
            resolvedActivationId = activationIdParam;
            resolvedCallsign = (callsignParam != null && !callsignParam.isBlank()) ? callsignParam : authorCallsignParam;
            resolvedTitle = titleParam;
            resolvedBody = bodyParam;
        } else {
            Integer id = activationIdParam;
            if (id == null) {
                Object o = json.get("activation_id");
                if (o instanceof Number) { id = ((Number) o).intValue(); }
                else if (o instanceof String) { try { id = Integer.parseInt((String) o); } catch (NumberFormatException ignored) {} }
            }
            // Accept both legacy keys and new keys (content, created_at, author_callsign)
            String cs;
            if (callsignParam != null && !callsignParam.isBlank()) {
                cs = callsignParam;
            } else if (authorCallsignParam != null && !authorCallsignParam.isBlank()) {
                cs = authorCallsignParam;
            } else {
                Object c1 = json.get("callsign");
                Object c2 = json.get("author_callsign");
                cs = c1 instanceof String s1 && !s1.isBlank() ? s1 : (c2 instanceof String s2 ? s2 : null);
            }
            String ti = (titleParam != null && !titleParam.isBlank()) ? titleParam : (String) json.getOrDefault("title", null);
            String bo = (bodyParam != null && !bodyParam.isBlank()) ? bodyParam :
                    (String) (json.containsKey("body") ? json.get("body") : json.get("content"));
            Object createdAt = json.get("created_at");
            if (createdAt instanceof String str && !str.isBlank()) {
                resolvedPostTime = str;
            }

            resolvedActivationId = id;
            resolvedCallsign = cs;
            resolvedTitle = ti;
            resolvedBody = bo;
        }

        if (resolvedActivationId == null) {
            return ResponseEntity.badRequest().build();
        }

        Activation activation = activationRepository.findById(resolvedActivationId)
                .orElseThrow(() -> new IllegalArgumentException("Unknown activation id: " + resolvedActivationId));

        // If callsign missing, fallback to activation's user
        Users user;
        if (resolvedCallsign == null || resolvedCallsign.isBlank()) {
            user = activation.getUser();
            if (user == null) {
                return ResponseEntity.badRequest().build();
            }
        } else {
            user = usersRepository.findByCallsign(resolvedCallsign)
                    .orElseThrow(() -> new IllegalArgumentException("Unknown callsign: " + resolvedCallsign));
        }

        ActivationPost post = new ActivationPost();
        post.setUser(user);
        post.setActivation(activation);
        post.setPostTime(resolvedPostTime != null ? resolvedPostTime : Instant.now().toString());
        post.setTitle(resolvedTitle);
        post.setBody(resolvedBody);

        ActivationPost saved = postService.add(post);
        return ResponseEntity.created(URI.create("/api/activation-posts/" + saved.getId())).body(saved);
    }
}
