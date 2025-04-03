package slogan.motion.outsidersapi.service.impl;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import slogan.motion.outsidersapi.domain.jpa.Battle;
import slogan.motion.outsidersapi.domain.jpa.Player;
import slogan.motion.outsidersapi.repository.BattleRepository;
import slogan.motion.outsidersapi.service.IBattleService;
import slogan.motion.outsidersapi.service.IPlayerService;

import java.util.Optional;

@Slf4j
@Service
public class BattleService implements IBattleService {
    private final BattleRepository repo;
    private final IPlayerService IPlayerService;

    public BattleService(BattleRepository repo, IPlayerService IPlayerService) {
        this.repo = repo;
        this.IPlayerService = IPlayerService;
    }

    public Battle getByPlayerDisplayName(String name) {
        Player player = IPlayerService.findByDisplayName(name);
        if (player != null) {
            for (Battle b : repo.findAll()) {
                if (b.getPlayerIdOne() == player.getId()) {
                    return b;
                }
            }
            return null;
        } else {
            return null;
        }
    }

    public Battle getByArenaId(int id) {
        return this.repo.getByArenaId(id);
    }

    public Battle save(Battle entity) {
        Battle b = this.repo.save(entity);
        log.info("------==SAVE |BAT:{}", b);
        return b;
    }

    public Iterable<Battle> saveAll(Iterable<Battle> entities) {
        return this.repo.saveAll(entities);
    }

    public Optional<Battle> findById(Integer id) {
        return this.repo.findById(id);
    }

    public boolean existsById(Integer id) {
        return this.repo.existsById(id);
    }

    public Iterable<Battle> findAll() {
        return this.repo.findAll();
    }

    public Iterable<Battle> findAllById(Iterable<Integer> ids) {
        return this.repo.findAllById(ids);
    }

    public long count() {
        return this.repo.count();
    }

    public void deleteById(Integer id) {
        this.repo.deleteById(id);
    }

    @Transactional
    public void delete(Battle entity) {

        removePlayers(entity);
        this.repo.delete(entity);
    }

    @Transactional
    protected void removePlayers(Battle battle) {
        battle.removePlayers();
        this.repo.save(battle);
    }

    public void deleteAll(Iterable<Battle> entities) {
        this.repo.deleteAll(entities);
    }

    @Transactional
    public void deleteAll() {
        this.removeAllPlayers();
        this.repo.deleteAll();
    }

    @Transactional
    protected void removeAllPlayers() {
        this.repo.findAll().forEach(battle -> {
            battle.removePlayers();
            this.repo.save(battle);
        });
    }
}
