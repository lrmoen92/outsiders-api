package slogan.motion.outsidersapi.bootstrap;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import slogan.motion.outsidersapi.domain.jpa.Player;
import slogan.motion.outsidersapi.service.IBattleService;
import slogan.motion.outsidersapi.service.ICharacterService;
import slogan.motion.outsidersapi.service.IMissionService;
import slogan.motion.outsidersapi.service.IPlayerService;

@Slf4j
@Component
public class SeedData implements CommandLineRunner {
    @Autowired
    private IBattleService IBattleService;
    @Autowired
    private IPlayerService IPlayerService;
    @Autowired
    private IMissionService IMissionService;
    @Autowired
    private ICharacterService ICharacterService;

    @Autowired
    private SeedPlayerData seedPlayerData;

    @Autowired
    private SeedCharacterData seedCharacterData;

    @Autowired
    private SeedMissionData seedMissionData;

    @Autowired
    private SeedBattleData seedBattleData;

    public void run(String... args) {
        log.info("--START--");
        deleteBattles();
        tryToSeed();
        log.info("--COMPLETE--");
    }

    protected void deleteBattles() {
        // delete all battles on startup is default behavior
        IBattleService.deleteAll();
    }

    public void tryToSeed() {
        try {
            seedCharacterData.makeOutsiders();
        } catch (Exception e) {
            log.warn("--CHARACTER EXCEPTION--", e);
            ICharacterService.deleteAll();
        }

        try {
            seedMissionData.makeMissions();
        } catch (Exception e) {
            log.warn("--MISSION EXCEPTION--", e);
            IMissionService.deleteAll();
        }

        try {
            Player red = seedPlayerData.makeRed();
            Player blue = seedPlayerData.makeBlue();
            try {
                seedBattleData.makeBattle(red, blue);
                seedBattleData.makeBattle(red, blue);
            } catch (Exception e) {
                log.warn("--BATTLE EXCEPTION--", e);
                IBattleService.deleteAll();
            }
        } catch (Exception e) {
            log.warn("--PLAYER EXCEPTION--", e);
            IPlayerService.deleteAll();
        }


    }
}
