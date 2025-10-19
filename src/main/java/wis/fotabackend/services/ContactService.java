package wis.fotabackend.services;

import wis.fotabackend.domains.Contact;

import java.util.List;

public interface ContactService {
    List<Contact> saveAll(List<Contact> contacts);
}
