package slogan.motion.outsidersapi.domain.constant;

public class Conditional {

    // these seem like they're for buff abilities

    //target an enemy with a damaging effect
    public static final String PHYSICAL = "PHYSICAL";
    //target an enemy with a damaging effect
    public static final String MAGICAL = "MAGICAL";
    //target an enemy with a damaging effect
    public static final String AFFLICTION = "AFFLICTION";

    // these seem like they're for buff abilities

    //target an enemy with a damaging effect
    public static final String ATTACK = "ATTACK";
    //damage an enemy with a damaging effect
    public static final String DAMAGE = "DAMAGE";
    //target an ally
    public static final String BUFF = "BUFF";
    //target an enemy non-damaging
    public static final String DEBUFF = "DEBUFF";
    //get a kill
    public static final String KILL = "KILL";

    // these seem like they're for counter abilities

    //become targeted by an enemy damaging effect
    public static final String ATTACKED = "ATTACKED";
    //become damaged by an enemy damaging effect
    public static final String DAMAGED = "DAMAGED";
    //become buffed by ally
    public static final String BUFFED = "BUFFED";
    //become targeted by an enemy non damaging
    public static final String DEBUFFED = "DEBUFFED";
    //become killed
    public static final String KILLED = "KILLED";

    // mirror of Quality
    // affected by ability or effect_name stacks
    public static String USER_AFFECTED_BY(String effectName, int stacks) {
        return "USER_AFFECTEDBY_" + effectName.toUpperCase() + "_" + stacks;
    }

    // affected by ability or effect_name
    public static String USER_AFFECTED_BY(String effectName) {
        return "USER_AFFECTEDBY_" + effectName.toUpperCase();
    }

    // affected by quality/status
    public static String USER_HAS_QUALITY(String qualityName) {
        return "USER_IS_" + qualityName.toUpperCase();
    }

    // Did a thing
    public static String USER_DID(String iDid) {
        return "USER_DID_" + iDid.toUpperCase();
    }

    // A thing happened to me
    public static String USER_WAS(String iWas) {
        return "USER_WAS_" + iWas.toUpperCase();
    }


    // mirror of Quality
    // affected by ability or effect_name stacks
    public static String TARGET_AFFECTED_BY(String effectName, int stacks) {
        return "TARGET_AFFECTEDBY_" + effectName.toUpperCase() + "_" + stacks;
    }

    // affected by ability or effect_name
    public static String TARGET_AFFECTED_BY(String effectName) {
        return "TARGET_AFFECTEDBY_" + effectName.toUpperCase();
    }

    // affected by quality/status
    public static String TARGET_HAS_QUALITY(String qualityName) {
        return "TARGET_IS_" + qualityName.toUpperCase();
    }

    // Did a thing
    public static String TARGET_DID(String iDid) {
        return "TARGET_DID_" + iDid.toUpperCase();
    }

    // A thing happened to me
    public static String TARGET_WAS(String iWas) {
        return "TARGET_WAS_" + iWas.toUpperCase();
    }

}
