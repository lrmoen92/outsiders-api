package slogan.motion.outsidersapi.domain.constant;

import java.util.Arrays;
import java.util.List;

//basically all the buffs and debuffs
public class Stat
{
	// for hp adjustments like dmg and healing
  public static final String DAMAGE = "DAMAGE";	
	// for hp adjustments like dmg and healing, unaffected by flat adjustments, still reduced by armor
  public static final String BONUS_DAMAGE = "BONUS_DAMAGE";	
  // for hp adjustments like piercing dmg reduction
  public static final String PIERCING_DAMAGE = "PIERCING_DAMAGE";	
  // for hp adjustments like damage to invuln or untargetable characters;
  public static final String TRUE_DAMAGE = "TRUE_DAMAGE";
   // dmg resist up or down
  public static final String DAMAGE_IN = "DAMAGE_IN";
  // phys dmg resist up or down
  public static final String PHYSICAL_DAMAGE_IN = "PHYSICAL_DAMAGE_IN";
  // mag dmg resist up or down
  public static final String MAGICAL_DAMAGE_IN = "MAGICAL_DAMAGE_IN";
  // aff dmg resist up or down
  public static final String AFFLICTION_DAMAGE_IN = "AFFLICTION_DAMAGE_IN";
  // dmg out up or down
  public static final String DAMAGE_OUT = "DAMAGE_OUT";
  // phys dmg out up or down
  public static final String PHYSICAL_DAMAGE_OUT = "PHYSICAL_DAMAGE_OUT";
  // mag dmg out up or down
  public static final String MAGICAL_DAMAGE_OUT = "MAGICAL_DAMAGE_OUT";
  // aff dmg out up or down
  public static final String AFFLICTION_DAMAGE_OUT = "AFFLICTION_DAMAGE_OUT";

  // costs increased (+/- random)
  public static final String COST_CHANGE = "COST_CHANGE";
  
  // all cooldowns up or down
  public static final String COOLDOWN_ALL = "COOLDOWN_ALL";
  // cd 1 up or down
  public static final String COOLDOWN_1 = "COOLDOWN_1";
  // cd 2 up or down
  public static final String COOLDOWN_2 = "COOLDOWN_2";
  // cd 3 up or down
  public static final String COOLDOWN_3 = "COOLDOWN_3";
  // cd 4 up or down
  public static final String COOLDOWN_4 = "COOLDOWN_4";
//  // this seems like a quality or something idk
//  public static final String COMBO_SLOT = "COMBO_SLOT";
  // strength energy up or down
  public static final String STRENGTH_CHANGE = "STRENGTH_CHANGE";
  // dexterity energy up or down
  public static final String DEXTERITY_CHANGE = "DEXTERITY_CHANGE";
  // arcana energy up or down
  public static final String ARCANA_CHANGE = "ARCANA_CHANGE";
  // divinity energy up or down
  public static final String DIVINITY_CHANGE = "DIVINITY_CHANGE";
  // any energy up or down
  public static final String ENERGY_CHANGE = "ENERGY_CHANGE";
  // destructible defense (actual stat)
  public static final String SHIELDS = "SHIELDS";
  // destructible defense up or down
  public static final String SHIELD_GAIN = "SHIELD_GAIN";
  // pretty sure these are the same as DAMAGE_IN
  // NOPE, DMG IN is like orihime ability, this is dmg reduction
  // in the form of an int, representing a percentage
  public static final String ARMOR = "ARMOR";
  public static final String PHYSICAL_ARMOR = "PHYSICAL_ARMOR";
  public static final String MAGICAL_ARMOR = "MAGICAL_ARMOR";
  

  // increasing these is good, decreasing them is bad
  public static final List<String> BUFFS = Arrays.asList(Stat.ARMOR, Stat.PHYSICAL_ARMOR, Stat.MAGICAL_ARMOR, Stat.SHIELD_GAIN, Stat.SHIELDS, Stat.ENERGY_CHANGE, Stat.STRENGTH_CHANGE, Stat.DEXTERITY_CHANGE, Stat.ARCANA_CHANGE, Stat.DIVINITY_CHANGE,
		  Stat.DAMAGE_OUT, Stat.PHYSICAL_DAMAGE_OUT, Stat.MAGICAL_DAMAGE_OUT, Stat.AFFLICTION_DAMAGE_OUT);
  
  // increasing these is bad, decreasing them is good
  public static final List<String> DEBUFFS = Arrays.asList(Stat.COOLDOWN_ALL, Stat.COOLDOWN_1, Stat.COOLDOWN_2, Stat.COOLDOWN_3, Stat.COOLDOWN_4, Stat.COST_CHANGE, Stat.DAMAGE_IN, Stat.PHYSICAL_DAMAGE_IN, Stat.MAGICAL_DAMAGE_IN, Stat.AFFLICTION_DAMAGE_IN);
  
}
