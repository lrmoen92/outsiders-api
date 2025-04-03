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

    protected String avatarUrl;
    protected String name;
    protected String description;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    protected List<Ability> abilities = new ArrayList<>();

    public void addAbilities(List<Ability> input) {
        input.forEach(this::addAbility);
    }

    public void addAbility(Ability g) {
        abilities.add(g);
        g.getCharacters().add(this);
    }

    public void removeAbility(Ability g) {
        g.getCharacters().remove(this);
        abilities.remove(g);
    }

    @ElementCollection(fetch = FetchType.LAZY)
    @Fetch(value = FetchMode.SUBSELECT)
    protected List<String> factions = new ArrayList<>();

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
        // bug is here?
        this.addAbilities(input.getAbilities());
    }

    public Character(String avatarUrl, String name, String description, List<String> factions) {
        this.avatarUrl = avatarUrl;
        this.name = name;
        this.description = description;
        this.factions = factions;
    }
}
