package slogan.motion.outsidersapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import slogan.motion.outsidersapi.domain.jpa.Battle;

public interface BattleRepository extends JpaRepository<Battle, Integer> {

    Battle getByArenaId(Integer id);

}
