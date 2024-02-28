package slogan.motion.outsidersapi.service.impl;


import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import slogan.motion.outsidersapi.domain.jpa.Mission;
import slogan.motion.outsidersapi.repository.MissionRepository;
import slogan.motion.outsidersapi.service.IMissionService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class MissionService implements IMissionService {

    @Autowired
    private MissionRepository repo;

    public void deleteAll() {
        this.repo.deleteAll();
    }

    @Transactional
    public Mission create(Mission entity) {
        if (entity.getId() == 0) {
            // save only if it doesn't exist
            Optional<Mission> mission = repo.findByName(entity.getName());
            return mission.orElseGet(() -> {
                Mission savedMission = repo.save(entity);
                log.info("Saved new Mission: {}", savedMission);
                return savedMission;
            });
        } else {
            throw new RuntimeException("Attempted to update an existing mission");
        }
    }

    public Mission findById(Integer id) {
        return this.repo.findById(id).get();
    }

    public List<Mission> findAll() {
        return this.repo.findAll();
    }

}
