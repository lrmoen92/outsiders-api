package slogan.motion.outsidersapi.bootstrap;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import slogan.motion.outsidersapi.domain.constant.*;
import slogan.motion.outsidersapi.domain.jpa.Ability;
import slogan.motion.outsidersapi.domain.jpa.Character;
import slogan.motion.outsidersapi.domain.jpa.Effect;
import slogan.motion.outsidersapi.domain.jpa.effects.*;
import slogan.motion.outsidersapi.service.ICharacterService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SeedCharacterData {

    @Autowired
    private ICharacterService characterService;

    protected SelfEffect buildSelfEffect(int duration, String name, String description, String url, boolean physical, boolean magical, boolean affliction, boolean interruptable, boolean conditional, boolean visible, boolean stacks, Map<String, Integer> statMods, String quality, String condition) {
        SelfEffect selfEffect = new SelfEffect(physical, magical, affliction, interruptable, conditional);
        return (SelfEffect) buildIEffect(selfEffect, duration, name, description, url, visible, stacks, statMods, quality, condition);
    }

    protected EnemyEffect buildEnemyEffect(int duration, String name, String description, String url, boolean physical, boolean magical, boolean affliction, boolean interruptable, boolean conditional, boolean visible, boolean stacks, Map<String, Integer> statMods, String quality, String condition) {
        EnemyEffect enemyEffect = new EnemyEffect(physical, magical, affliction, interruptable, conditional);
        return (EnemyEffect) buildIEffect(enemyEffect, duration, name, description, url, visible, stacks, statMods, quality, condition);
    }

    protected AllyEffect buildAllyEffect(int duration, String name, String description, String url, boolean physical, boolean magical, boolean affliction, boolean interruptable, boolean conditional, boolean visible, boolean stacks, Map<String, Integer> statMods, String quality, String condition) {
        AllyEffect allyEffect = new AllyEffect(physical, magical, affliction, interruptable, conditional);
        return (AllyEffect) buildIEffect(allyEffect, duration, name, description, url, visible, stacks, statMods, quality, condition);
    }

    protected AoeEnemyEffect buildAoeEnemyEffect(int duration, String name, String description, String url, boolean physical, boolean magical, boolean affliction, boolean interruptable, boolean conditional, boolean visible, boolean stacks, Map<String, Integer> statMods, String quality, String condition) {
        AoeEnemyEffect aoeEnemyEffect = new AoeEnemyEffect(physical, magical, affliction, interruptable, conditional);
        return (AoeEnemyEffect) buildIEffect(aoeEnemyEffect, duration, name, description, url, visible, stacks, statMods, quality, condition);
    }

    protected AoeAllyEffect buildAoeAllyEffect(int duration, String name, String description, String url, boolean physical, boolean magical, boolean affliction, boolean interruptable, boolean conditional, boolean visible, boolean stacks, Map<String, Integer> statMods, String quality, String condition) {
        AoeAllyEffect aoeAllyEffect = new AoeAllyEffect(physical, magical, affliction, interruptable, conditional);
        return (AoeAllyEffect) buildIEffect(aoeAllyEffect, duration, name, description, url, visible, stacks, statMods, quality, condition);
    }

    protected Effect buildIEffect(Effect e, int duration, String name, String description, String url, boolean visible, boolean stacks, Map<String, Integer> statMods, String quality, String condition) {
        e.setName(name);
        e.setDescription(description);
        e.setAvatarUrl(url);
        e.setDuration(duration);
        e.setCondition(condition);
        if (statMods != null) {
            e.setStatMods(statMods);
        }
        e.setQuality(quality);
        e.setVisible(visible);
        e.setStacks(stacks);
        return e;
    }

    protected Ability buildIAbility(int position, boolean enemies, boolean allies, boolean self, boolean aoe, List<String> cost, int cooldown, String name, String url, String description, List<EnemyEffect> effects, List<SelfEffect> selfEffects, List<AllyEffect> allyEffects, List<AoeEnemyEffect> aoeEnemyEffects, List<AoeAllyEffect> aoeAllyEffects) {
        Ability a = new Ability(enemies, allies, self, aoe);
        a.setCooldown(cooldown);
        a.setName(name);
        a.setAbilityUrl(url);
        a.setCost(cost);
        if (effects != null) {
            effects.forEach(a::addEnemyEffect);
        }

        if (selfEffects != null) {
            selfEffects.forEach(a::addSelfEffect);
        }

        if (allyEffects != null) {
            allyEffects.forEach(a::addAllyEffect);
        }

        if (aoeEnemyEffects != null) {
            aoeEnemyEffects.forEach(a::addAoeEnemyEffect);
        }

        if (aoeAllyEffects != null) {
            aoeAllyEffects.forEach(a::addAoeAllyEffect);
        }
        a.setDescription(description);
        a.setTargets();
        a.setTypes();
        a.setPosition(position);
        return a;
    }

    protected HashMap<String, Integer> buildStat(String a, Integer b) {
        HashMap<String, Integer> map = new HashMap<>();
        map.put(a, b);
        return map;
    }

    @Transactional
    protected void makeOutsiders() {
        makeAlex();
        makeFainne();
        makeShinzo();
        makeHollyanna();
        makeGeddy();
        makeTristane();
        makeDrundar();
    }

    protected Character makeCharacter(String name, String description, String avatarUrl, List<String> factions, List<Ability> abilities) {
        Character c = new Character(avatarUrl, name, description, factions);
        c.addAbilities(abilities);
        c = this.characterService.create(c);
        return c;
    }

    protected Character makeAlex() {
        // EFFECTS 1
        EnemyEffect effects = buildEnemyEffect(3, "Impulse", "This unit is taking 10 magic damage", "/assets/alex1.png", false, true, false, true, false, true, false, buildStat(Stat.DAMAGE, 10), null, null);

        // ABILITY 1
        Ability ability = buildIAbility(0, true, false, false, false, Cost.oneRan, 4, "Impulse", "/assets/alex1.png", "Alex uses Impulse to attack one enemy, dealing 10 points of magic damage for 3 turns.", List.of(effects), null, null, null, null);

        // EFFECTS 2
        EnemyEffect effects1 = buildEnemyEffect(1, "Flash Freeze", "This unit took damage from Flash Freeze", "/assets/alex2.png", false, false, true, false, false, true, false, buildStat(Stat.DAMAGE, 20), null, null);
        EnemyEffect effects2 = buildEnemyEffect(2, "Flash Frozen", "This unit will do 10 less damage", "/assets/alex2.png", false, false, true, false, false, true, false, buildStat(Stat.DAMAGE_OUT, -10), null, null);

        // ABILITY 2
        Ability ability2 = buildIAbility(1, true, false, false, false, Cost.oneDex, 2, "Flash Freeze", "/assets/alex2.png", "Alex freezes one enemy with Flash Freeze, dealing 20 affliction damage, and reducing their damage by 10 for 2 turns", List.of(effects1, effects2), null, null, null, null);

        // EFFECTS 3
        EnemyEffect effects3 = buildEnemyEffect(2, "Study Target", "This unit takes 10 more damage from all sources", "/assets/alex3.png", true, false, false, false, false, true, false, buildStat(Stat.DAMAGE_IN, 10), null, null);
        EnemyEffect effects4 = buildEnemyEffect(1, "Study Target", "This unit's traps have been revealed", "/assets/alex3.png", true, false, false, false, false, true, false, null, Quality.REVEALED, null);

        // ABILITY 3
        Ability ability3 = buildIAbility(2, true, false, false, false, Cost.twoRan, 3, "Study Target", "/assets/alex3.png", "Alex reveals and disables any hidden skills on the target, and increases the damage target takes from all sources by 10 for 2 turns.", List.of(effects3, effects4), null, null, null, null);

        // EFFECTS 4
        SelfEffect effects5 = buildSelfEffect(1, "Stealth", "Alex is invunerable", "/assets/alex4.png", true, false, false, false, false, true, false, null, Quality.INVULNERABLE, null);

        // ABILITY 4
        Ability ability4 = buildIAbility(3, false, false, true, false, Cost.oneRan, 4, "Stealth", "/assets/alex4.png", "Alex hides for a turn, going invulnerable", null, List.of(effects5), null, null, null);

        return this.makeCharacter("Alex Drake", "alexDesc", "/assets/alex.png", List.of(Faction.ALEX, Faction.OUTSIDERS), List.of(ability, ability2, ability3, ability4));
    }

    protected Character makeFainne() {
        // EFFECTS 1
        AllyEffect effect = buildAllyEffect(2, "Unyielding Spirit", "This unit will gain 1 Random Energy if attacked", "/assets/fainne1.png", false, true, false, true, true, false, false, buildStat(Stat.ENERGY_CHANGE, 1), null, Conditional.TARGET_WAS(Conditional.ATTACKED));

        // ABILITY 1
        Ability ability = buildIAbility(0, false, true, true, false, Cost.oneDiv, 3, "Unyielding Spirit", "/assets/fainne1.png", "Fainne's unyielding spirit supplies 1 random energy to an ally or herself if they are attacked, for 2 turns", null, null, List.of(effect), null, null);

        // EFFECTS 2
        EnemyEffect effect1 = buildEnemyEffect(4, "Call Lightning", "This unit is taking 5 damage", "/assets/fainne2.png", false, true, false, true, false, true, false, buildStat(Stat.DAMAGE, 5), Quality.AFFECTED_BY("Call Lightning"), null);

        // ABILITY 2
        Ability ability2 = buildIAbility(1, true, false, false, false, Cost.oneRan, 1, "Call Lightning", "/assets/fainne2.png", "Fainne summons bolts of lighting from above, dealing 5 damage to one enemy for four turns.", List.of(effect1), null, null, null, null);

        // EFFECTS 3
        EnemyEffect effect2 = buildEnemyEffect(1, "Zahra", "This unit took extra damage from Zahra", "/assets/fainne3.png", true, false, false, false, true, true, false, buildStat(Stat.BONUS_DAMAGE, 10), null, Conditional.TARGET_AFFECTED_BY("Call Lightning"));
        EnemyEffect effect3 = buildEnemyEffect(1, "Zahra", "This unit took damage from Zahra", "/assets/fainne3.png", true, false, false, false, false, true, false, buildStat(Stat.DAMAGE, 20), null, null);

        // ABILITY 3
        Ability ability3 = buildIAbility(2, true, false, false, false, Cost.oneStr, 1, "Zahra", "/assets/fainne3.png", "Fainee lets loose Zahra to maim one enemy for 20 dmg.  10 extra damage if target is under the effects of Call Lightning", List.of(effect2, effect3), null, null, null, null);

        // EFFECTS 4
        SelfEffect effect4 = buildSelfEffect(1, "Irethis", "Fainne calls for Irethis' aid, going invulnerable", "/assets/fainne4.png", true, false, false, false, false, true, false, null, Quality.INVULNERABLE, null);

        // ABILITY 4
        Ability ability4 = buildIAbility(3, false, false, true, false, Cost.oneRan, 4, "Irethis", "/assets/fainne4.png", "Fainne calls for Irethis' aid, going invulnerable", null, List.of(effect4), null, null, null);

        return this.makeCharacter("Fainne", "fainneDesc", "/assets/fainne.png", List.of(Faction.FAINNE, Faction.OUTSIDERS), List.of(ability, ability2, ability3, ability4));
    }

    protected Character makeShinzo() {
        // EFFECTS 1
        EnemyEffect effect = buildEnemyEffect(1, "Trident", "Shinzo has dealt physical damage with his trident", "/assets/shinzo1.png", true, false, false, false, false, true, false, buildStat(Stat.DAMAGE, 25), null, null);
        EnemyEffect effect1 = buildEnemyEffect(1, "Trident", "Shinzo has dealt magical damage with his trident", "/assets/shinzo1.png", false, true, false, false, false, true, false, buildStat(Stat.DAMAGE, 15), null, null);

        // ABILITY 1
        Ability ability = buildIAbility(0, true, false, false, false, Cost.oneStrOneArc, 2, "Trident", "/assets/shinzo1.png", "Shinzo brings the storm with his trident, doing 25 physical damage and 15 magical damage", List.of(effect, effect1), null, null, null, null);

        // EFFECTS 2
        EnemyEffect effect2 = buildEnemyEffect(1, "Entangle", "This unit is taking 15 damage", "/assets/shinzo2.png", false, true, false, false, false, true, false, buildStat(Stat.DAMAGE, 15), null, null);
        EnemyEffect effect3 = buildEnemyEffect(1, "Entangle", "This unit is stunned", "/assets/shinzo2.png", false, true, false, false, false, true, false, null, Quality.STUNNED, null);

        // ABILITY 2
        Ability ability2 = buildIAbility(1, true, false, false, false, Cost.oneDiv, 4, "Entangle", "/assets/shinzo2.png", "Shinzo entangles an enemy doing 15 damage, and stunning them", List.of(effect2, effect3), null, null, null, null);

        // EFFECTS 3
        AoeAllyEffect effect4 = buildAoeAllyEffect(1, "Channel Energy", "This unit is healing 20 HP", "/assets/shinzo3.png", false, true, false, false, false, true, false, buildStat(Stat.DAMAGE, -20), null, null);

        // ABILITY 3
        Ability ability3 = buildIAbility(2, false, true, true, true, Cost.oneDivOneRan, 4, "Channel Energy", "/assets/shinzo3.png", "Shinzo calls upon his divine power and heals his party for 20 hp", null, null, null, null, List.of(effect4));

        // EFFECTS 4
        SelfEffect effect5 = buildSelfEffect(1, "Captain's Orders", "Shinzo's the Captain.  He's invulnerable this turn.", "/assets/shinzo4.png", true, false, false, false, false, true, false, null, Quality.INVULNERABLE, null);

        // ABILITY 4
        Ability ability4 = buildIAbility(3, false, false, true, false, Cost.oneRan, 4, "Captain's Orders", "/assets/shinzo4.png", "Shinzo call's upon his status among the seas to become invulnerable this turn.", null, List.of(effect5), null, null, null);

        return this.makeCharacter("Shinzo Katetsu", "shinzoDesc", "/assets/shinzo.png", List.of(Faction.SHINZO, Faction.OUTSIDERS), List.of(ability, ability2, ability3, ability4));
    }

    protected Character makeHollyanna() {
        // EFFECTS 1
        EnemyEffect effect = buildEnemyEffect(1, "Sneak Attack", "Sneak Attack Damage", "/assets/holly1.png", true, false, false, false, false, true, false, buildStat(Stat.DAMAGE, 25), null, null);

//	  effects = buildEffects(effects, 1,
//			  "Sneak Attack", "Sneak Attack Bonus Damage",  "/assets/holly1.png",
//			  true, false, false, false, true, true, false,
//			  buildStat(Stat.BONUS_DAMAGE, 10), null, Conditional.TARGET_HAS_QUALITY(Quality.STUNNED));

        // ABILITY 1
        Ability ability = buildIAbility(0, true, false, false, false, Cost.oneRan, 2, "Sneak Attack", "/assets/holly1.png", "Hollyanna hides in the shadows, and then strikes, dealing 25 damage.", List.of(effect), null, null, null, null);

        // EFFECTS 2
        EnemyEffect effect1 = buildEnemyEffect(1, "Eliminate", "Hollyanna uses raw force to inflict a mortal wound.", "/assets/holly2.png", true, false, false, false, false, true, false, buildStat(Stat.DAMAGE, 40), null, null);
        SelfEffect selfEffect1 = buildSelfEffect(3, "Eliminate - Bloodbath", "Hollyanna is being baptised in blood, Fulfill Contract is empowered until the end of next turn.", "/assets/holly2.png", true, false, false, false, true, true, false, null, Quality.AFFECTED_BY("Bloodbath"), Conditional.USER_DID(Conditional.KILL));

        // ABILITY 2
        Ability ability2 = buildIAbility(1, true, false, true, false, Cost.oneStrOneDex, 2, "Eliminate", "/assets/holly2.png", "Hollyanna uses raw force to inflict a mortal wound, dealing 40 damage.  If this skill kills an enemy, Fulfill Contract is empowered for 2 turns.", List.of(effect1), List.of(selfEffect1), null, null, null);

        // EFFECTS 3
        AoeEnemyEffect effect2 = buildAoeEnemyEffect(1, "Fulfill Contract", "Take part in an evil deed, stunning all enemies for one turn.", "/assets/holly3.png", false, true, false, false, false, true, false, null, Quality.STUNNED, null);
        SelfEffect selfEffect2 = buildSelfEffect(1, "Fulfill Contract", "Hollyanna is untargetable.", "/assets/holly3.png", false, true, false, false, true, true, false, buildStat(Stat.DAMAGE, -40), null, Conditional.USER_AFFECTED_BY("Bloodbath"));
        SelfEffect selfEffect3 = buildSelfEffect(1, "Fulfill Contract", "Hollyanna is untargetable.", "/assets/holly3.png", false, true, false, false, true, true, false, null, Quality.UNTARGETABLE, Conditional.USER_AFFECTED_BY("Bloodbath"));
        SelfEffect selfEffect4 = buildSelfEffect(-1, "Fulfill Contract", "If this skill is used 3 times, Hollyanna dies.", "/assets/holly3.png", false, true, false, false, false, true, true, null, Quality.AFFECTED_BY("Fulfill Contract"), null);
        SelfEffect selfEffect5 = buildSelfEffect(0, "Fulfill Contract", "If this skill is used 3 times, Hollyanna dies.", "/assets/holly3.png", false, true, false, false, true, true, false, buildStat(Stat.TRUE_DAMAGE, 100), null, Conditional.USER_AFFECTED_BY("Fulfill Contract", 3));

        // ABILITY 3
        Ability ability3 = buildIAbility(2, true, false, true, true, Cost.twoDiv, 2, "Fulfill Contract", "/assets/holly3.png", "Take part in an evil deed, stunning all enemies for one turn.  If Bloodbath is active, heal 40 hp, and become untargetable for one turn.  If this skill is used 3 times, Hollyanna dies.", null, List.of(selfEffect2, selfEffect3, selfEffect4, selfEffect5), null, List.of(effect2), null);

        // EFFECTS 4
        SelfEffect effect3 = buildSelfEffect(1, "Shadow Illusion", "This unit is invulnerable.", "/assets/holly4.png", true, false, false, false, false, true, false, null, Quality.INVULNERABLE, null);

        // ABILITY 4
        Ability ability4 = buildIAbility(3, false, false, true, false, Cost.oneRan, 4, "Shadow Illusion", "/assets/holly4.png", "Hollyanna hides in shadow, going invulnerable for 1 turn", null, List.of(effect3), null, null, null);

        return this.makeCharacter("Hollyanna Knox", "hollyDesc", "/assets/holly.png", List.of(Faction.HOLLYANNA, Faction.OUTSIDERS), List.of(ability, ability2, ability3, ability4));
    }

    protected Character makeGeddy() {
        // EFFECTS 1
        AoeEnemyEffect effect = buildAoeEnemyEffect(1, "Darts", "This unit took damage from darts", "/assets/geddy1.png", true, false, false, false, false, true, false, buildStat(Stat.DAMAGE, 15), null, null);

        // ABILITY 1
        Ability ability = buildIAbility(0, true, false, false, true, Cost.oneDex, 1, "Darts", "/assets/geddy1.png", "Geddy tosses darts at all enemies, dealing 15 damage", null, null, null, List.of(effect), null);

        // EFFECTS 2
        AllyEffect effect1 = buildAllyEffect(2, "Inspiration", "This unit will gain 10 shields.", "/assets/geddy2.png", false, true, false, true, false, true, false, buildStat(Stat.SHIELD_GAIN, 10), null, null);

        // ABILITY 2
        Ability ability2 = buildIAbility(1, false, true, false, false, Cost.free, 4, "Inspiration", "/assets/geddy2.png", "Geddy inspires an ally for 2 turns, giving them 10 shields each turn.", null, null, List.of(effect1), null, null);

        // EFFECTS 3
        EnemyEffect effect2 = buildEnemyEffect(2, "Geas", "This unit is following their Geas, and cannot become invulnerable.", "/assets/geddy3.png", false, true, false, false, false, true, false, null, Quality.VULNERABLE, null);
        EnemyEffect effect3 = buildEnemyEffect(2, "Geas", "This unit is following their Geas, and is stunned.", "/assets/geddy3.png", false, true, false, false, false, true, false, null, Quality.STUNNED, null);

        // ABILITY 3
        Ability ability3 = buildIAbility(2, true, false, false, false, Cost.twoArc, 4, "Geas", "/assets/geddy3.png", "Geddy gives one unit a quest, causing them to be stunned and unable to become invulnerable for 2 turns.", List.of(effect2, effect3), null, null, null, null);

        // EFFECTS 4
        SelfEffect effect4 = buildSelfEffect(1, "Distracting Performance", "Geddy is being loud.  He's invulnerable.", "/assets/geddy4.png", true, false, false, false, false, true, false, null, Quality.INVULNERABLE, null);

        // ABILITY 4
        Ability ability4 = buildIAbility(3, false, false, true, false, Cost.oneRan, 4, "Distracting Performance", "/assets/geddy4.png", "Geddy makes a scene, going invulnerable", null, List.of(effect4), null, null, null);

        return this.makeCharacter("Geddy Splintwalker", "geddyDesc", "/assets/geddy.png", List.of(Faction.GEDDY, Faction.OUTSIDERS), List.of(ability, ability2, ability3, ability4));
    }

    protected Character makeTristane() {
        // phys / mag / affl / inter / cond / vis / stack
        // EFFECTS 1
        EnemyEffect effect = buildEnemyEffect(1, "Backstab", "This unit took damage from backstab", "/assets/tristane1.png", true, false, false, false, false, true, false, buildStat(Stat.DAMAGE, 40), null, null);

        // ABILITY 1
        Ability ability = buildIAbility(0, true, false, false, false, Cost.threeRan, 1, "Backstab", "/assets/tristane1.png", "Tristane sneaks behind an enemy, stabbing them in the back for 40 physical damage.", List.of(effect), null, null, null, null);

        // EFFECTS 2
        SelfEffect effect1 = buildSelfEffect(4, "Guildmaster", "Tristane's costs have been reduced by one.", "/assets/tristane2.png", true, false, false, false, false, true, false, buildStat(Stat.COST_CHANGE, -1), null, null);

        // ABILITY 2
        Ability ability2 = buildIAbility(1, false, false, true, false, Cost.twoRan, 1, "Guildmaster", "/assets/tristane2.png", "Tristane calls in backup, reducing the costs of his abilities by one for three turns.", null, List.of(effect1), null, null, null);

        // EFFECTS 3
        EnemyEffect effect2 = buildEnemyEffect(1, "Scheme", "If this unit uses a damaging skill, it will be countered.", "/assets/tristane3.png", true, false, false, false, true, false, false, null, Quality.COUNTERED, Conditional.TARGET_DID(Conditional.DAMAGE));

        // ABILITY 3
        Ability ability3 = buildIAbility(2, true, false, false, false, Cost.twoRan, 4, "Scheme", "/assets/tristane3.png", "Tristane schemes against a unit, countering any damaging skill for one turn.", List.of(effect2), null, null, null, null);

        // EFFECTS 4
        SelfEffect effect3 = buildSelfEffect(1, "Untrackable", "This unit is invulnerable.", "/assets/tristane4.png", true, false, false, false, false, true, false, null, Quality.INVULNERABLE, null);

        // ABILITY 4
        Ability ability4 = buildIAbility(3, false, false, true, false, Cost.oneRan, 4, "Untrackable", "/assets/tristane4.png", "Tristane goes invulnerable, unable to be found.", null, List.of(effect3), null, null, null);

        return this.makeCharacter("Guildmaster Tristane", "tristaneDesc", "/assets/tristane.png", List.of(Faction.TRISTANE), List.of(ability, ability2, ability3, ability4));
    }

    //Drundar
//
//CD: 1 - Hammer Down - Piercing damage
//CD: 4 - Build Up - put shields or armor on self
//CD: 4 - Quake - aoe dmg, and shield destruction
//CD: 4 - Call The Guard - Go invulnerable for one turn
    protected Character makeDrundar() {
        // phys / mag / affl / inter / cond / vis / stack
        // EFFECTS 1
        EnemyEffect effect = buildEnemyEffect(1, "Hammer Down", "This unit took damage from hammer down", "/assets/drundar1.png", true, false, false, false, false, true, false, buildStat(Stat.PIERCING_DAMAGE, 35), null, null);

        // ABILITY 1
        Ability ability = buildIAbility(0, true, false, false, false, Cost.oneStrOneRan, 1, "Hammer Down", "/assets/drundar1.png", "Drundar uses his magical hammer to deal 35 piercing damage.", List.of(effect), null, null, null, null);

        // EFFECTS 2
        SelfEffect effect1 = buildSelfEffect(3, "Manor Security", "Drundar has 50% physical armor.", "/assets/drundar2.png", true, false, false, false, false, true, false, buildStat(Stat.PHYSICAL_ARMOR, 50), null, null);

        // ABILITY 2
        Ability ability2 = buildIAbility(1, false, false, true, false, Cost.oneRan, 1, "Manor Security", "/assets/drundar2.png", "Drundar beefs up on security, taking 50% less physical damage for 3 turns", null, List.of(effect1), null, null, null);

        // EFFECTS 3
        AoeEnemyEffect effect2 = buildAoeEnemyEffect(1, "Quake", "This unit has taken damage and lost all shields from quake.", "/assets/drundar3.png", false, true, false, false, false, true, false, buildStat(Stat.SHIELD_GAIN, -9999), null, null);
        AoeEnemyEffect effect3 = buildAoeEnemyEffect(1, "Quake", "This unit has taken damage and lost all shields from quake.", "/assets/drundar3.png", false, true, false, false, false, true, false, buildStat(Stat.DAMAGE, 20), null, null);

        // ABILITY 3
        Ability ability3 = buildIAbility(2, true, false, false, true, Cost.oneStrOneArc, 4, "Quake", "/assets/drundar3.png", "Drundar creates a magical quake, destroying all enemy shields and doing 20 damage to all enemies.", null, null, null, List.of(effect2, effect3), null);

        // EFFECTS 4
        SelfEffect effect4 = buildSelfEffect(1, "Brotherly Bond", "This unit is invulnerable.", "/assets/drundar4.png", true, false, false, false, false, true, false, null, Quality.INVULNERABLE, null);

        // ABILITY 4
        Ability ability4 = buildIAbility(3, false, false, true, false, Cost.oneRan, 4, "Brotherly Bond", "/assets/drundar4.png", "Drundar goes invulnerable.", null, List.of(effect4), null, null, null);

        return this.makeCharacter("Drundar Earthbreaker", "drundarDesc", "/assets/drundar.png", List.of(Faction.DRUNDAR), List.of(ability, ability2, ability3, ability4));
    }
//
//  Kalio
//
//  CD: 1 - Mage Armor - Give ally or self armor and reflect damage
//  CD: 4 - Dispel - remove magical effects
//  CD: 4 - Research - reroll energy hanatarou style
//  CD: 4 - A+ - Go invulnerable for one turn
//
//  Ralion
//
//  CD: 1 - Extort - increase enemy costs
//  CD: 4 - Lightning Bolt - hard dmg
//  CD: 4 - Arrogant Generosity - heal enemy for 10 hp, lower enemy damage, or/and stun skills
//  CD: 4 - Ultimatum - Go invulnerable for one turn
//
//  Imperia
//
//  CD: 1 - Sneaky Jump - Counter skills used on her
//  CD: 4 - Stunning Fist - stun and dmg, if sneak jump succeeded then bonus
//  CD: 4 - Flurry of Blows - dot, if sneak jump succeeded then bonus
//  CD: 4 - 1 Random - Go invulnerable for one turn
//
//  Millor
//
//  CD: 1 - Hold Hostage - Stun an enemy's physical skills and put shields on self
//  CD: 4 - Ice Trap - put counter on enemy, do damage to them and counter if they use a damaging skill
//  CD: 4 - Mutilate - dmg and increase costs on one unit
//  CD: 4 - 1 Random - Go invulnerable for one turn
}
