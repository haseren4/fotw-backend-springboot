package wis.fotabackend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wis.fotabackend.domains.Contact;
import wis.fotabackend.dto.ContactDTO;
import wis.fotabackend.services.ContactService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/contacts")
public class ContactsController {
    private final ContactService contactService;

    public ContactsController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping
    public ResponseEntity<List<ContactDTO>> list(
            @RequestParam(required = false, name = "activationId") Integer activationId,
            @RequestParam(required = false, name = "activation_id") Integer activationIdAlt
    ) {
        Integer aid = (activationId != null) ? activationId : activationIdAlt;
        List<Contact> contacts = (aid != null)
                ? contactService.getByActivationId(aid)
                : contactService.getAll();
        List<ContactDTO> dto = contacts.stream().map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(dto);
    }

    private ContactDTO toDto(Contact c) {
        ContactDTO dto = new ContactDTO();
        dto.setId(c.getId());
        dto.setActivationId(c.getActivation() != null ? c.getActivation().getId() : null);
        dto.setTime(c.getQsoTime());
        dto.setCallsign(c.getCallsignWorked());
        dto.setBand(c.getFrequency());
        dto.setMode(c.getMode());
        dto.setRstSent(c.getSignalReportSent());
        dto.setRstRcvd(c.getSignalReportRecv());
        dto.setNotes(null); // not present in domain; reserved for future use
        return dto;
    }
}
