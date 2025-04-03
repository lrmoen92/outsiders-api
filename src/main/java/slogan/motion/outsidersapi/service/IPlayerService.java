package slogan.motion.outsidersapi.service;

import slogan.motion.outsidersapi.domain.jpa.Player;

import java.util.Optional;

public interface IPlayerService {


    Player update(Player player);

    Player create(Player paramPlayer);

    Player findByDisplayName(String name);

    Player findByEmail(String email);

    Iterable<Player> saveAll(Iterable<Player> paramIterable);

    Optional<Player> findById(Integer paramInteger);

    boolean existsById(Integer paramInteger);

    Iterable<Player> findAll();

    Iterable<Player> findAllById(Iterable<Integer> paramIterable);

    long count();

    void deleteById(Integer paramInteger);

    void delete(Player paramPlayer);

    void deleteAll(Iterable<Player> paramIterable);

    void deleteAll();
}
