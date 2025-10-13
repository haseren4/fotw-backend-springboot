package wis.fotabackend.services;

import org.springframework.stereotype.Service;
import wis.fotabackend.domains.Site;
import wis.fotabackend.repositories.SiteRepository;

import java.util.List;

@Service
public class SiteServiceImpl implements SiteService{
    public SiteRepository siteRepository;
    public SiteServiceImpl(SiteRepository siteRepository){
        this.siteRepository = siteRepository;
    }

    @Override
    public List<Site> getAll() {
        return siteRepository.findAll();
    }
}
