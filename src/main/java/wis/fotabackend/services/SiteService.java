package wis.fotabackend.services;

import wis.fotabackend.domains.Site;

import java.util.List;

public interface SiteService {
    List<Site> getAll();

    List<Site> getAllByCatagory(String catagory);
}
