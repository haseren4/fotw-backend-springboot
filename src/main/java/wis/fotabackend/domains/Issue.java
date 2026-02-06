package wis.fotabackend.domains;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "issue")
public class Issue {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private int id;

    @Column(name = "reporter")
    private String reporter;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "report_date")
    private LocalDateTime reportDate;
}
