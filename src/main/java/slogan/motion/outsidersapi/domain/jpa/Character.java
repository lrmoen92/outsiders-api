package slogan.motion.outsidersapi.domain.jpa;

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
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorColumn(name = "charType")
public class Character extends JsonObject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(insertable = false, updatable = false)
    private String charType;

    private String avatarUrl;
    private String name;
    private String description;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "Character", fetch = FetchType.EAGER)
    private List<Ability> IAbilities = new ArrayList<>();

    public void addIAbilities(List<Ability> input) {
        input.forEach(this::addIAbility);
    }

    public void addIAbility(Ability g) {
        IAbilities.add(g);
//        // TODO:  look at this for creation vs update
//        g.setId(0);
//        g.getEnemyEffects().forEach(e -> e.setId(0));
//        g.getAllyEffects().forEach(e -> e.setId(0));
//        g.getAoeAllyEffects().forEach(e -> e.setId(0));
//        g.getAoeEnemyEffects().forEach(e -> e.setId(0));
//        g.getSelfEffects().forEach(e -> e.setId(0));
        g.setCharacter(this);
    }

    public void removeIAbility(Ability g) {
        g.setCharacter(null);
        IAbilities.remove(g);
    }

    @ElementCollection(fetch = FetchType.LAZY)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<String> factions = new ArrayList<>();

    public String getShorthand() {
        return this.name +
                " | id: (" +
                this.id +
                ") | " +
                this.description;
    }

    public Character(Character input) {
//        this.id = input.getId();
        this.avatarUrl = input.getAvatarUrl();
        this.name = input.getName();
        this.description = input.getDescription();
        this.factions = input.getFactions();
        this.addIAbilities(input.getIAbilities());
    }

    public Character(String avatarUrl, String name, String description, List<String> factions) {
        this.avatarUrl = avatarUrl;
        this.name = name;
        this.description = description;
        this.factions = factions;
    }
}
