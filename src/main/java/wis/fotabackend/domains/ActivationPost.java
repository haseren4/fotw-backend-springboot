package wis.fotabackend.domains;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "activation_post")
public class ActivationPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @Getter @Setter
    private Users user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "activation_id", referencedColumnName = "id")
    @Getter @Setter
    private Activation activation;

    @Getter @Setter
    @Column(name = "post_time", length = 255)
    private String postTime;

    @Getter @Setter
    @Column(name = "title", length = 255)
    private String title;

    @Getter @Setter
    @Column(name = "body", columnDefinition = "varchar(255)", length = 255)
    private String body;
}
