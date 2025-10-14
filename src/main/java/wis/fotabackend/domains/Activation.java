package wis.fotabackend.domains;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "activation")
public class Activation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    // Relationships
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @Getter @Setter
    private Users user;

    // activation.site_id references site.site_code which is the @Id of Site
    @ManyToOne(optional = false)
    @JoinColumn(name = "site_id", referencedColumnName = "site_code")
    @Getter @Setter
    private Site site;

    @Getter @Setter
    @Column(name = "start_time", length = 255)
    private String startTime;

    @Getter @Setter
    @Column(name = "end_time", length = 255)
    private String endTime;

    @Getter @Setter
    @Column(name = "log_status", length = 255)
    private String logStatus;

    @Getter @Setter
    @Column(name = "submitted_at", length = 255)
    private String submittedAt;
}
