package slogan.motion.outsidersapi.domain.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Mission extends JsonObject {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(unique = true)
    private String name;
    private String description;
    private String avatarUrl;
    private int minmumLevel;
    private int prerequisiteMissionId;
    private int characterIdUnlocked;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "Mission", orphanRemoval = true)
    private List<MissionRequirement> requirements = new ArrayList<>();

    public void addIMissionRequirements(List<MissionRequirement> list) {
        list.forEach(this::addIMissionRequirement);
    }

    public void addIMissionRequirement(MissionRequirement g) {
        requirements.add(g);
        g.setMission(this);
    }

    public void removeIMissionRequirement(MissionRequirement g) {
        g.setMission(null);
        requirements.remove(g);
    }
}
