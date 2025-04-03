package slogan.motion.outsidersapi.domain.jpa.effects;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import slogan.motion.outsidersapi.domain.jpa.Effect;

@Entity(name = "enemyEffect")
@Getter
@Setter
@NoArgsConstructor
public class EnemyEffect extends BattleEffect {

    public EnemyEffect(Effect e) {
        super(e);
    }

    public EnemyEffect(boolean physical, boolean magical, boolean affliction, boolean interruptable,
                       boolean conditional) {
        super(physical, magical, affliction, interruptable, conditional);
    }
}
