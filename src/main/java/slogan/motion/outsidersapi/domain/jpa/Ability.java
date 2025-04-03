package slogan.motion.outsidersapi.domain.jpa;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import slogan.motion.outsidersapi.domain.constant.Energy;
import slogan.motion.outsidersapi.domain.constant.Quality;
import slogan.motion.outsidersapi.domain.constant.Stat;
import slogan.motion.outsidersapi.domain.jpa.effects.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
//@Immutable
public class Ability extends JsonObject {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private int cooldown = 0;
    private String name;
    private String abilityUrl;
    private String description;
    private int position;
    private String targets;
    private String types;
    private boolean aoe = false;
    private boolean self = false;
    private boolean ally = false;
    private boolean enemy = false;

    @ElementCollection(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<String> cost = new ArrayList<>();


    // TODO: all effects should be in one map by type if possible
    // ir implement child classes? List<SelfEffect> etc...
    // or a list with filtering on a field...
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "Ability")
    private List<SelfEffect> selfEffects = new ArrayList<>();

    public Ability() {

    }

    public void addSelfEffect(SelfEffect b) {
        selfEffects.add(b);
        b.setAbility(this);
    }

    public void removeSelfEffect(SelfEffect b) {
        b.setAbility(null);
        selfEffects.remove(b);
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "Ability")
    private List<EnemyEffect> enemyEffects = new ArrayList<>();

    public void addEnemyEffect(EnemyEffect b) {
        enemyEffects.add(b);
        b.setAbility(this);
    }

    public void removeEnemyEffect(EnemyEffect b) {
        b.setAbility(null);
        enemyEffects.remove(b);
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "Ability")
    private List<AoeEnemyEffect> aoeEnemyEffects = new ArrayList<>();

    public void addAoeEnemyEffect(AoeEnemyEffect b) {
        aoeEnemyEffects.add(b);
        b.setAbility(this);
    }

    public void removeAoeEnemyEffect(AoeEnemyEffect b) {
        b.setAbility(null);
        aoeEnemyEffects.remove(b);
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "Ability")
    private List<AllyEffect> allyEffects = new ArrayList<>();

    public void addAllyEffect(AllyEffect b) {
        allyEffects.add(b);
        b.setAbility(this);
    }

    public void removeAllyEffect(AllyEffect b) {
        b.setAbility(null);
        allyEffects.remove(b);
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "Ability")
    private List<AoeAllyEffect> aoeAllyEffects = new ArrayList<>();

    public void addAoeAllyEffect(AoeAllyEffect b) {
        aoeAllyEffects.add(b);
        b.setAbility(this);
    }

    public void removeAoeAllyEffect(AoeAllyEffect b) {
        b.setAbility(null);
        aoeAllyEffects.remove(b);
    }

    @ManyToMany(mappedBy = "abilities")
    @JsonIgnore
    private List<Character> characters = new ArrayList<>();

    public Ability(boolean enemies, boolean allies, boolean self, boolean aoe) {
        this.enemy = enemies;
        this.ally = allies;
        this.self = self;
        this.aoe = aoe;
    }

    public String getCleanCost() {
        StringBuilder sb = new StringBuilder();
        int div = 0;
        int arc = 0;
        int dex = 0;
        int str = 0;
        int ran = 0;
        for (String cost : this.cost) {
            if (cost.equals(Energy.DIVINITY)) {
                div++;
            } else if (cost.equals(Energy.ARCANA)) {
                arc++;
            } else if (cost.equals(Energy.DEXTERITY)) {
                dex++;
            } else if (cost.equals(Energy.STRENGTH)) {
                str++;
            } else if (cost.equals(Energy.RANDOM)) {
                ran++;
            }
        }

        if (div > 0) {
            sb.append(div);
            sb.append(" Divinity");
        }
        if (arc > 0) {
            sb.append(arc);
            sb.append(" Arcana");
        }
        if (dex > 0) {
            sb.append(dex);
            sb.append(" Dexterity");
        }
        if (str > 0) {
            sb.append(str);
            sb.append(" Strength");
        }
        if (ran > 0) {
            sb.append(ran);
            sb.append(" Random");
        }
        return sb.toString();
    }

    public String getShorthand() {
        return "CD: " + this.cooldown + " - " + this.name + " - " + this.getCleanCost() + ": " + this.description;
    }

    public void setTargets() {
        List<String> list = new ArrayList<>();
        if (this.isSelf()) {
            list.add("Self");
        }

        if (this.isAoe()) {
            if (this.isAlly()) {
                list.add("Allies");
            }
            if (this.isEnemy()) {
                list.add("Enemies");
            }
        } else {
            if (this.isAlly()) {
                list.add("Ally");
            } else if (this.isEnemy()) {
                list.add("Enemy");
            }
        }
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {
            String s = list.get(i);
            sb.append(s);
            if (i != list.size() - 1) {
                sb.append(" and ");
            }
        }

        this.targets = sb.toString();
    }

    public void setTypes() {
        List<String> list = new ArrayList<>();
        if (this.isPhysical()) {
            list.add("Physical");
        }
        if (this.isMagical()) {
            list.add("Magical");
        }
        if (this.isAffliction()) {
            list.add("Affliction");
        }
        if (this.isDamaging()) {
            list.add("Damaging");
        }
        if (this.isDebuff()) {
            list.add("Debuff");
        }
        if (this.isBuff()) {
            list.add("Buff");
        }
        if (this.isInterruptable()) {
            list.add("Interruptable");
        }
        if (!this.isVisible()) {
            list.add("Hidden");
        }
        if (this.isStacks()) {
            list.add("Stacks");
        }
        // TODO LATER: Interruptable, Conditional, Visible, Stacks

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {
            String s = list.get(i);
            sb.append(s);
            if (i != list.size() - 1) {
                sb.append(", ");
            }
        }

        this.types = sb.toString();
    }

    @JsonIgnore
    public Map<String, String> getRundown() {
        Map<String, String> map = new HashMap<>();

        map.put("TARGETS", this.getTargets());
        map.put("TYPES", this.getTypes());

        return map;
    }

    @JsonIgnore
    public boolean isInterruptable() {
        for (Effect e : this.selfEffects) {
            if (e.isInterruptable()) {
                return true;
            }
        }
        for (Effect e : this.enemyEffects) {
            if (e.isInterruptable()) {
                return true;
            }
        }
        for (Effect e : this.aoeEnemyEffects) {
            if (e.isInterruptable()) {
                return true;
            }
        }
        for (Effect e : this.allyEffects) {
            if (e.isInterruptable()) {
                return true;
            }
        }
        for (Effect e : this.aoeAllyEffects) {
            if (e.isInterruptable()) {
                return true;
            }
        }
        return false;
    }

    @JsonIgnore
    public boolean isVisible() {
        for (Effect e : this.selfEffects) {
            if (!e.isVisible()) {
                return false;
            }
        }
        for (Effect e : this.enemyEffects) {
            if (!e.isVisible()) {
                return false;
            }
        }
        for (Effect e : this.aoeEnemyEffects) {
            if (!e.isVisible()) {
                return false;
            }
        }
        for (Effect e : this.allyEffects) {
            if (!e.isVisible()) {
                return false;
            }
        }
        for (Effect e : this.aoeAllyEffects) {
            if (!e.isVisible()) {
                return false;
            }
        }
        return true;
    }

    @JsonIgnore
    public boolean isStacks() {
        for (Effect e : this.selfEffects) {
            if (e.isStacks()) {
                return true;
            }
        }
        for (Effect e : this.enemyEffects) {
            if (e.isStacks()) {
                return true;
            }
        }
        for (Effect e : this.aoeEnemyEffects) {
            if (e.isStacks()) {
                return true;
            }
        }
        for (Effect e : this.allyEffects) {
            if (e.isStacks()) {
                return true;
            }
        }
        for (Effect e : this.aoeAllyEffects) {
            if (e.isStacks()) {
                return true;
            }
        }
        return false;
    }

    @JsonIgnore
    public boolean isPhysical() {
        for (Effect e : this.selfEffects) {
            if (e.isPhysical()) {
                return true;
            }
        }
        for (Effect e : this.enemyEffects) {
            if (e.isPhysical()) {
                return true;
            }
        }
        for (Effect e : this.aoeEnemyEffects) {
            if (e.isPhysical()) {
                return true;
            }
        }
        for (Effect e : this.allyEffects) {
            if (e.isPhysical()) {
                return true;
            }
        }
        for (Effect e : this.aoeAllyEffects) {
            if (e.isPhysical()) {
                return true;
            }
        }
        return false;
    }

    @JsonIgnore
    public boolean isMagical() {
        for (Effect e : this.selfEffects) {
            if (e.isMagical()) {
                return true;
            }
        }
        for (Effect e : this.enemyEffects) {
            if (e.isMagical()) {
                return true;
            }
        }
        for (Effect e : this.aoeEnemyEffects) {
            if (e.isMagical()) {
                return true;
            }
        }
        for (Effect e : this.allyEffects) {
            if (e.isMagical()) {
                return true;
            }
        }
        for (Effect e : this.aoeAllyEffects) {
            if (e.isMagical()) {
                return true;
            }
        }
        return false;
    }

    @JsonIgnore
    public boolean isAffliction() {
        for (Effect e : this.selfEffects) {
            if (e.isAffliction()) {
                return true;
            }
        }
        for (Effect e : this.enemyEffects) {
            if (e.isAffliction()) {
                return true;
            }
        }
        for (Effect e : this.aoeEnemyEffects) {
            if (e.isAffliction()) {
                return true;
            }
        }
        for (Effect e : this.allyEffects) {
            if (e.isAffliction()) {
                return true;
            }
        }
        for (Effect e : this.aoeAllyEffects) {
            if (e.isAffliction()) {
                return true;
            }
        }
        return false;
    }

    @JsonIgnore
    public boolean isDamaging() {
        for (Effect e : this.selfEffects) {
            if (e.getStatMods().get(Stat.DAMAGE) != null) {
                if (e.getStatMods().get(Stat.DAMAGE) > 0) {
                    return true;
                }
            }
        }
        for (Effect e : this.enemyEffects) {
            if (e.getStatMods().get(Stat.DAMAGE) != null) {
                if (e.getStatMods().get(Stat.DAMAGE) > 0) {
                    return true;
                }
            }
        }
        for (Effect e : this.aoeEnemyEffects) {
            if (e.getStatMods().get(Stat.DAMAGE) != null) {
                if (e.getStatMods().get(Stat.DAMAGE) > 0) {
                    return true;
                }
            }
        }
        for (Effect e : this.allyEffects) {
            if (e.getStatMods().get(Stat.DAMAGE) != null) {
                if (e.getStatMods().get(Stat.DAMAGE) > 0) {
                    return true;
                }
            }
        }
        for (Effect e : this.aoeAllyEffects) {
            if (e.getStatMods().get(Stat.DAMAGE) != null) {
                if (e.getStatMods().get(Stat.DAMAGE) > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    @JsonIgnore
    public boolean isBuff() {
        for (Effect e : this.selfEffects) {
            if (e.getStatMods() != null) {
                for (String buff : Stat.BUFFS) {
                    if (e.getStatMods().get(buff) != null) {
                        if (e.getStatMods().get(buff) > 0) {
                            return true;
                        }
                    }
                }
                for (String buff : Stat.DEBUFFS) {
                    if (e.getStatMods().get(buff) != null) {
                        if (e.getStatMods().get(buff) < 0) {
                            return true;
                        }
                    }
                }
            }
            for (String buff : Quality.BUFFS) {
                if (buff.equals(e.getQuality())) {
                    return true;
                }
            }
        }
        for (Effect e : this.allyEffects) {
            if (e.getStatMods() != null) {
                for (String buff : Stat.BUFFS) {
                    if (e.getStatMods().get(buff) != null) {
                        if (e.getStatMods().get(buff) > 0) {
                            return true;
                        }
                    }
                }
                for (String buff : Stat.DEBUFFS) {
                    if (e.getStatMods().get(buff) != null) {
                        if (e.getStatMods().get(buff) < 0) {
                            return true;
                        }
                    }
                }
            }
            for (String buff : Quality.BUFFS) {
                if (buff.equals(e.getQuality())) {
                    return true;
                }
            }
        }
        for (Effect e : this.aoeAllyEffects) {
            if (e.getStatMods() != null) {
                for (String buff : Stat.BUFFS) {
                    if (e.getStatMods().get(buff) != null) {
                        if (e.getStatMods().get(buff) > 0) {
                            return true;
                        }
                    }
                }
                for (String buff : Stat.DEBUFFS) {
                    if (e.getStatMods().get(buff) != null) {
                        if (e.getStatMods().get(buff) < 0) {
                            return true;
                        }
                    }
                }
            }
            for (String buff : Quality.BUFFS) {
                if (buff.equals(e.getQuality())) {
                    return true;
                }
            }
        }
        return false;
    }

    @JsonIgnore
    public boolean isDebuff() {
        for (Effect e : this.selfEffects) {
            if (e.getStatMods() != null) {
                for (String debuff : Stat.BUFFS) {
                    if (e.getStatMods().get(debuff) != null) {
                        if (e.getStatMods().get(debuff) < 0) {
                            return true;
                        }
                    }
                }
                for (String debuff : Stat.DEBUFFS) {
                    if (e.getStatMods().get(debuff) != null) {
                        if (e.getStatMods().get(debuff) > 0) {
                            return true;
                        }
                    }
                }
            }
            for (String debuff : Quality.DEBUFFS) {
                if (debuff.equals(e.getQuality())) {
                    return true;
                }
            }
        }
        for (Effect e : this.enemyEffects) {
            if (e.getStatMods() != null) {
                for (String debuff : Stat.BUFFS) {
                    if (e.getStatMods().get(debuff) != null) {
                        if (e.getStatMods().get(debuff) < 0) {
                            return true;
                        }
                    }
                }
                for (String debuff : Stat.DEBUFFS) {
                    if (e.getStatMods().get(debuff) != null) {
                        if (e.getStatMods().get(debuff) > 0) {
                            return true;
                        }
                    }
                }
            }
            for (String debuff : Quality.DEBUFFS) {
                if (debuff.equals(e.getQuality())) {
                    return true;
                }
            }
        }
        for (Effect e : this.aoeEnemyEffects) {
            if (e.getStatMods() != null) {
                for (String debuff : Stat.BUFFS) {
                    if (e.getStatMods().get(debuff) != null) {
                        if (e.getStatMods().get(debuff) < 0) {
                            return true;
                        }
                    }
                }
                for (String debuff : Stat.DEBUFFS) {
                    if (e.getStatMods().get(debuff) != null) {
                        if (e.getStatMods().get(debuff) > 0) {
                            return true;
                        }
                    }
                }
            }
            for (String debuff : Quality.DEBUFFS) {
                if (debuff.equals(e.getQuality())) {
                    return true;
                }
            }
        }
        return false;
    }
}
