package slogan.motion.outsidersapi.domain.jpa;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.HashMap;
import java.util.Map;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Effect extends JsonObject {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    protected String name;
    protected String description;
    protected String avatarUrl = "https://i.imgur.com/CiUI6Sg.png";
    protected int duration = 1;
    protected boolean interruptable = false;
    protected boolean physical = false;
    protected boolean magical = false;
    protected boolean affliction = false;
    protected boolean conditional = false;
    protected boolean visible = true;
    protected boolean stacks = false;

    // used to identify an effect within the context of a battle
    // set during battle logic (random number i guess, just not negative)
    private int instanceId;
    // set during battle logic for groups of effects (random number i guess, just
    // not negative)
    private int groupId;
    // only for effects on character instances (should be position based)
    private int originCharacter;
    private int targetCharacter;

    // TODO: list of conditions, list of qualities....  yeah it's ultimately better can't deny it.
    // conditional string to meet
    protected String condition;
    // buff or debuff
    protected String quality;

    @ManyToOne
    @JsonIgnore
    private Ability Ability;

    @ManyToOne
    @JsonIgnore
    private Combatant Combatant;

    @MapKeyColumn(name = "stat")
    @Column(name = "mod")
    @ElementCollection(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    protected Map<String, Integer> statMods = new HashMap<>();

    public Effect(boolean physical, boolean magical, boolean affliction, boolean interruptable,
                  boolean conditional) {
        this.physical = physical;
        this.magical = magical;
        this.affliction = affliction;
        this.interruptable = interruptable;
        this.conditional = conditional;
    }

    public Effect(Effect e) {
        this.duration = e.duration;
        this.avatarUrl = e.avatarUrl;
        this.instanceId = e.instanceId;
        this.name = e.name;
        this.condition = e.condition;
        this.quality = e.quality;
        this.description = e.description;
        this.originCharacter = e.originCharacter;
        this.targetCharacter = e.targetCharacter;
        this.interruptable = e.interruptable;
        this.physical = e.physical;
        this.magical = e.magical;
        this.affliction = e.affliction;
        this.conditional = e.conditional;
        this.visible = e.visible;
        this.stacks = e.stacks;
        this.statMods = new HashMap<>();
        this.statMods.putAll(e.getStatMods());
    }
    public void triggerAndRevealCounter(Ability abilityCountered) {
        this.setVisible(true);
        this.setDuration(995);
        this.setDescription(abilityCountered.getName() + " has been countered.");
        this.setQuality(null);
        this.setCondition(null);
        this.setConditional(false);
        this.setStatMods(null);
        this.setStacks(false);
    }
}
