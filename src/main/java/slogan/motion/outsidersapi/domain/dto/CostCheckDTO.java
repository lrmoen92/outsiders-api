package slogan.motion.outsidersapi.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import slogan.motion.outsidersapi.domain.jpa.JsonObject;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CostCheckDTO extends JsonObject {
	
	private List<TargetCheckDTO> chosenAbilities;
	private List<List<String>> allyCosts;
	private List<String> spentEnergy;

}
