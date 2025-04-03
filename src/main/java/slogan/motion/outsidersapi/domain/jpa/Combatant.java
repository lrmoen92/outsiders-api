package slogan.motion.outsidersapi.domain.jpa;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import slogan.motion.outsidersapi.domain.jpa.effects.BattleEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
public class Combatant extends Character {

    private int characterId;

    private int hp = 100;
    private boolean dead = false;
    // player 1 (0, 1, 2) player 2 (3, 4, 5)
    private int position = -1;


    @ElementCollection(fetch = FetchType.LAZY)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Integer> cooldowns = new ArrayList<>(4);

    @ElementCollection(fetch = FetchType.LAZY)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<String> flags = new ArrayList<>();

    @ManyToOne
    @JsonIgnore
    private Player Player;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "Combatant", orphanRemoval = true)
    private List<BattleEffect> Effects = new ArrayList<>();

    public void addIEffect(BattleEffect b) {
        Effects.add(b);
        b.setCombatant(this);
    }

    public void removeIEffect(BattleEffect b) {
        b.setCombatant(null);
        Effects.remove(b);
    }

    public void clearEffects() {
        Effects.forEach(this::removeIEffect);
    }

    public String getShorgthand() {
        return "Combatant{" +
                ", name=" + name +
                ", hp=" + hp +
                ", dead=" + dead +
                ", position=" + position +
                ", cooldowns=" + cooldowns +
                ", flags=" + flags +
                ", Effects=" + Effects.stream().map(BattleEffect::getShorthand).collect(Collectors.joining(", ")) +
                '}';
    }

    public void setHp(int hp) {
        if (!this.isDead()) {
            if (hp > 100) {
                this.hp = 100;
            } else if (hp <= 0) {
                this.hp = 0;
                this.setDead(true);
            } else {
                this.hp = hp;
            }
        }

    }

    public Combatant() {

    }

    public Combatant(Character input, int position) {
        super(input);

        this.characterId = input.getId();
        this.position = position;
        this.cooldowns.add(0);
        this.cooldowns.add(0);
        this.cooldowns.add(0);
        this.cooldowns.add(0);
    }
}
