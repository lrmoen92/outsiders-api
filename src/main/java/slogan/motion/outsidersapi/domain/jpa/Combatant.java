package slogan.motion.outsidersapi.domain.jpa;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
    private List<Effect> Effects = new ArrayList<>();

    public void addIEffect(Effect b) {
        Effects.add(b);
        b.setCombatant(this);
    }

    public void removeIEffect(Effect b) {
        b.setCombatant(null);
        Effects.remove(b);
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
