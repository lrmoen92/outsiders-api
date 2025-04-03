package slogan.motion.outsidersapi.domain.jpa.effects;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import slogan.motion.outsidersapi.domain.jpa.Effect;

@Entity(name = "aoeEnemyEffect")
@Getter
@Setter
@NoArgsConstructor
public class AoeEnemyEffect extends BattleEffect {

    public AoeEnemyEffect(Effect e) {
        super(e);
    }

    public AoeEnemyEffect(boolean physical, boolean magical, boolean affliction, boolean interruptable,
                          boolean conditional) {
        super(physical, magical, affliction, interruptable, conditional);
    }
}
