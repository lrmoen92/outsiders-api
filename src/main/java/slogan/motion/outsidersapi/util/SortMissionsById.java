package slogan.motion.outsidersapi.util;

import slogan.motion.outsidersapi.domain.jpa.Mission;

import java.util.Comparator;

public class SortMissionsById implements Comparator<Mission> {

    public int compare(Mission a, Mission b) {
        return a.getId() - b.getId();
    }
}
