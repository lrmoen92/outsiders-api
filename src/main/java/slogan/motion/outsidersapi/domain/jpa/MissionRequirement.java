package slogan.motion.outsidersapi.domain.jpa;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MissionRequirement extends JsonObject {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private int amount;

    private String userFaction = "ANYONE";
    private String targetFaction = "ANYONE";

    @ManyToOne
    @JsonIgnore
    private Mission Mission;

    @ManyToOne
    @JsonIgnore
    private MissionProgress MissionProgress;

    public void makeSafe() {
        this.setMission(null);
        this.setMissionProgress(null);
    }

    public MissionRequirement(int amount, MissionRequirement parent) {
        this.amount = amount;
        this.userFaction = parent.getUserFaction();
        this.targetFaction = parent.getTargetFaction();
    }

    @JsonGetter
    public String getDescription() {
        StringBuilder result = new StringBuilder();
        List<java.lang.Character> vowels = Arrays.asList('A', 'E', 'I', 'O', 'U');

        result.append("Win " + amount + " game");
        if (amount != 1) {
            result.append("s");
        }

        if (userFaction != null) {
            char one = userFaction.toCharArray()[0];
            String an = "a ";
            if (vowels.contains(one)) {
                an = "an ";
            }

            result.append(" with " + an + userFaction);
        }

        if (targetFaction != null) {
            char one = targetFaction.toCharArray()[0];
            String an = "a ";
            if (vowels.contains(one)) {
                an = "an ";
            }

            result.append(" vs " + an + targetFaction);
        }

        return result.toString();
    }
}
