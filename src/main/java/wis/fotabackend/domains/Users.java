package wis.fotabackend.domains;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;            // Spring Boot 3.x uses jakarta.*
import lombok.*;                         // if you’re using Lombok

import java.util.ArrayList;
import java.util.List;
@Entity
@NoArgsConstructor                               // JPA required
@AllArgsConstructor                              // enables new User(...all fields...)
@Builder
@Table(name = "\"user\"")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    
    @Getter@Setter
    @Column(name = "callsign")
    String callsign;
    
    @Getter@Setter @Nullable
    @Column(name = "email")
    String email;
    
    @Getter@Setter @Nullable
    @Column(name = "password_hash")
    String password_hash;
    
    @Getter@Setter @Nullable
    @Column(name = "created_at")
    String created_at;


}
