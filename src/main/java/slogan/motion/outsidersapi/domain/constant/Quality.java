package slogan.motion.outsidersapi.domain.constant;

import java.util.Arrays;
import java.util.List;



// basically status effects
public class Quality
{
  // has active hidden effect
  public static final String STEALTHED = "STEALTHED";
  // hidden skills used by or on character revealed
  public static final String REVEALED = "REVEALED";
  // immune to dmg
  public static final String INVULNERABLE = "INVULNERABLE";
  // cant reduce dmg or go invuln
  public static final String VULNERABLE = "VULNERABLE";
  // immune to targets
  public static final String UNTARGETABLE = "UNTARGETABLE";
  // immune to being immune to targets
  public static final String TARGETABLE = "TARGETABLE";
  // is stunned all skills
  public static final String STUNNED = "STUNNED";
  // is stunned physical skills
  public static final String PHYSICAL_STUNNED = "PHYSICAL_STUNNED";
  // is stunned magic skills
  public static final String MAGICAL_STUNNED = "MAGICAL_STUNNED";
  // ability is countered
  public static final String COUNTERED = "COUNTERED";
  // get enemy effect removed
  public static final String DISPELLED = "DISPELLED";
  
  public static final List<String> BUFFS = Arrays.asList(Quality.STEALTHED, Quality.INVULNERABLE, Quality.UNTARGETABLE, Quality.DISPELLED);
  
  public static final List<String> DEBUFFS = Arrays.asList(Quality.REVEALED, Quality.VULNERABLE, Quality.TARGETABLE, Quality.STUNNED, Quality.PHYSICAL_STUNNED, Quality.MAGICAL_STUNNED);
  
  // affected by ability or effect_name stacks
  public static String AFFECTED_BY(String effectName, int stacks) {
	  return "AFFECTEDBY_" + effectName.toUpperCase() + "_" + stacks; 
  };

  // affected by ability or effect_name
  public static String AFFECTED_BY(String effectName) {
	  return "AFFECTEDBY_" + effectName.toUpperCase(); 
  };
  
}
