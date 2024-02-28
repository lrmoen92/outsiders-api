package slogan.motion.outsidersapi.service;

import slogan.motion.outsidersapi.domain.jpa.Mission;

import java.util.List;

public abstract interface IMissionService {
  void deleteAll();
	
  public abstract Mission create(Mission paramMission);
  
  public abstract Mission findById(Integer paramInteger);
  
  public abstract List<Mission> findAll();
}
