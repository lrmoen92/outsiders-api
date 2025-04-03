package slogan.motion.outsidersapi.domain.constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Cost {

    public static final List<String> free = new ArrayList<>();
    public static final List<String> oneRan = List.of(Energy.RANDOM);
    public static final List<String> oneDiv = List.of(Energy.DIVINITY);
    public static final List<String> oneArc = List.of(Energy.ARCANA);
    public static final List<String> oneStr = List.of(Energy.STRENGTH);
    public static final List<String> oneDex = List.of(Energy.DEXTERITY);
    public static final List<String> oneDivOneRan = Arrays.asList(Energy.DIVINITY, Energy.RANDOM);
    public static final List<String> oneArcOneRan = Arrays.asList(Energy.ARCANA, Energy.RANDOM);
    public static final List<String> oneStrOneRan = Arrays.asList(Energy.STRENGTH, Energy.RANDOM);
    public static final List<String> oneDexOneRan = Arrays.asList(Energy.DEXTERITY, Energy.RANDOM);
    public static final List<String> oneStrOneDiv = Arrays.asList(Energy.STRENGTH, Energy.DIVINITY);
    public static final List<String> oneStrOneDex = Arrays.asList(Energy.STRENGTH, Energy.DEXTERITY);
    public static final List<String> oneStrOneArc = Arrays.asList(Energy.STRENGTH, Energy.ARCANA);
    public static final List<String> oneDexOneDiv = Arrays.asList(Energy.DEXTERITY, Energy.DIVINITY);
    public static final List<String> oneDexOneArc = Arrays.asList(Energy.DEXTERITY, Energy.ARCANA);
    public static final List<String> oneDexOneStr = Arrays.asList(Energy.DEXTERITY, Energy.STRENGTH);
    public static final List<String> oneArcOneDiv = Arrays.asList(Energy.ARCANA, Energy.DIVINITY);
    public static final List<String> oneArcOneDex = Arrays.asList(Energy.ARCANA, Energy.DEXTERITY);
    public static final List<String> oneArcOneStr = Arrays.asList(Energy.ARCANA, Energy.STRENGTH);
    public static final List<String> oneDivOneArc = Arrays.asList(Energy.DIVINITY, Energy.ARCANA);
    public static final List<String> oneDivOneDex = Arrays.asList(Energy.DIVINITY, Energy.DEXTERITY);
    public static final List<String> oneDivOneStr = Arrays.asList(Energy.DIVINITY, Energy.STRENGTH);
    public static final List<String> twoRan = Arrays.asList(Energy.RANDOM, Energy.RANDOM);
    public static final List<String> twoDiv = Arrays.asList(Energy.DIVINITY, Energy.DIVINITY);
    public static final List<String> twoArc = Arrays.asList(Energy.ARCANA, Energy.ARCANA);
    public static final List<String> twoStr = Arrays.asList(Energy.STRENGTH, Energy.STRENGTH);
    public static final List<String> twoDex = Arrays.asList(Energy.DEXTERITY, Energy.DEXTERITY);
    public static final List<String> threeRan = Arrays.asList(Energy.RANDOM, Energy.RANDOM, Energy.RANDOM);
    public static final List<String> threeDiv = Arrays.asList(Energy.DIVINITY, Energy.DIVINITY, Energy.DIVINITY);
    public static final List<String> threeArc = Arrays.asList(Energy.ARCANA, Energy.ARCANA, Energy.DIVINITY);
    public static final List<String> threeStr = Arrays.asList(Energy.STRENGTH, Energy.STRENGTH, Energy.STRENGTH);
    public static final List<String> threeDex = Arrays.asList(Energy.DEXTERITY, Energy.DEXTERITY, Energy.DEXTERITY);

}
