package slogan.motion.outsidersapi.service;

import slogan.motion.outsidersapi.domain.jpa.Player;

import java.util.Optional;

public abstract interface IPlayerService
{
  public abstract Player create(Player paramPlayer);
  
  public abstract Player findByDisplayName(String name);
  
  public abstract Player findByEmail(String email);
  
  public abstract Iterable<Player> saveAll(Iterable<Player> paramIterable);
  
  public abstract Optional<Player> findById(Integer paramInteger);
  
  public abstract boolean existsById(Integer paramInteger);
  
  public abstract Iterable<Player> findAll();
  
  public abstract Iterable<Player> findAllById(Iterable<Integer> paramIterable);
  
  public abstract long count();
  
  public abstract void deleteById(Integer paramInteger);
  
  public abstract void delete(Player paramPlayer);
  
  public abstract void deleteAll(Iterable<Player> paramIterable);
  
  public abstract void deleteAll();
}
