package slogan.motion.outsidersapi.service;

import slogan.motion.outsidersapi.domain.jpa.Character;

import java.util.List;

public interface ICharacterService {

    void deleteAll();
    Character create(Character paramCharacter);

    Character findById(Integer paramInteger);

    List<Character> findAll();

    List<Character> findAllCharacters();
}
