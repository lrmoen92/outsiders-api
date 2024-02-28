package slogan.motion.outsidersapi.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import slogan.motion.outsidersapi.domain.jpa.JsonObject;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MatchMakingDTO extends JsonObject {
    private String queue;
    private int char1;
    private int char2;
    private int char3;
}
