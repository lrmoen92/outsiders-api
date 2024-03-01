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
import java.util.Map;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Player extends JsonObject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String avatarUrl;
    // rank
    private int level;

    // raw xp (lp)
    // 0/100  (+30, +20, +10) (-25, -15, -5)
    private int xp;

    @Column(unique = true)
    private String displayName;

    @OneToOne(cascade = CascadeType.ALL)
    private PlayerCredentials credentials;
    // mission id, current amount (as opposed to total amount needed)

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "Player", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<MissionProgress> missionProgress = new ArrayList<>();

    public void addIMissionProgress(MissionProgress g) {
        missionProgress.add(g);
        g.setPlayer(this);
    }

    public void removeIMissionProgress(MissionProgress g) {
        g.setPlayer(null);
        missionProgress.remove(g);
    }

    @ElementCollection(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private Set<Integer> missionIdsCompleted;

    @ElementCollection(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private Set<Integer> characterIdsUnlocked;

    @MapKeyColumn(name = "type")
    @Column(name = "amount")
    @ElementCollection(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private Map<String, Integer> playerEnergy;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "Player", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Combatant> Combatants = new ArrayList<>();

    // TODO: remove persist?
    @ManyToOne
    @JsonIgnore
    private Battle Battle;

    public void addICombatants(List<Combatant> a) {
        a.forEach(this::addICombatant);
    }

    public void addICombatant(Combatant a) {
        Combatants.add(a);
        a.setPlayer(this);
    }

    public void removeICombatant(Combatant a) {
        a.setPlayer(null);
        Combatants.remove(a);
    }


    @JsonIgnore
    private int loseXP(int i) {
        int x = this.getXp();

        int res = x - i;

        if (res < 0) {
            // demote
            res = 100 - Math.abs(res);
            int l = this.getLevel();
            if (l > 1) {
                this.setLevel(l - 1);
            } else {
                this.setXp(0);
            }
        } else {
            this.setXp(res);
        }

        return i;
    }

    @JsonIgnore
    private int gainXP(int i) {
        int x = this.getXp();

        int res = x + i;

        if (res > 99) {
            // rankup
            res = res - 100;
            int l = this.getLevel();
            this.setLevel(l + 1);
            this.setXp(res);
        } else {
            this.setXp(res);
        }

        return i;
    }

    @JsonIgnore
    public int loseBattleXP(Player opponent) {
        if (opponent.getLevel() > this.getLevel()) {
            return this.loseXP(5);
            // lose low xp
        } else if (opponent.getLevel() < this.getLevel()) {
            return this.loseXP(25);
            // lose high xp
        } else {
            return this.loseXP(15);
            // lose moderate xp
        }
    }

    @JsonIgnore
    public int winBattleXP(Player opponent) {
        if (opponent.getLevel() > this.getLevel()) {
            return this.gainXP(30);
            // reward high xp
        } else if (opponent.getLevel() < this.getLevel()) {
            return this.gainXP(10);
            // reward low xp
        } else {
            return this.gainXP(20);
            // reward moderate xp
        }
    }

    public String getShorthand() {
        return this.displayName +
                " | id: (" +
                this.id +
                ") | LVL: "+ this.level +" | (" +
                this.xp +
                "/100)";
    }
}
