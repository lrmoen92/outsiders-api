package slogan.motion.outsidersapi.bootstrap;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import slogan.motion.outsidersapi.domain.jpa.MissionProgress;
import slogan.motion.outsidersapi.domain.jpa.Player;
import slogan.motion.outsidersapi.domain.jpa.PlayerCredentials;
import slogan.motion.outsidersapi.service.IPlayerService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class SeedPlayerData {

    @Autowired
    private IPlayerService IPlayerService;

    @Transactional
    public Player makeRed() {
        Player player = new Player();
        PlayerCredentials creds = new PlayerCredentials();
        creds.setEmail("Red@Red.com");
        creds.setPassword("red");
        player.setLevel(1);
        player.setDisplayName("Red");
        player.setAvatarUrl("https://i.imgur.com/x8VwSea.png");
        player.setCredentials(creds);

        Set<Integer> chars = new HashSet<>();
        chars.addAll(Arrays.asList(0, 1, 2, 3, 4, 5, 6));
        player.setCharacterIdsUnlocked(chars);

        Set<Integer> miss = new HashSet<>();
        miss.add(0);
        player.setMissionIdsCompleted(miss);

        player.addIMissionProgress(new MissionProgress());

        return this.IPlayerService.create(player);
    }

    @Transactional
    public Player makeBlue() {
        Player player = new Player();
        PlayerCredentials creds = new PlayerCredentials();
        creds.setEmail("Blue@Blue.com");
        creds.setPassword("blue");
        player.setLevel(1);
        player.setDisplayName("Blue");
        player.setAvatarUrl("https://i.imgur.com/d8MKvv0.png");
        player.setCredentials(creds);

        Set<Integer> chars = new HashSet<>();
        chars.addAll(Arrays.asList(0, 1, 2, 3, 4, 5, 6));
        player.setCharacterIdsUnlocked(chars);

        Set<Integer> miss = new HashSet<>();
        miss.add(0);
        player.setMissionIdsCompleted(miss);

        player.addIMissionProgress(new MissionProgress());

        return this.IPlayerService.create(player);
    }
}
