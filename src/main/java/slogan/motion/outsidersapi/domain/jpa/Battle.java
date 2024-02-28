package slogan.motion.outsidersapi.domain.jpa;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import slogan.motion.outsidersapi.util.NRG;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Battle extends JsonObject {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private boolean playerOneStart = new Random().nextBoolean();
    private String status;
    private String queue;
    private int turn = 0;
    private int arenaId;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, mappedBy = "Battle", fetch = FetchType.EAGER)
    private List<Player> players = new ArrayList<>();

    @JsonGetter
    public boolean getPlayerOneVictory() {
        if (playerTwoExists()) {
            for (Combatant c : this.getPlayerTwoTeam()) {
                if (!c.isDead()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @JsonGetter
    public boolean getPlayerTwoVictory() {
        for (Combatant c : this.getPlayerOneTeam()) {
            if (!c.isDead()) {
                return false;
            }
        }
        return true;
    }

    public int getPlayerIdOne() {
        return this.getPlayerOne().getId();
    }

    public int getPlayerIdTwo() {
        if (playerTwoExists()) {
            return this.getPlayerTwo().getId();
        }
        return -1;
    }

    public List<Combatant> getPlayerOneTeam() {
		return this.getPlayerOne().getCombatants();
    }

    public List<Combatant> getPlayerTwoTeam() {
        if (playerTwoExists()) {
            return this.getPlayerTwo().getCombatants();
        }
        return List.of();
    }

    public boolean playerTwoExists() {
        return this.players.size() > 1;
    }

    public void drawPlayerTwoEnergy(int i) {
        if (playerTwoExists()) {
            this.setPlayerTwoEnergy(NRG.drawEnergy(i, this.getPlayerTwoEnergy()));
        }
    }

    public void drawPlayerOneEnergy(int i) {
        this.setPlayerOneEnergy(NRG.drawEnergy(i, this.getPlayerOneEnergy()));
    }

    @Override
    public String toString() {
        return this.getShorthand();
    }
    public String getShorthand() {
        return "Battle [playerOneStart=" + playerOneStart + ", turn=" + turn + ", arenaId=" + arenaId + ", playerIdOne="
                + getPlayerIdOne() + ", playerIdTwo=" + getPlayerIdTwo() + ", "
                + (getPlayerOneEnergy() != null ? "playerOneEnergy=" + getPlayerOneEnergy() + ", " : "")
                + (getPlayerTwoEnergy() != null ? "playerTwoEnergy=" + getPlayerTwoEnergy() : "") + "]";
    }

    public Map<String, Integer> getPlayerOneEnergy() {
        return getPlayerOne().getPlayerEnergy();
    }

    public void setPlayerOneEnergy(Map<String, Integer> playerOneEnergy) {
        this.getPlayerOne().setPlayerEnergy(playerOneEnergy);
    }

    public Map<String, Integer> getPlayerTwoEnergy() {
        if (playerTwoExists()) {
            return getPlayerTwo().getPlayerEnergy();
        }
        return null;
    }

    public void setPlayerTwoEnergy(Map<String, Integer> playerTwoEnergy) {
        if (playerTwoExists()) {
            this.getPlayerTwo().setPlayerEnergy(playerTwoEnergy);
        }
    }

    public Player getPlayerOne(){
        if (players.isEmpty()) {
            return null;
        } else {
            return players.get(0);
        }
    }

    public Player getPlayerTwo(){
        if (players.isEmpty()) {
            return null;
        } else if (players.size() == 1) {
            return null;
        } else {
            return players.get(1);
        }
    }

    public void setPlayerOne(Player player) {
        this.players.add(0, player);
        player.setBattle(this);
    }

    public void setPlayerTwo(Player player) {
        this.players.add(1, player);
        player.setBattle(this);
    }
}
