package slogan.motion.outsidersapi.domain.jpa.effects;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import slogan.motion.outsidersapi.domain.jpa.Effect;

@Entity(name = "selfEffect")
@Getter
@Setter
@NoArgsConstructor
public class SelfEffect extends BattleEffect {

    public SelfEffect(Effect e) {
        super(e);
    }

    public SelfEffect(boolean physical, boolean magical, boolean affliction, boolean interruptable,
                      boolean conditional) {
        super(physical, magical, affliction, interruptable, conditional);
    }
}
