package slogan.motion.outsidersapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import slogan.motion.outsidersapi.domain.jpa.Player;

import java.util.List;

public interface PlayerRepository
        extends JpaRepository<Player, Integer> {
    List<Player> findAllByDisplayName(String displayName);
}
