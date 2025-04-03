package slogan.motion.outsidersapi.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import slogan.motion.outsidersapi.domain.jpa.PlayerCredentials;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerMessage {
    private String name;
    private PlayerCredentials credentials;
    private String avatar;

}
