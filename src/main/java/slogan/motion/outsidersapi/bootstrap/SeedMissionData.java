package slogan.motion.outsidersapi.bootstrap;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import slogan.motion.outsidersapi.domain.constant.Faction;
import slogan.motion.outsidersapi.domain.jpa.Mission;
import slogan.motion.outsidersapi.domain.jpa.MissionRequirement;
import slogan.motion.outsidersapi.service.IMissionService;

import java.util.ArrayList;
import java.util.List;

@Component
public class SeedMissionData {

    @Autowired
    private IMissionService IMissionService;

    @Transactional
    protected void makeMissions() {
        makeTristaneMission();
        makeDrundarMission();
    }

    protected Mission makeDrundarMission() {
        Mission m = new Mission();

        m.setAvatarUrl("/assets/drundar.png");
        m.setCharacterIdUnlocked(7);
        m.setDescription("Drundar is tearing down the place, no choice but to fight!");
        m.setMinmumLevel(6);
        m.setName("Walls Come Tumbling Down");

        List<MissionRequirement> requirements = new ArrayList<>();

        MissionRequirement mq1 = new MissionRequirement();
        mq1.setAmount(3);
        mq1.setUserFaction(Faction.TRISTANE);

        MissionRequirement mq2 = new MissionRequirement();
        mq2.setAmount(3);
        mq2.setUserFaction(Faction.GEDDY);

        MissionRequirement mq3 = new MissionRequirement();
        mq3.setAmount(3);
        mq3.setUserFaction(Faction.ALEX);

        MissionRequirement mq4 = new MissionRequirement();
        mq4.setAmount(3);
        mq4.setUserFaction(Faction.SHINZO);

        requirements.add(mq1);
        requirements.add(mq2);
        requirements.add(mq3);
        requirements.add(mq4);

        m.addIMissionRequirements(requirements);

        return this.IMissionService.create(m);
    }

    protected Mission makeTristaneMission() {
        Mission m = new Mission();

        m.setAvatarUrl("/assets/tristane.png");
        m.setCharacterIdUnlocked(6);
        m.setDescription("Gotta complete a job for Tristane.");
        m.setMinmumLevel(0);
        m.setName("Guild Duties");

        List<MissionRequirement> requirements = new ArrayList<>();

        MissionRequirement mq1 = new MissionRequirement();
        mq1.setAmount(3);
        mq1.setUserFaction(Faction.ALEX);

        MissionRequirement mq2 = new MissionRequirement();
        mq2.setAmount(3);
        mq2.setUserFaction(Faction.GEDDY);

        requirements.add(mq1);
        requirements.add(mq2);
        m.addIMissionRequirements(requirements);

        return this.IMissionService.create(m);
    }
}
