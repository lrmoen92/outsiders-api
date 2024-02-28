package slogan.motion.outsidersapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import slogan.motion.outsidersapi.domain.jpa.Player;

import java.util.List;
import java.util.Optional;

public interface PlayerRepository
  extends JpaRepository<Player, Integer>
{
    List<Player> findAllByDisplayName(String displayName);
}
