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
public class WebSocketDTO<T> extends JsonObject {

    private int arenaId;
    private int playerId;
    private String type;
    private T dto;
}
