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
public class ChatDTO extends JsonObject {

    public String timestamp;
    public String name;
    public String message;
    public String avatarUrl;
}
