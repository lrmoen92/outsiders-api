package slogan.motion.outsidersapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import slogan.motion.outsidersapi.domain.jpa.Character;

import java.util.List;
import java.util.Optional;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Integer> {

    Optional<Character> findByName(String name);

    List<Character> findAllByCharType(String name);

    List<Character> findAllByName(String name);
}
