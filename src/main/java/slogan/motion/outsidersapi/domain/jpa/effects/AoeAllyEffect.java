package slogan.motion.outsidersapi.domain.jpa.effects;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import slogan.motion.outsidersapi.domain.jpa.Effect;

@Entity(name = "aoeAllyEffect")
@Getter
@Setter
@NoArgsConstructor
public class AoeAllyEffect extends BattleEffect {

    public AoeAllyEffect(Effect e) {
        super(e);
    }

    public AoeAllyEffect(boolean physical, boolean magical, boolean affliction, boolean interruptable,
                         boolean conditional) {
        super(physical, magical, affliction, interruptable, conditional);
    }
}
