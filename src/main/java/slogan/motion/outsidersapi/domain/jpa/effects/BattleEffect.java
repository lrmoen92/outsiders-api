package slogan.motion.outsidersapi.domain.jpa.effects;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import slogan.motion.outsidersapi.domain.jpa.Effect;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BattleEffect extends Effect {

    // used to identify an effect within the context of a battle
    // set during battle logic (random number i guess, just not negative)
    private int instanceId;
    // set during battle logic for groups of effects (random number i guess, just
    // not negative)
    private int groupId;
    // only for effects on character instances (should be position based)
    private int originCharacter;
    private int targetCharacter;


    public String getShorthand() {
        return "BattleEffect{" +
                "instanceId=" + instanceId +
                ", groupId=" + groupId +
                ", originCharacter=" + originCharacter +
                ", targetCharacter=" + targetCharacter +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", duration=" + duration +
                ", interruptable=" + interruptable +
                ", physical=" + physical +
                ", magical=" + magical +
                ", affliction=" + affliction +
                ", conditional=" + conditional +
                ", visible=" + visible +
                ", stacks=" + stacks +
                ", condition='" + condition + '\'' +
                ", quality='" + quality + '\'' +
                ", statMods=" + statMods +
                '}';
    }

    public BattleEffect(Effect e) {
        super(e);
    }

    public BattleEffect(boolean physical, boolean magical, boolean affliction, boolean interruptable,
                        boolean conditional) {
        super(physical, magical, affliction, interruptable, conditional);
    }
}
