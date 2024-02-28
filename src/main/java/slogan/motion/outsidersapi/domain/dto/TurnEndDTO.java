package slogan.motion.outsidersapi.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import slogan.motion.outsidersapi.domain.jpa.Effect;
import slogan.motion.outsidersapi.domain.jpa.JsonObject;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TurnEndDTO extends JsonObject
{
	// list of spent energy
	Map<String, Integer> spentEnergy;

	// list of all effects by id?
	List<Effect> effects;

	// list of abilityID -> list of targetIDs (all positional ie:2nd char ability 3 is abilityID 6)
	List<TargetCheckDTO> abilities;

}
