package slogan.motion.outsidersapi.domain.jpa.effects;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import slogan.motion.outsidersapi.domain.jpa.Effect;

@Entity(name = "allyEffect")
@Getter
@Setter
@NoArgsConstructor
public class AllyEffect extends BattleEffect {

    public AllyEffect(Effect e) {
        super(e);
    }

    public AllyEffect(boolean physical, boolean magical, boolean affliction, boolean interruptable,
                      boolean conditional) {
        super(physical, magical, affliction, interruptable, conditional);
    }
}
