package slogan.motion.outsidersapi.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import slogan.motion.outsidersapi.domain.jpa.Character;
import slogan.motion.outsidersapi.repository.CharacterRepository;
import slogan.motion.outsidersapi.service.ICharacterService;

import java.util.List;

@Slf4j
@Service
public class CharacterService implements ICharacterService {

    @Autowired
    private CharacterRepository characterRepository;

    @Override
    public void deleteAll() {
        characterRepository.deleteAll();
    }

    @Override
    public Character create(Character paramCharacter) {
        if (paramCharacter.getId() == 0) {
            // save only if it doesn't exist
            List<Character> character = characterRepository.findAllByName(paramCharacter.getName());
            if (character.isEmpty()) {
                Character savedChar = characterRepository.save(paramCharacter);
                log.info("------==SAVE |CHA:{}", savedChar);
                return savedChar;
            } else {
                return paramCharacter;
            }
        } else {
            throw new RuntimeException("Attempted to update an existing character");
        }
    }

    @Override
    public Character findById(Integer paramInteger) {
        return characterRepository.getReferenceById(paramInteger);
    }

    @Override
    public List<Character> findAll() {
        return characterRepository.findAll();
    }

    @Override
    public List<Character> findAllCharacters() {
        List<Character> list = characterRepository.findAllByCharType("Character");
        return list;
    }
}
