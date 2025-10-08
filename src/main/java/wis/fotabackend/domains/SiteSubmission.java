package wis.fotabackend.domains;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Entity
@Table(name = "site_submission")
public class SiteSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    @Getter @Setter
    @Column(name="site_name")
    public String siteName;

    @Getter @Setter
    @Column(name="category")
    public String category;

    @Getter @Setter
    @Column(name="location")
    public String location;

    @Getter @Setter
    @Column(name="latitude")
    public double latitude;

    @Getter @Setter
    @Column(name="longitude")
    public double longitude;


    @Getter @Setter
    @Column(name="proposed_by")
    public String proposedBy;


}
