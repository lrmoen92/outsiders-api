package slogan.motion.outsidersapi.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import slogan.motion.outsidersapi.domain.jpa.JsonObject;
import slogan.motion.outsidersapi.domain.jpa.effects.BattleEffect;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TurnEndDTO extends JsonObject {
    // list of spent energy
    Map<String, Integer> spentEnergy;

    // list of all effects by id?
    List<BattleEffect> effects;

    // list of abilityID -> list of targetIDs (all positional ie:2nd char ability 3 is abilityID 6)
    List<TargetCheckDTO> abilities;

}
