package slogan.motion.outsidersapi.bootstrap;

import com.google.gson.Gson;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import slogan.motion.outsidersapi.domain.jpa.*;
import slogan.motion.outsidersapi.service.*;
import slogan.motion.outsidersapi.util.NRG;

import java.util.List;

@Component
public class SeedBattleData {
    private static final Logger LOG = LoggerFactory.getLogger(SeedData.class);
    @Autowired
    private IBattleService IBattleService;
    @Autowired
    private ICharacterService characterService;

    @Transactional
    protected void deleteBattles() {
        // delete all battles on startup is default behavior
        IBattleService.deleteAll();
    }

    @Transactional
    protected String makeBattle(Player red, Player blue) {
        int arenaId = 1;
        String queue = "QUICK";
        Battle battle = this.IBattleService.getByArenaId(arenaId);
        if (battle == null) {
            //TODO: fix these ID assignments to query from DB
            Integer characterId1 = 1;
            Integer characterId2 = 2;
            Integer characterId3 = 3;
            battle = new Battle();
            battle.setStatus("MATCHING");
            battle.setId(NRG.randomInt());
            battle.setQueue(queue);
            battle.setArenaId(arenaId);
            battle.setPlayerOneStart(true);
            Combatant i1 = new Combatant(characterService.findById(characterId1), red.getId());
            Combatant i2 = new Combatant(characterService.findById(characterId2), red.getId());
            Combatant i3 = new Combatant(characterService.findById(characterId3), red.getId());
            i1.setPosition(0);
            i2.setPosition(1);
            i3.setPosition(2);
            red.addICombatants(List.of(i1,i2,i3));
            battle.setPlayerOne(red);

            Battle savedBattle = this.IBattleService.save(battle);
            return null;
        } else {
            //TODO: fix these ID assignments to query from DB
            Integer characterId1 = 4;
            Integer characterId2 = 5;
            Integer characterId3 = 6;
            Combatant i1 = new Combatant(characterService.findById(characterId1), blue.getId());
            Combatant i2 = new Combatant(characterService.findById(characterId2), blue.getId());
            Combatant i3 = new Combatant(characterService.findById(characterId3), blue.getId());
            i1.setPosition(3);
            i2.setPosition(4);
            i3.setPosition(5);
            blue.addICombatants(List.of(i1,i2,i3));
            battle.setPlayerTwo(blue);

            if (battle.isPlayerOneStart()) {
                battle.drawPlayerOneEnergy(1);
                battle.drawPlayerTwoEnergy(3);
            } else {
                battle.drawPlayerOneEnergy(3);
                battle.drawPlayerTwoEnergy(1);
            }

            battle.setStatus("STARTING");
            Battle savedBattle = this.IBattleService.save(battle);
            String battleJson = savedBattle.json();

            if (battleJson != null ) {
                return "{\"type\": \"INIT\", \"battle\": " + battleJson + "}";
            } else {
                // TODO: Improve error payload
                return null;
            }
        }
    }
}
