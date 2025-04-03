package slogan.motion.outsidersapi.service;

import slogan.motion.outsidersapi.domain.jpa.Battle;

import java.util.Optional;

public interface IBattleService {

    Battle getByPlayerDisplayName(String displayName);

    Battle getByArenaId(int paramInt);

    Battle save(Battle paramBattle);

    Iterable<Battle> saveAll(Iterable<Battle> paramIterable);

    Optional<Battle> findById(Integer paramInteger);

    boolean existsById(Integer paramInteger);

    Iterable<Battle> findAll();

    Iterable<Battle> findAllById(Iterable<Integer> paramIterable);

    long count();

    void deleteById(Integer paramInteger);

    void delete(Battle paramBattle);

    void deleteAll(Iterable<Battle> paramIterable);

    void deleteAll();
}
