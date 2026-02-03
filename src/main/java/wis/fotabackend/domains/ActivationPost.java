package wis.fotabackend.domains;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "activation_post")
public class ActivationPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Users user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "activation_id", referencedColumnName = "id")
    private Activation activation;

    @Column(name = "post_time", length = 255)
    private String postTime;

    @Column(name = "title", length = 255)
    private String title;

    @Column(name = "body", columnDefinition = "varchar(255)", length = 255)
    private String body;
}
