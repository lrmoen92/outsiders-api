package slogan.motion.outsidersapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import slogan.motion.outsidersapi.domain.jpa.Battle;

public abstract interface BattleRepository extends JpaRepository<Battle, Integer> {

	public Battle getByArenaId(Integer id);

}
