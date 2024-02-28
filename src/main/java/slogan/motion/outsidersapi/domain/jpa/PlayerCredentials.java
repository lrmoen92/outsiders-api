package slogan.motion.outsidersapi.domain.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class PlayerCredentials extends JsonObject implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Serial
    private static final long serialVersionUID = 8895585864692900053L;

    private String email;
    private String password;
}
