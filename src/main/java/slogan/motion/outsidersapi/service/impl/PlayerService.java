package slogan.motion.outsidersapi.service.impl;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import slogan.motion.outsidersapi.domain.jpa.Player;
import slogan.motion.outsidersapi.repository.PlayerRepository;
import slogan.motion.outsidersapi.service.IPlayerService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class PlayerService implements IPlayerService {
    @Autowired
    private PlayerRepository repo;

    public Player create(Player entity) {
        if (entity.getId() == 0) {
            // save only if it doesn't exist
            List<Player> player = repo.findAllByDisplayName(entity.getDisplayName());
            if (player.isEmpty()) {
                Player savedPlayer = repo.save(entity);
                log.info("------==SAVE |PLA:{}", savedPlayer);
                return savedPlayer;
            } else {
                return player.get(0);
            }
        } else {
            throw new RuntimeException("Attempted to update an existing Player");
        }
    }

    public Player update(Player entity) {
        return repo.save(entity);
    }


    public Player findByEmail(String email) {
        for (Player p : repo.findAll()) {
            if (p.getCredentials() != null) {
                if (p.getCredentials().getEmail().equals(email)) {
                    return p;
                }
            }
        }
        return null;
    }

    public Player findByDisplayName(String name) {
        List<Player> list = repo.findAllByDisplayName(name);
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

    public Iterable<Player> saveAll(Iterable<Player> entities) {
        return this.repo.saveAll(entities);
    }

    public Optional<Player> findById(Integer id) {
        return this.repo.findById(id);
    }

    public boolean existsById(Integer id) {
        return this.repo.existsById(id);
    }

    public Iterable<Player> findAll() {
        return this.repo.findAll();
    }

    public Iterable<Player> findAllById(Iterable<Integer> ids) {
        return this.repo.findAllById(ids);
    }

    public long count() {
        return this.repo.count();
    }

    public void deleteById(Integer id) {
        this.repo.deleteById(id);
    }

    public void delete(Player entity) {
        this.repo.delete(entity);
    }

    public void deleteAll(Iterable<Player> entities) {
        this.repo.deleteAll(entities);
    }

    public void deleteAll() {
        this.repo.deleteAll();
    }
}
