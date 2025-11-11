package wis.fotabackend.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wis.fotabackend.domains.Site;
import wis.fotabackend.services.SiteService;
import wis.fotabackend.services.SiteServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/site")
public class SiteController {
    public SiteService siteService;
    public SiteController(SiteServiceImpl siteService){
        this.siteService = siteService;
    }

    @GetMapping(value="/all")
    public List<Site> getAll(){
        return siteService.getAll();
    }

    @GetMapping(value= "/catagory={catagory}")
    public List<Site> getAllByCatagory(@PathVariable String catagory){
        return siteService.getAllByCatagory(catagory);
    }
}
