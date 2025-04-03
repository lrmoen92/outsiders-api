package slogan.motion.outsidersapi.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import slogan.motion.outsidersapi.domain.jpa.Ability;
import slogan.motion.outsidersapi.domain.jpa.JsonObject;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TargetCheckDTO extends JsonObject {

    // all positional
    Ability ability;
    Integer characterPosition;
    List<Integer> targetPositions;
}
