package wis.fotabackend.domains;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "contact")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    @ManyToOne(optional = false)
    @JoinColumn(name = "activation_id", referencedColumnName = "id")
    @Getter @Setter
    private Activation activation;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @Getter @Setter
    private Users user;

    @Getter @Setter
    @Column(name = "callsign_worked", length = 255)
    private String callsignWorked;

    @Getter @Setter
    @Column(name = "frequency", length = 255)
    private String frequency;

    @Getter @Setter
    @Column(name = "mode", length = 255)
    private String mode;

    @Getter @Setter
    @Column(name = "qso_time", length = 255)
    private String qsoTime;

    @Getter @Setter
    @Column(name = "signal_report_sent", length = 255)
    private String signalReportSent;

    @Getter @Setter
    @Column(name = "signal_report_recv", length = 255)
    private String signalReportRecv;
}
