package slogan.motion.outsidersapi.service;

import slogan.motion.outsidersapi.domain.jpa.Mission;

import java.util.List;

public interface IMissionService {
    void deleteAll();

    Mission create(Mission paramMission);

    Mission findById(Integer paramInteger);

    List<Mission> findAll();
}
