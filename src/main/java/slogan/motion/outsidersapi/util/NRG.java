package slogan.motion.outsidersapi.util;

import slogan.motion.outsidersapi.domain.constant.Energy;

import java.util.*;

public class NRG {


    public static boolean isNullOrEmpty(String s) {
        return s == null || "".equals(s);
    }

    public static int getTotalForEnergy(Map<String, Integer> map) {
        int counter = 0;
        for (Map.Entry<String, Integer> e : map.entrySet()) {
            counter = counter + e.getValue();
        }
        return counter;
    }

    // TODO: improve this RNG nightmare
    public static int randomInt() {
        return randomInt(99999);
    }

    public static int randomInt(int in) {
        return new Random().nextInt(in);
    }

    public static void randomlyRemoveN(Map<String, Integer> energy, int n) {
        for (int i = n; i > 0; i--) {
            randomlyRemoveOne(energy);
        }
    }

    public static void randomlyRemoveOne(Map<String, Integer> energy) {
        List<String> options = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : energy.entrySet()) {
            if (entry.getValue() != 0) {
                options.add(entry.getKey());
            }
        }

        if (options.size() > 0) {
            Collections.shuffle(options);
            String chosen = options.get(0);
            int oldVal = energy.get(chosen);
            energy.put(chosen, oldVal - 1);
        }

    }

    public static List<String> draw(int i) {
        List<String> res = new ArrayList();
        for (int j = i; j > 0; j--) {
            Random random = new Random();
            random.nextInt(4);
            int det = random.nextInt() % 4;
            if (det < 0) {
                det = -det;
            }
            res.add(det == 2 ? "STRENGTH" : det == 1 ? "DIVINITY" : det == 0 ? "ARCANA" : "DEXTERITY");
        }
        return res;
    }

    public static Map<String, Integer> drawEnergy(int i) {
        return drawEnergy(i, null);
    }

    public static Map<String, Integer> drawEnergy(int i, Map<String, Integer> previous) {
        if (previous == null || previous.isEmpty()) {
            previous = new HashMap<>();
            previous.put(Energy.STRENGTH, 0);
            previous.put(Energy.DEXTERITY, 0);
            previous.put(Energy.ARCANA, 0);
            previous.put(Energy.DIVINITY, 0);
        }

        List<String> drawnEnergy = draw(i);
        for (String energy : drawnEnergy) {
            Integer oldVal = previous.get(energy);
            previous.put(energy, oldVal + 1);
        }
        return previous;
    }
}
