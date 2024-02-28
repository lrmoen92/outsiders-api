package slogan.motion.outsidersapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import slogan.motion.outsidersapi.domain.jpa.Mission;

import java.util.Optional;

public interface MissionRepository extends JpaRepository<Mission, Integer> {

    Optional<Mission> findByName(String name);
}
