package wis.fotabackend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import wis.fotabackend.domains.Activation;
import wis.fotabackend.domains.Contact;
import wis.fotabackend.domains.Site;
import wis.fotabackend.domains.Users;
import wis.fotabackend.dto.CreateActivationDTO;
import wis.fotabackend.repositories.SiteRepository;
import wis.fotabackend.repositories.UsersRepository;
import wis.fotabackend.services.ActivationService;
import wis.fotabackend.services.ActivationServiceImpl;
import wis.fotabackend.services.ContactService;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.*;

@RestController
@RequestMapping("/api/activations")
public class ActivationController {
    private final ActivationService activationService;
    private final UsersRepository usersRepository;
    private final SiteRepository siteRepository;
    private final ContactService contactService;

    public ActivationController(ActivationServiceImpl activationService,
                                UsersRepository usersRepository,
                                SiteRepository siteRepository,
                                ContactService contactService) {
        this.activationService = activationService;
        this.usersRepository = usersRepository;
        this.siteRepository = siteRepository;
        this.contactService = contactService;
    }

    @PostMapping
    public ResponseEntity<Activation> create(@RequestBody CreateActivationDTO dto) {
        Users user = usersRepository.findByCallsign(dto.getCallsign())
                .orElseThrow(() -> new IllegalArgumentException("Unknown callsign: " + dto.getCallsign()));
        Site site = siteRepository.findById(dto.getSiteId())
                .orElseThrow(() -> new IllegalArgumentException("Unknown site id: " + dto.getSiteId()));

        Activation a = new Activation();
        a.setUser(user);
        a.setSite(site);
        a.setStartTime(Instant.now().toString());

        a.setLogStatus(dto.getLogStatus());
        a.setSubmittedAt(Instant.now().toString());

        Activation saved = activationService.add(a);
        return ResponseEntity.created(URI.create("/api/activations/" + saved.getId())).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<Activation>> list(@RequestParam(required = false) String callsign,
                                                 @RequestParam(required = false) Integer siteId,
                                                 @RequestParam(required = false) Integer id) {
        if (id != null) {
            return activationService.getById(id)
                    .map(a -> ResponseEntity.ok(List.of(a)))
                    .orElseGet(() -> ResponseEntity.ok(List.of()));
        }
        if (callsign != null) {
            return ResponseEntity.ok(activationService.getByUserCallsign(callsign));
        }
        if (siteId != null) {
            return ResponseEntity.ok(activationService.getBySiteCode(siteId));
        }
        return ResponseEntity.ok(activationService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Activation> getById(@PathVariable int id) {
        return activationService.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/current")
    public ResponseEntity<Activation> current(@RequestParam String callsign) {
        return activationService.getCurrentByUserCallsign(callsign)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Activation> update(@PathVariable int id, @RequestBody(required = false) Map<String, Object> body) {
        // Preserve legacy behavior: if no body provided, end the activation now
        if (body == null || body.isEmpty()) {
            try {
                Activation updated = activationService.endActivation(id);
                return ResponseEntity.ok(updated);
            } catch (IllegalArgumentException notFound) {
                return ResponseEntity.notFound().build();
            }
        }

        return activationService.getById(id)
                .map(a -> {
                    try {
                        if (body.containsKey("started_at")) {
                            Object v = body.get("started_at");
                            if (v != null) a.setStartTime(String.valueOf(v));
                        }
                        if (body.containsKey("ended_at")) {
                            Object v = body.get("ended_at");
                            if (v != null) a.setEndTime(String.valueOf(v));
                        }
                        if (body.containsKey("site_id")) {
                            Object v = body.get("site_id");
                            if (v != null) {
                                int siteId = Integer.parseInt(String.valueOf(v));
                                Site site = siteRepository.findById(siteId)
                                        .orElseThrow(() -> new IllegalArgumentException("Unknown site id: " + siteId));
                                a.setSite(site);
                            }
                        }
                        Activation saved = activationService.add(a);
                        return ResponseEntity.ok(saved);
                    } catch (IllegalArgumentException ex) {
                        return ResponseEntity.badRequest().body((Activation) null);
                    }
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/current")
    public ResponseEntity<Activation> endCurrent(@RequestParam String callsign) {
        return activationService.endCurrentByCallsign(callsign)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping(path = "/current/adif", consumes = { "multipart/form-data" })
    public ResponseEntity<Map<String, Object>> importAdifAndEnd(@RequestParam String callsign,
                                                                @RequestPart("file") MultipartFile file) {
        Optional<Activation> currentOpt = activationService.getCurrentByUserCallsign(callsign);
        if (currentOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "No current activation for callsign: " + callsign));
        }
        Activation activation = currentOpt.get();
        Users user = activation.getUser();

        // Parse ADIF
        List<Map<String, String>> adifQsos;
        try {
            String content = new String(file.getBytes(), StandardCharsets.UTF_8);
            adifQsos = parseAdif(content);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to read ADIF: " + e.getMessage()));
        }

        Instant start = Instant.parse(activation.getStartTime());
        Instant now = Instant.now();

        // Transform to Contact entities, filter by activation window
        List<Contact> contacts = new ArrayList<>();
        for (Map<String, String> rec : adifQsos) {
            Instant qsoInstant = parseAdifDateTimeToInstant(rec.get("qso_date"), rec.get("time_on"));
            if (qsoInstant == null || qsoInstant.isBefore(start) || qsoInstant.isAfter(now)) {
                continue; // outside activation window
            }
            Contact c = new Contact();
            c.setActivation(activation);
            c.setUser(user);
            c.setCallsignWorked(rec.getOrDefault("call", null));
            c.setFrequency(rec.getOrDefault("freq", null));
            c.setMode(rec.getOrDefault("mode", null));
            c.setSignalReportSent(rec.getOrDefault("rst_sent", null));
            c.setSignalReportRecv(rec.getOrDefault("rst_rcvd", null));
            c.setQsoTime(qsoInstant.toString());
            contacts.add(c);
        }

        if (!contacts.isEmpty()) {
            contactService.saveAll(contacts);
        }

        // End the activation now
        activation = activationService.endActivation(activation.getId());

        Map<String, Object> resp = new LinkedHashMap<>();
        resp.put("imported", contacts.size());
        resp.put("activationId", activation.getId());
        resp.put("endedAt", activation.getEndTime());
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/{id}/adif", consumes = { "multipart/form-data" })
    public ResponseEntity<Map<String, Object>> importAdifByIdAndEnd(@PathVariable int id,
                                                                    @RequestPart("file") MultipartFile file) {
        Optional<Activation> opt = activationService.getById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Activation activation = opt.get();
        Users user = activation.getUser();

        List<Map<String, String>> adifQsos;
        try {
            String content = new String(file.getBytes(), StandardCharsets.UTF_8);
            adifQsos = parseAdif(content);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to read ADIF: " + e.getMessage()));
        }

        Instant start = Instant.parse(activation.getStartTime());
        Instant now = Instant.now();

        List<Contact> contacts = new ArrayList<>();
        for (Map<String, String> rec : adifQsos) {
            Instant qsoInstant = parseAdifDateTimeToInstant(rec.get("qso_date"), rec.get("time_on"));
            if (qsoInstant == null || qsoInstant.isBefore(start) || qsoInstant.isAfter(now)) {
                continue;
            }
            Contact c = new Contact();
            c.setActivation(activation);
            c.setUser(user);
            c.setCallsignWorked(rec.getOrDefault("call", null));
            c.setFrequency(rec.getOrDefault("freq", null));
            c.setMode(rec.getOrDefault("mode", null));
            c.setSignalReportSent(rec.getOrDefault("rst_sent", null));
            c.setSignalReportRecv(rec.getOrDefault("rst_rcvd", null));
            c.setQsoTime(qsoInstant.toString());
            contacts.add(c);
        }

        if (!contacts.isEmpty()) {
            contactService.saveAll(contacts);
        }

        activation = activationService.endActivation(activation.getId());

        Map<String, Object> resp = new LinkedHashMap<>();
        resp.put("imported", contacts.size());
        resp.put("activationId", activation.getId());
        resp.put("endedAt", activation.getEndTime());
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/adif", consumes = { "multipart/form-data" })
    public ResponseEntity<Activation> createFromAdif(@RequestParam(name = "site_id") int siteId,
                                                     @RequestPart("file") MultipartFile file,
                                                     @RequestParam(required = false) String callsign) {
        try {
            if (callsign == null || callsign.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            String cs = callsign.trim();

            // Resolve user and site
            Users user = usersRepository.findByCallsign(cs)
                    .orElseThrow(() -> new IllegalArgumentException("Unknown callsign: " + cs));
            Site site = siteRepository.findById(siteId)
                    .orElseThrow(() -> new IllegalArgumentException("Unknown site id: " + siteId));

            // Parse ADIF
            String content = new String(file.getBytes(), StandardCharsets.UTF_8);
            List<Map<String, String>> adifQsos = parseAdif(content);

            // Determine earliest and latest QSO time
            Instant earliest = null;
            Instant latest = null;
            for (Map<String, String> rec : adifQsos) {
                Instant t = parseAdifDateTimeToInstant(rec.get("qso_date"), rec.get("time_on"));
                if (t == null) continue;
                if (earliest == null || t.isBefore(earliest)) earliest = t;
                if (latest == null || t.isAfter(latest)) latest = t;
            }
            if (earliest == null || latest == null) {
                return ResponseEntity.badRequest().build();
            }

            // Create and save Activation using the derived window
            Activation activation = new Activation();
            activation.setUser(user);
            activation.setSite(site);
            activation.setStartTime(earliest.toString());
            activation.setEndTime(latest.toString());
            activation.setLogStatus("imported");
            activation.setSubmittedAt(Instant.now().toString());
            Activation saved = activationService.add(activation);

            // Map all valid QSOs to contacts for this activation
            List<Contact> contacts = new ArrayList<>();
            for (Map<String, String> rec : adifQsos) {
                Instant qsoInstant = parseAdifDateTimeToInstant(rec.get("qso_date"), rec.get("time_on"));
                if (qsoInstant == null) continue;
                Contact c = new Contact();
                c.setActivation(saved);
                c.setUser(user);
                c.setCallsignWorked(rec.getOrDefault("call", null));
                c.setFrequency(rec.getOrDefault("freq", null));
                c.setMode(rec.getOrDefault("mode", null));
                c.setSignalReportSent(rec.getOrDefault("rst_sent", null));
                c.setSignalReportRecv(rec.getOrDefault("rst_rcvd", null));
                c.setQsoTime(qsoInstant.toString());
                contacts.add(c);
                System.out.println("Contact: " + c + ".");
            }
            if (!contacts.isEmpty()) {
                contactService.saveAll(contacts);
            }

            return ResponseEntity.created(URI.create("/api/activations/" + saved.getId())).body(saved);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // --- Minimal ADIF parser ---
    private static List<Map<String, String>> parseAdif(String content) {
        String lower = content.replace("\r", "");
        String[] records = lower.split("<eor>");
        List<Map<String, String>> result = new ArrayList<>();
        for (String rec : records) {
            Map<String, String> map = new HashMap<>();
            int i = 0;
            while (i < rec.length()) {
                int lt = rec.indexOf('<', i);
                if (lt == -1) break;
                int gt = rec.indexOf('>', lt);
                if (gt == -1) break;
                String tag = rec.substring(lt + 1, gt).trim();
                // tag like CALL:5 or QSO_DATE:8
                String name = tag;
                int colon = tag.indexOf(':');
                if (colon > 0) name = tag.substring(0, colon);
                int nextLt = rec.indexOf('<', gt + 1);
                String value = nextLt == -1 ? rec.substring(gt + 1) : rec.substring(gt + 1, nextLt);
                value = value.strip();
                map.put(name.toLowerCase(Locale.ROOT), value);
                i = nextLt == -1 ? rec.length() : nextLt;
            }
            // Keep only if has essential fields
            if (!map.isEmpty()) result.add(map);
        }
        return result;
    }

    private static Instant parseAdifDateTimeToInstant(String yyyymmdd, String hhmmOrHhmmss) {
        try {
            if (yyyymmdd == null) return null;
            String date = yyyymmdd.trim();
            LocalDate ld = LocalDate.of(
                    Integer.parseInt(date.substring(0, 4)),
                    Integer.parseInt(date.substring(4, 6)),
                    Integer.parseInt(date.substring(6, 8)));
            String t = (hhmmOrHhmmss == null ? "000000" : hhmmOrHhmmss.trim());
            if (t.length() == 4) t = t + "00"; // HHmm -> HHmmss
            LocalTime lt = LocalTime.of(
                    Integer.parseInt(t.substring(0, 2)),
                    Integer.parseInt(t.substring(2, 4)),
                    Integer.parseInt(t.substring(4, 6)));
            return lt.atDate(ld).toInstant(ZoneOffset.UTC);
        } catch (Exception e) {
            return null;
        }
    }
}
