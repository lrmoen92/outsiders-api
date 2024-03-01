package slogan.motion.outsidersapi.domain.jpa;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MissionProgress extends JsonObject {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JsonIgnore
    private Player Player;

    //TODO: change this to mission
    private int missionId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "MissionProgress", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<MissionRequirement> requirements = new ArrayList<>();
    public void addIMissionRequirement(MissionRequirement g) {
        requirements.add(g);
        g.setMissionProgress(this);
    }

    public void removeIMissionRequirement(MissionRequirement g) {
        g.setMissionProgress(null);
        requirements.remove(g);
    }

    public void makeSafe() {
        this.setPlayer(null);
        this.getRequirements().forEach(MissionRequirement::makeSafe);
    }

    public MissionProgress(Mission input, List<MissionRequirement> progress) {
        this.missionId = input.getId();
        this.requirements = new ArrayList<>();
        for (MissionRequirement misReq : input.getRequirements()) {
            for (MissionRequirement prog : progress) {
                if (misReq.getTargetFaction().equals(prog.getTargetFaction()) && misReq.getUserFaction().equals(prog.getUserFaction())) {
                    addIMissionRequirement(prog);
                }
            }
        }
    }
}
