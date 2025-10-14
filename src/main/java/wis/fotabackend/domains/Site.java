package wis.fotabackend.domains;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "site")
public class Site {
    @Id
    @Column(name = "site_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Getter    @Setter
    @Column(name = "category")
    private String category;

    @Getter    @Setter
    @Column(name = "location")
    private String location;

    @Getter    @Setter
    @Column(name="qth")
    private String qth;

    @Getter @Setter
    @Column(name = "active", columnDefinition = "varchar(5)", length = 5)
    @Convert(converter = BooleanToStringConverter.class)
    private Boolean active;

    @Getter    @Setter
    @Column(name = "created_by")
    private String createdBy;

    @Getter    @Setter
    @Column(name = "longitude")
    private double longitude;

    @Getter    @Setter
    @Column(name = "latitude")
    private double latitude;
}
