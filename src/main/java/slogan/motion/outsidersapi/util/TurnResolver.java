package slogan.motion.outsidersapi.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import slogan.motion.outsidersapi.domain.constant.Conditional;
import slogan.motion.outsidersapi.domain.constant.Energy;
import slogan.motion.outsidersapi.domain.constant.Quality;
import slogan.motion.outsidersapi.domain.constant.Stat;
import slogan.motion.outsidersapi.domain.dto.TargetCheckDTO;
import slogan.motion.outsidersapi.domain.dto.TurnEndDTO;
import slogan.motion.outsidersapi.domain.jpa.Ability;
import slogan.motion.outsidersapi.domain.jpa.Battle;
import slogan.motion.outsidersapi.domain.jpa.Combatant;
import slogan.motion.outsidersapi.domain.jpa.effects.BattleEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static slogan.motion.outsidersapi.util.NRG.*;

@Slf4j
public class TurnResolver {

    private List<Combatant> playerOneTeam = new ArrayList<>();
    private List<Combatant> playerTwoTeam = new ArrayList<>();
    private List<Combatant> team = new ArrayList<>();
    private List<Combatant> enemyTeam = new ArrayList<>();
    private Map<String, Integer> energy = new HashMap<>();
    private Map<String, Integer> enemyEnergy = new HashMap<>();
    private Map<String, Integer> spentEnergy = new HashMap<>();
    private List<TargetCheckDTO> moves = new ArrayList<>();
    private List<BattleEffect> effects = new ArrayList<>();
    private boolean isPlayerOne = false;

    private List<BattleEffect> currentTargetEffects = new ArrayList<>();
    private List<BattleEffect> currentFromEffects = new ArrayList<>();

    private boolean anyEnemyHasCounter = false;
    private boolean anyAllyHasCounter = false;
    private boolean iHaveCounterFromEnemy = false;
    private boolean enemyHasCounter = false;
    private boolean allyHasCounter = false;
    private boolean counterIsTriggered = false;

    private Battle battle = null;
    private TurnEndDTO turnEndDTO = null;

    private BattleEffect myCounter = null;
    private BattleEffect theirCounter = null;
    private BattleEffect ourCounter = null;

    private BattleEffect effect = null;
    private Combatant targetCharacter = null;
    private Combatant fromCharacter = null;
    private Integer randomInt = 0;
    private Integer finalDamage = 0;

    private boolean firstCast = false;
    private boolean hiddenPass = false;
    private boolean isVisible = false;
    private boolean isConditional = false;
    private boolean satisfiesConditional = false;
    private boolean isInterruptable = false;
    private boolean shouldSkipHidden = false;
    private final boolean isNewEffect = false;
    private boolean isTargetingEnemy = false;
    private boolean interrupted = false;
    private boolean hasMods = false;
    private boolean isEnergyChange = false;
    private boolean isRandomChange = false;
    private boolean isStrengthChange = false;
    private boolean isDexterityChange = false;
    private boolean isArcanaChange = false;
    private boolean isDivinityChange = false;
    private boolean isDmg = false;
    private boolean isHPChange = false;
    private boolean isHeal = false;
    private boolean isShield = false;
    private boolean isStun = false;
    private boolean isPhysStun = false;
    private boolean isMagStun = false;
    private boolean isReveal = false;
    private boolean isDmgOutIn = false;
    private boolean isPhysDmgOutIn = false;
    private boolean isMagDmgOutIn = false;
    private boolean isAffDmgOutIn = false;
    private boolean isDmgMod = false;
    private boolean passesConditional = false;
    private boolean isStunned = false;
    private boolean isPhysStunned = false;
    private boolean isMagStunned = false;
    private boolean isShieldChange = false;
    private boolean isShieldGain = false;
    private boolean isShieldLoss = false;
    private boolean isAffliction = false;
    private boolean shieldsApplied = false;
    private boolean targetInvulnerable = false;
    private boolean targetVulnerable = false;
    private boolean isDamagable = true;
    private boolean hasQuality = false;
    private boolean hasStatMods = false;
    private boolean nonDmg = true;
    private boolean atkFlag = false;
    private boolean dmgFlag = false;
    private boolean bufFlag = false;
    private boolean debFlag = false;
    private boolean kilFlag = false;
    private int shields = 0;
    private int gainAmt = 0;
    private int lossAmt = 0;
    private int totalIn = 0;
    private int totalAr = 0;

    private void checkForPrescenceOfCounters(Combatant self, List<Integer> targetPositions) {
        for (BattleEffect e : self.getEffects()) {
            boolean isFriendlyEffect = (e.getOriginCharacter() > 2 && self.getPosition() > 2)
                    || (e.getOriginCharacter() < 3 && self.getPosition() < 3);
            boolean isNotVisible = !e.isVisible();
            boolean isCounter = Quality.COUNTERED.equals(e.getQuality());
            if (isCounter && isNotVisible && !isFriendlyEffect) {
                iHaveCounterFromEnemy = true;
                myCounter = e;
            }
        }

        for (Combatant ally : team) {
            for (BattleEffect e : ally.getEffects()) {
                if (allyHasCounter) {
                    break;
                }
                boolean isFriendlyEffect = (e.getOriginCharacter() > 2 && self.getPosition() > 2)
                        || (e.getOriginCharacter() < 3 && self.getPosition() < 3);
                boolean isNotVisible = !e.isVisible();
                boolean isCounter = Quality.COUNTERED.equals(e.getQuality());
                if (isCounter && isNotVisible && !isFriendlyEffect) {
                    if (targetPositions.contains(ally.getPosition())) {
                        allyHasCounter = true;
                        ourCounter = e;
                    }
                    anyAllyHasCounter = true;
                    ourCounter = e;
                }
            }
        }

        for (Combatant enemy : enemyTeam) {
            for (BattleEffect e : enemy.getEffects()) {
                if (enemyHasCounter) {
                    break;
                }
                boolean isFriendlyEffect = (e.getOriginCharacter() > 2 && self.getPosition() > 2)
                        || (e.getOriginCharacter() < 3 && self.getPosition() < 3);
                boolean isNotVisible = !e.isVisible();
                boolean isCounter = Quality.COUNTERED.equals(e.getQuality());
                if (isCounter && isNotVisible && !isFriendlyEffect) {
                    if (targetPositions.contains(enemy.getPosition())) {
                        enemyHasCounter = true;
                        theirCounter = e;
                    }
                    anyEnemyHasCounter = true;
                    theirCounter = e;
                }
            }
        }
    }

    private boolean isOriginCounterTriggered(BattleEffect counter, Ability a) {
        boolean counterIsTriggered = true;
        if (counter.isConditional()) {
            counterIsTriggered = false;
            String flagName = counter.getCondition().split("_")[2];
            if (Conditional.DAMAGE.equals(flagName) || Conditional.ATTACK.equals(flagName)) {
                counterIsTriggered = a.isDamaging();
            } else if (Conditional.BUFF.equals(flagName)) {
                counterIsTriggered = a.isBuff();
            } else if (Conditional.DEBUFF.equals(flagName)) {
                counterIsTriggered = a.isDebuff();
            } else if (Conditional.PHYSICAL.equals(flagName)) {
                counterIsTriggered = a.isPhysical();
            } else if (Conditional.MAGICAL.equals(flagName)) {
                counterIsTriggered = a.isMagical();
            } else if (Conditional.AFFLICTION.equals(flagName)) {
                counterIsTriggered = a.isAffliction();
            }
        }

        if (counterIsTriggered) {
            counter.triggerAndRevealCounter(a);
        }

        return counterIsTriggered;
    }

    private boolean isTargetCounterTriggered(BattleEffect counter, Ability a) {
        boolean counterIsTriggered = true;
        if (counter.isConditional()) {
            counterIsTriggered = false;
            String flagName = counter.getCondition().split("_")[2];
            if (Conditional.DAMAGED.equals(flagName) || Conditional.ATTACKED.equals(flagName)) {
                counterIsTriggered = a.isDamaging();
            } else if (Conditional.BUFFED.equals(flagName)) {
                counterIsTriggered = a.isBuff();
            } else if (Conditional.DEBUFFED.equals(flagName)) {
                counterIsTriggered = a.isDebuff();
            } else if (Conditional.PHYSICAL.equals(flagName)) {
                counterIsTriggered = a.isPhysical();
            } else if (Conditional.MAGICAL.equals(flagName)) {
                counterIsTriggered = a.isMagical();
            } else if (Conditional.AFFLICTION.equals(flagName)) {
                counterIsTriggered = a.isAffliction();
            }
        }

        if (counterIsTriggered) {
            counter.triggerAndRevealCounter(a);
        }

        return counterIsTriggered;
    }

    private boolean isNewEffect(BattleEffect e) {
        return e.getInstanceId() <= 0;
    }

    private boolean isEnemyEffect(BattleEffect e) {
        return ((isPlayerOne && e.getOriginCharacter() > 2) || (!isPlayerOne && e.getOriginCharacter() < 3));
    }

    private boolean isHiddenEnemyEffect(BattleEffect e) {
        return !e.isVisible() && isEnemyEffect(e);
    }

    private boolean statModExists(String stat) {
        Map<String, Integer> stats = effect.getStatMods();
        if (CollectionUtils.isEmpty(stats)) {
            return false;
        } else {
            return effect.getStatMods().get(stat) != null;
        }
    }

    private boolean effectResolves() {

        checkIfDamagable();

        checkIfInterrupted();

        checkIfConditional();

        return satisfiesConditional && !interrupted && !shouldSkipHidden;
    }

    private void extractBattle(Battle incomingBattle, TurnEndDTO dto, boolean playerOne) {
        battle = incomingBattle;
        turnEndDTO = dto;
        // first time this happens is the record of the "end" of turn 1
        battle.setTurn(battle.getTurn() + 1);
        isPlayerOne = playerOne;
        spentEnergy = dto.getSpentEnergy();
        moves = dto.getAbilities();
        effects = dto.getEffects();
        playerOneTeam = battle.getPlayerOneTeam();
        playerTwoTeam = battle.getPlayerTwoTeam();
        if (isPlayerOne) {
            team = playerOneTeam;
            enemyTeam = playerTwoTeam;
            energy = battle.getPlayerOneEnergy();
            enemyEnergy = battle.getPlayerTwoEnergy();
        } else {
            team = playerTwoTeam;
            enemyTeam = playerOneTeam;
            energy = battle.getPlayerTwoEnergy();
            enemyEnergy = battle.getPlayerOneEnergy();
        }
    }

    private void extractValues(BattleEffect currentEffect, Combatant tarChar, Combatant fromChar,
                               boolean isHiddenPass, boolean isFirstCast) {
        effect = currentEffect;
        targetCharacter = tarChar;
        fromCharacter = fromChar;
        hiddenPass = isHiddenPass;
        firstCast = isFirstCast;
        currentTargetEffects = targetCharacter.getEffects();
        currentFromEffects = fromCharacter.getEffects();
        isVisible = effect.isVisible();
        isConditional = effect.isConditional();
        isInterruptable = effect.isInterruptable();
        shouldSkipHidden = !hiddenPass && !isVisible;

        extractStats();
    }

    private void extractStats() {
        hasStatMods = !CollectionUtils.isEmpty(effect.getStatMods());
        isHPChange = statModExists(Stat.DAMAGE);
        isHeal = isHPChange && effect.getStatMods().get(Stat.DAMAGE) < 0;
        isDmg = (isHPChange && !isHeal) || statModExists(Stat.PIERCING_DAMAGE) || statModExists(Stat.BONUS_DAMAGE)
                || statModExists(Stat.TRUE_DAMAGE);
        isAffliction = effect.isAffliction();

        isRandomChange = statModExists(Stat.ENERGY_CHANGE);
        isStrengthChange = statModExists(Stat.STRENGTH_CHANGE);
        isDexterityChange = statModExists(Stat.DEXTERITY_CHANGE);
        isArcanaChange = statModExists(Stat.ARCANA_CHANGE);
        isDivinityChange = statModExists(Stat.DIVINITY_CHANGE);

        isDmgOutIn = statModExists(Stat.DAMAGE_IN) || statModExists(Stat.DAMAGE_OUT);
        isPhysDmgOutIn = statModExists(Stat.PHYSICAL_DAMAGE_IN) || statModExists(Stat.PHYSICAL_DAMAGE_OUT);
        isMagDmgOutIn = statModExists(Stat.MAGICAL_DAMAGE_IN) || statModExists(Stat.MAGICAL_DAMAGE_OUT);
        isAffDmgOutIn = statModExists(Stat.AFFLICTION_DAMAGE_IN) || statModExists(Stat.AFFLICTION_DAMAGE_OUT);

        isShield = statModExists(Stat.SHIELDS);
        isShieldChange = statModExists(Stat.SHIELD_GAIN);

        nonDmg = !isDmg;

        isShieldGain = isShieldChange && effect.getStatMods().get(Stat.SHIELD_GAIN) > 0;
        isShieldLoss = isShieldChange && !isShieldGain;
        lossAmt = isShieldLoss ? effect.getStatMods().get(Stat.SHIELD_GAIN) : 0;

        isTargetingEnemy = (targetCharacter.getPosition() - fromCharacter.getPosition()) > 2;
        isEnergyChange = isRandomChange || isStrengthChange || isDexterityChange || isArcanaChange || isDivinityChange;
        isDmgMod = isDmgOutIn || isPhysDmgOutIn || isMagDmgOutIn || isAffDmgOutIn;

        atkFlag = isDmg;
        bufFlag = !isTargetingEnemy && nonDmg;
        debFlag = isTargetingEnemy && nonDmg;

        extractQuality();
    }

    private void extractQuality() {
        hasQuality = !isNullOrEmpty(effect.getQuality());
        isPhysStun = Quality.PHYSICAL_STUNNED.equals(effect.getQuality());
        isMagStun = Quality.MAGICAL_STUNNED.equals(effect.getQuality());
        isStun = Quality.STUNNED.equals(effect.getQuality()) || isPhysStun || isMagStun;
        isReveal = Quality.REVEALED.equals(effect.getQuality());
    }

    private void spendEnergy() {
        if (spentEnergy.get(Energy.STRENGTH) > 0 || spentEnergy.get(Energy.DEXTERITY) > 0
                || spentEnergy.get(Energy.ARCANA) > 0 || spentEnergy.get(Energy.DIVINITY) > 0) {
            spentEnergy.forEach((s, i) -> {
                int prev = energy.get(s);
                energy.put(s, prev - i);
            });
        }
    }

    private void resetFlagsAndRemoveOldEffects() {
        if (!isPlayerOne) {
            for (Combatant c : battle.getPlayerOneTeam()) {
                c.setFlags(new ArrayList<>());
                List<BattleEffect> currentEffects = c.getEffects();
                currentEffects.removeIf((BattleEffect e) -> (e.getOriginCharacter() < 3 && e.getDuration() == 999));
            }
            for (Combatant c : battle.getPlayerTwoTeam()) {
                c.setFlags(new ArrayList<>());
                List<BattleEffect> currentEffects = c.getEffects();
                currentEffects.removeIf((BattleEffect e) -> (e.getOriginCharacter() < 3 && e.getDuration() == 999));
            }
        } else {
            for (Combatant c : battle.getPlayerOneTeam()) {
                c.setFlags(new ArrayList<>());
                List<BattleEffect> currentEffects = c.getEffects();
                currentEffects.removeIf((BattleEffect e) -> (e.getOriginCharacter() > 2 && e.getDuration() == 999));
            }
            for (Combatant c : battle.getPlayerTwoTeam()) {
                c.setFlags(new ArrayList<>());
                List<BattleEffect> currentEffects = c.getEffects();
                currentEffects.removeIf((BattleEffect e) -> (e.getOriginCharacter() > 2 && e.getDuration() == 999));
            }
        }
    }

    private void setAbilitiesUsedOnCooldown() {
        for (TargetCheckDTO atDTO : turnEndDTO.getAbilities()) {
            Ability a = atDTO.getAbility();
            int abPos = a.getPosition();
            int cd = a.getCooldown();
            int charPos = atDTO.getCharacterPosition();
            int teamPos = charPos > 2 ? charPos - 3 : charPos;
            team.get(teamPos).getCooldowns().set(abPos, cd);
        }
    }

    private void drawNewEnergy() {
        if (battle.getTurn() != 1) {
            int count = 0;
            if (isPlayerOne) {
                for (Combatant c : battle.getPlayerTwoTeam()) {
                    if (!c.isDead()) {
                        count++;
                    } else {
                        c.clearEffects();
                    }
                }
                battle.drawPlayerTwoEnergy(count);
            } else {
                for (Combatant c : battle.getPlayerOneTeam()) {
                    if (!c.isDead()) {
                        count++;
                    } else {
                        c.clearEffects();
                    }
                }
                battle.drawPlayerOneEnergy(count);
            }
            count = 0;
        }
    }

    private void decrementCooldowns() {
        if (isPlayerOne) {
            for (Combatant c : playerOneTeam) {
                for (int i = 0; i < c.getCooldowns().size(); i++) {
                    int cd = c.getCooldowns().get(i);
                    if (cd > 0) {
                        c.getCooldowns().set(i, (cd - 1));
                    }
                }
            }
        } else {
            for (Combatant c : playerTwoTeam) {
                for (int i = 0; i < c.getCooldowns().size(); i++) {
                    int cd = c.getCooldowns().get(i);
                    if (cd > 0) {
                        c.getCooldowns().set(i, (cd - 1));
                    }
                }
            }
        }
    }

    public Battle handleTurns(Battle incomingBattle, TurnEndDTO dto, boolean playerOne) {
        log.info("------==TURN |START");
        extractBattle(incomingBattle, dto, playerOne);
        spendEnergy();

        applyAbilities();

        resetFlagsAndRemoveOldEffects();
        setAbilitiesUsedOnCooldown();
        drawNewEnergy();
        decrementCooldowns();
        return battle;
    }

    private void applyAbilities() {
        int newMoveCounter = 0;
        for (BattleEffect e : effects) {
            log.info("------==TURN |TARGET BEFORE: {}", targetCharacter);
            if (isNewEffect(e)) {
                log.info("------==TURN |APPLYING NEW ABILITY");
                applyNewAbility(newMoveCounter);
                newMoveCounter++;
            } else {
                log.info("------==TURN |APPLYING OLD ABILITY");
                applyOngoingEffects(e, false);
            }
            log.info("------==TURN |TARGET AFTER: {}", targetCharacter);
        }
        applyHiddenEffects();
    }

    private void applyNewAbility(int newMoveCounter) {
        TargetCheckDTO atDTO = moves.get(newMoveCounter);
        Ability a = atDTO.getAbility();
        int charPos = atDTO.getCharacterPosition();
        int teamPos = charPos > 2 ? charPos - 3 : charPos;
        Combatant self = team.get(teamPos);
        List<Integer> tarPos = atDTO.getTargetPositions();

        applyAbilityToTargets(a, tarPos, self);
    }

    private void applyAbilityToTargets(Ability a, List<Integer> targetPositions, Combatant self) {
        int groupInt = randomInt() + 1;
        randomInt = groupInt;
        checkForPrescenceOfCounters(self, targetPositions);
        triggerCounterOrApplyAbility(a, targetPositions, self);
    }

    private void triggerCounterOrApplyAbility(Ability a, List<Integer> targetPositions, Combatant self) {


        if (a.isAoe()) {
            if (a.isEnemy()) {
                if (anyEnemyHasCounter) {
                    counterIsTriggered = isTargetCounterTriggered(theirCounter, a);
                } else if (iHaveCounterFromEnemy) {
                    counterIsTriggered = isOriginCounterTriggered(myCounter, a);
                }
                // TODO: pass boolean so ability still gets set on CD? or something
                if (!counterIsTriggered) {
                    a.getAoeEnemyEffects().forEach(e -> {
                        applyEffectToCharacters(new BattleEffect(e), enemyTeam, self, targetPositions, true);
                    });
                }
            }
            if (a.isAlly()) {
                if (anyAllyHasCounter) {
                    counterIsTriggered = isOriginCounterTriggered(ourCounter, a);
                } else if (iHaveCounterFromEnemy) {
                    counterIsTriggered = isOriginCounterTriggered(myCounter, a);
                }
                if (!counterIsTriggered) {
                    a.getAoeAllyEffects().forEach(e -> {
                        applyEffectToCharacters(new BattleEffect(e), team, self, targetPositions, true);
                    });
                }
            }
        } else {
            if (a.isEnemy()) {
                if (anyEnemyHasCounter) {
                    counterIsTriggered = isTargetCounterTriggered(theirCounter, a);
                } else if (iHaveCounterFromEnemy) {
                    counterIsTriggered = isOriginCounterTriggered(myCounter, a);
                }
                if (!counterIsTriggered) {
                    a.getEnemyEffects().forEach(e -> {
                        applyEffectToCharacters(new BattleEffect(e), enemyTeam, self, targetPositions, true);
                    });
                }
            }
            if (a.isAlly()) {
                if (anyAllyHasCounter) {
                    counterIsTriggered = isOriginCounterTriggered(ourCounter, a);
                } else if (iHaveCounterFromEnemy) {
                    counterIsTriggered = isOriginCounterTriggered(myCounter, a);
                }
                if (!counterIsTriggered) {
                    a.getAllyEffects().forEach(e -> {
                        applyEffectToCharacters(new BattleEffect(e), team, self, targetPositions, true);
                    });
                }
            }
        }

        if (a.isSelf()) {
            if (iHaveCounterFromEnemy) {
                counterIsTriggered = isOriginCounterTriggered(myCounter, a);
            }
            if (!counterIsTriggered) {
                a.getSelfEffects().forEach(e -> {
                    log.info("------==TURN |TARGET BEFORE: {}", targetCharacter);
                    applyEffectToCharacter(new BattleEffect(e), self, self, false, true);
                });
            }
        }
    }

    private void applyOngoingEffects(BattleEffect e, boolean hidden) {
        int origin = e.getOriginCharacter();
        int target = e.getTargetCharacter();
        Combatant originCharacter = origin > 2 ? playerTwoTeam.get(origin - 3) : playerOneTeam.get(origin);
        Combatant targetCharacter = target > 2 ? playerTwoTeam.get(target - 3) : playerOneTeam.get(target);
        applyEffectToCharacter(e, targetCharacter, originCharacter, hidden, e.getInstanceId(), false);
    }

    private void applyHiddenEffects() {
        applyHiddenEffectsForTeam(team);
        applyHiddenEffectsForTeam(enemyTeam);
    }

    private void applyHiddenEffectsForTeam(List<Combatant> team) {
        for (Combatant c : team) {
            for (BattleEffect e : c.getEffects()) {
                if (isHiddenEnemyEffect(e)) {
                    log.info("------==TURN |APPLYING HIDDEN EFFECT");
                    applyOngoingEffects(e, true);
                }
            }
        }
    }

    private void applyEffectToCharacters(BattleEffect effect, List<Combatant> characters,
                                         Combatant fromCharacter, List<Integer> targetPositions, boolean firstCast) {

        for (Combatant c : characters) {
            if (targetPositions.contains(c.getPosition())) {
                applyEffectToCharacter(effect, c, fromCharacter, false, firstCast);
            }
        }
    }

    private void applyEffectToCharacter(BattleEffect effect, Combatant targetCharacter, Combatant fromCharacter,
                                        boolean isHiddenPass, boolean firstCast) {
        applyEffectToCharacter(effect, targetCharacter, fromCharacter, isHiddenPass, randomInt, firstCast);
    }

    private void applyEffectToCharacter(BattleEffect incomingEffect, Combatant targetCharacter,
                                        Combatant fromCharacter, boolean isHiddenPass, int randomInt, boolean firstCast) {


        extractValues(incomingEffect, targetCharacter, fromCharacter, isHiddenPass, firstCast);
        if (effectResolves()) {
            log.info("------==TURN |TARGET BEFORE: {}", targetCharacter);
            log.info("------==TURN |" + (firstCast ? "NEW" : "OLD") + "_EFFECT: " + incomingEffect.toString());
            log.info("------==TURN |USER: " + fromCharacter.getShorthand());
            log.info("------==TURN |TARGET: " + targetCharacter.getShorthand());
            addConditionalFlags();
            applyEffectLogic();
            if (isNewEffect(effect)) {
                addNewEffect();
            } else {
                progressOldEffect();
            }
            log.info("------==TURN |EFFECT RESOLVED");
        } else {
            log.info("------==TURN |EFFECT SKIPPED - DOES NOT RESOLVE");
        }
    }

    private void checkIfDamagable() {
        for (BattleEffect e : currentFromEffects) {
            if (Quality.STUNNED.equals(e.getQuality())) {
                isStunned = true;
            }
            if (Quality.PHYSICAL_STUNNED.equals(e.getQuality())) {
                isPhysStunned = true;
            }
            if (Quality.MAGICAL_STUNNED.equals(e.getQuality())) {
                isMagStunned = true;
            }
        }

        for (BattleEffect e : currentTargetEffects) {
            if (Quality.INVULNERABLE.equals(e.getQuality())) {
                targetInvulnerable = true;
            }
            if (Quality.VULNERABLE.equals(e.getQuality())) {
                targetVulnerable = true;
            }
        }

        isDamagable = !targetInvulnerable || (targetInvulnerable && targetVulnerable);
    }

    private void checkIfInterrupted() {
        if (isInterruptable) {
            if (isStunned) {
                interrupted = true;
            }
            if (effect.isPhysical() && isPhysStunned) {
                interrupted = true;
            }
            if (effect.isMagical() && isMagStunned) {
                interrupted = true;
            }
        }
    }

    private void checkIfConditional() {
        if (isConditional) {
            String condition = effect.getCondition();
            String[] conditions = condition.split("_");
            String character = conditions[0];
            String operator = conditions[1];
            Combatant thisChar;
            if ("TARGET".equals(character)) {
                thisChar = targetCharacter;
            } else {
                thisChar = fromCharacter;
            }

            if ("AFFECTEDBY".equals(operator)) {
                String effectName = conditions[2];
                if (conditions.length == 4) {
                    int stacks = Integer.parseInt(conditions[3]);
                    for (BattleEffect e : thisChar.getEffects()) {
                        if (Quality.AFFECTED_BY(effectName, stacks).equals(e.getQuality())) {
                            passesConditional = true;
                            break;
                        }
                    }
                } else {
                    for (BattleEffect e : thisChar.getEffects()) {
                        if (Quality.AFFECTED_BY(effectName).equals(e.getQuality())) {
                            passesConditional = true;
                            break;
                        }
                    }
                }

            } else if ("IS".equals(operator)) {
                String qualityName = conditions[2];
                for (BattleEffect e : thisChar.getEffects()) {
                    if (qualityName.equals(e.getQuality())) {
                        passesConditional = true;
                        break;
                    }
                }
            } else if ("WAS".equals(operator) || "DID".equals(operator)) {
                String flagName = conditions[2];
                for (String flag : thisChar.getFlags()) {
                    if (flagName.equals(flag)) {
                        passesConditional = true;
                        break;
                    }
                }
            }
        }
        satisfiesConditional = (!isConditional || passesConditional);
    }

    private void addConditionalFlags() {
        if (firstCast) {
            if (atkFlag) {
                fromCharacter.getFlags().add(Conditional.ATTACK);
                targetCharacter.getFlags().add(Conditional.ATTACKED);
            }
            if (bufFlag) {
                fromCharacter.getFlags().add(Conditional.BUFF);
                targetCharacter.getFlags().add(Conditional.BUFFED);
            }
            if (debFlag) {
                fromCharacter.getFlags().add(Conditional.DEBUFF);
                targetCharacter.getFlags().add(Conditional.DEBUFFED);
            }
            // these two might have to come out of this IF...

            if (dmgFlag) {
                fromCharacter.getFlags().add(Conditional.DAMAGE);
                targetCharacter.getFlags().add(Conditional.DAMAGED);
            }
            if (kilFlag) {
                fromCharacter.getFlags().add(Conditional.KILL);
                targetCharacter.getFlags().add(Conditional.KILLED);
            }
        }

    }

    private void applyEffectLogic() {
        if (hasQuality) {
            checkForReveal();
        }
        if (hasStatMods) {
            checkForDamageModsAndStackShields();
            applyHPChange();
            applyEnergyChange();
            applyNewShields();
        }
    }

    private void checkForReveal() {
        if (isReveal) {
            for (BattleEffect effectEnded : currentTargetEffects) {
                if (!effectEnded.isVisible()) {
                    // their hidden effect is ending, remove it as normal but replace it with a 1
                    // turn blank effect that's not hidden
                    effectEnded.setDescription(effectEnded.getName() + " has ended.");
                    effectEnded.setQuality(null);
                    effectEnded.setStatMods(new HashMap<>());
                    effectEnded.setCondition(null);
                    effectEnded.setConditional(false);
                    // 995 code for hidden skill ending, revealed
                    effectEnded.setDuration(995);
                    effectEnded.setVisible(true);
                }
            }
        }
    }

    private void checkForDamageModsAndStackShields() {
        // ========================
        // Description: Check my active Effects for Damage Out mods
        // Method Name:
        // return type:
        // ========================
        for (BattleEffect e : currentFromEffects) {
            if (isDmg && !isHeal) {
                if (e.getStatMods().get(Stat.DAMAGE_OUT) != null) {
                    totalIn = totalIn + e.getStatMods().get(Stat.DAMAGE_OUT);
                    hasMods = true;
                }
                if (e.getStatMods().get(Stat.PHYSICAL_DAMAGE_OUT) != null && effect.isPhysical()) {
                    totalIn = totalIn + e.getStatMods().get(Stat.PHYSICAL_DAMAGE_OUT);
                    hasMods = true;
                }
                if (e.getStatMods().get(Stat.MAGICAL_DAMAGE_OUT) != null && effect.isMagical()) {
                    totalIn = totalIn + e.getStatMods().get(Stat.MAGICAL_DAMAGE_OUT);
                    hasMods = true;
                }
                if (e.getStatMods().get(Stat.AFFLICTION_DAMAGE_OUT) != null && effect.isAffliction()) {
                    totalIn = totalIn + e.getStatMods().get(Stat.AFFLICTION_DAMAGE_OUT);
                    hasMods = true;
                }
            }
        }

        // ========================
        // Description: Check target's active Effects for Damage Mods and Ongoing
        // shieldgain
        // Method Name:
        // return type:
        // ========================
        for (BattleEffect e : currentTargetEffects) {
            // increment ongoing shield gain
            if (isShieldGain && !shieldsApplied) {
                gainAmt = effect.getStatMods().get(Stat.SHIELD_GAIN);
                if (e.getName().equals(effect.getName())) {
                    if (e.getStatMods().get(Stat.SHIELDS) != null) {
                        shields = e.getStatMods().get(Stat.SHIELDS);
                        e.getStatMods().put(Stat.SHIELDS, shields + gainAmt);
                        e.setDescription("This unit has " + (shields + gainAmt) + " shields.");
                        shieldsApplied = true;
                    }
                }
            }
            // check for dmg mods (flat and percentage)
            if (isDmg && !isHeal) {
                if (e.getStatMods() != null) {
                    if (e.getStatMods().get(Stat.DAMAGE_IN) != null) {
                        totalIn = totalIn + e.getStatMods().get(Stat.DAMAGE_IN);
                        hasMods = true;
                    }
                    if (e.getStatMods().get(Stat.ARMOR) != null && !isAffliction) {
                        totalAr = totalAr + e.getStatMods().get(Stat.ARMOR);
                        hasMods = true;
                    }
                    if (effect.isMagical()) {
                        if (e.getStatMods().get(Stat.MAGICAL_DAMAGE_IN) != null) {
                            totalIn = totalIn + e.getStatMods().get(Stat.MAGICAL_DAMAGE_IN);
                            hasMods = true;
                        }
                        if (e.getStatMods().get(Stat.MAGICAL_ARMOR) != null) {
                            totalAr = totalAr + e.getStatMods().get(Stat.MAGICAL_ARMOR);
                            hasMods = true;
                        }
                    }
                    if (effect.isPhysical()) {
                        if (e.getStatMods().get(Stat.PHYSICAL_DAMAGE_IN) != null) {
                            totalIn = totalIn + e.getStatMods().get(Stat.PHYSICAL_DAMAGE_IN);
                            hasMods = true;
                        }
                        if (e.getStatMods().get(Stat.PHYSICAL_ARMOR) != null) {
                            totalAr = totalAr + e.getStatMods().get(Stat.PHYSICAL_ARMOR);
                            hasMods = true;
                        }
                    }
                    if (isAffliction) {
                        if (e.getStatMods().get(Stat.AFFLICTION_DAMAGE_IN) != null) {
                            totalIn = totalIn + e.getStatMods().get(Stat.AFFLICTION_DAMAGE_IN);
                            hasMods = true;
                        }
                    }
                }
            }
        }
    }

    private void applyNewShields() {
        // ========================
        // Description: New shields are applied
        // Method Name:
        // return type:
        // ========================
        if (!shieldsApplied && isShieldGain) {
            gainAmt = effect.getStatMods().get(Stat.SHIELD_GAIN);
            BattleEffect shieldEffect = new BattleEffect(effect);
            int shieldInt = randomInt() + 1;
            shieldEffect.setInstanceId(shieldInt);
            shieldEffect.setGroupId(randomInt);
            // TODO: this makes shields infinite, add check here later if we want them to
            // fall off
            shieldEffect.setDuration(-1);
            shieldEffect.setOriginCharacter(fromCharacter.getPosition());
            shieldEffect.setTargetCharacter(targetCharacter.getPosition());
            shieldEffect.getStatMods().remove(Stat.SHIELD_GAIN);
            shieldEffect.getStatMods().put(Stat.SHIELDS, gainAmt);
            shieldEffect.setInterruptable(false);
            shieldEffect.setDescription("This unit has " + gainAmt + " shields.");
            addTargetEffectToCombatant(shieldEffect);
        }
    }

    private void applyEnergyChange() {

        // ========================
        // Description: Energy change is applied (lost or gained)
        // Method Name:
        // return type:
        // ========================
        if (isEnergyChange) {
            Map<String, Integer> targeted;
            if (Math.abs(targetCharacter.getPosition() - fromCharacter.getPosition()) > 2) {
                targeted = enemyEnergy;
            } else {
                targeted = energy;
            }

            if (isStrengthChange) {
                int val = effect.getStatMods().get(Stat.STRENGTH_CHANGE);
                int oldVal = targeted.get(Energy.STRENGTH);
                int newVal = oldVal + val;
                if (newVal >= 0) {
                    targeted.put(Energy.STRENGTH, newVal);
                }
            }
            if (isDexterityChange) {
                int val = effect.getStatMods().get(Stat.DEXTERITY_CHANGE);
                int oldVal = targeted.get(Energy.DEXTERITY);
                int newVal = oldVal + val;
                if (newVal >= 0) {
                    targeted.put(Energy.DEXTERITY, newVal);
                }
            }
            if (isArcanaChange) {
                int val = effect.getStatMods().get(Stat.ARCANA_CHANGE);
                int oldVal = targeted.get(Energy.ARCANA);
                int newVal = oldVal + val;
                if (newVal >= 0) {
                    targeted.put(Energy.ARCANA, newVal);
                }
            }
            if (isDivinityChange) {
                int val = effect.getStatMods().get(Stat.DIVINITY_CHANGE);
                int oldVal = targeted.get(Energy.DIVINITY);
                int newVal = oldVal + val;
                if (newVal >= 0) {
                    targeted.put(Energy.DIVINITY, newVal);
                }
            }
            if (isRandomChange) {
                int val = effect.getStatMods().get(Stat.ENERGY_CHANGE);
                if (val > 0) {
                    drawEnergy(val, targeted);
                } else if (val < 0) {
                    randomlyRemoveN(targeted, val);
                }
            }
        }
    }

    private void applyHPChange() {
        if (isDmg || isHeal || isShieldLoss) {
            calculateDamage();
            removeShields();
            setFinalDamage();
        }
    }

    private void calculateDamage() {
        if (isDmg || isHeal) {
            if (effect.getStatMods().get(Stat.DAMAGE) != null) {
                if (hasMods && !isHeal) {
                    int dmg = effect.getStatMods().get(Stat.DAMAGE);
                    int flatDmg = dmg + totalIn;
                    if (flatDmg > 0) {
                        int nonArmor = 100 - totalAr;
                        double armorMultiplier = (double) nonArmor / (double) 100;
                        double afterArmor = flatDmg * armorMultiplier;

                        finalDamage = (int) Math.round(afterArmor);
                    }
                } else {
                    finalDamage = effect.getStatMods().get(Stat.DAMAGE);
                }
            }
            if (effect.getStatMods().get(Stat.BONUS_DAMAGE) != null) {
                if (hasMods) {
                    int dmg = effect.getStatMods().get(Stat.BONUS_DAMAGE);
                    int nonArmor = 100 - totalAr;
                    double armorMultiplier = (double) nonArmor / (double) 100;
                    double afterArmor = dmg * armorMultiplier;

                    finalDamage = (int) Math.round(afterArmor);
                } else {
                    finalDamage = finalDamage + effect.getStatMods().get(Stat.BONUS_DAMAGE);
                }
            }
            if (effect.getStatMods().get(Stat.PIERCING_DAMAGE) != null) {
                if (hasMods) {
                    int dmg = effect.getStatMods().get(Stat.PIERCING_DAMAGE);
                    int flatDmg = dmg + totalIn;
                    if (flatDmg > 0) {
                        finalDamage = finalDamage + flatDmg;
                    }
                } else {
                    finalDamage = finalDamage + effect.getStatMods().get(Stat.PIERCING_DAMAGE);
                }
            }
        }
    }

    private void removeShields() {
        if (!isHeal && isDamagable && !isAffliction) {
            for (BattleEffect ef : currentTargetEffects) {
                if (ef.getStatMods() != null) {
                    if (ef.getStatMods().get(Stat.SHIELDS) != null) {
                        if (isShieldLoss) {
                            shields = ef.getStatMods().get(Stat.SHIELDS);
                            if (-lossAmt >= shields) {
                                lossAmt = lossAmt + shields;
                                ef.setDuration(997);
                                ef.getStatMods().put(Stat.SHIELDS, 0);
                                ef.setDescription("This unit has lost all shields.");
                            } else {
                                ef.getStatMods().put(Stat.SHIELDS, shields - lossAmt);
                            }
                        } else {
                            shields = ef.getStatMods().get(Stat.SHIELDS);
                            if (finalDamage > 0) {
                                finalDamage = finalDamage - shields;
                                if (finalDamage < 0) {
                                    ef.getStatMods().put(Stat.SHIELDS, -finalDamage);
                                    ef.setDescription("This unit has " + -finalDamage + " shields.");
                                    finalDamage = 0;
                                } else {
                                    ef.setDuration(997);
                                }
                            }
                        }
                    }
                }
            }
            // remove destroyed shield effects
            if (shields > 0 && finalDamage >= 0) {
                currentTargetEffects.removeIf(ex -> {
                    return ex.getDuration() == 997;
                });
            }
        }
    }

    private void setFinalDamage() {
        int trueDmg = 0;
        if (effect.getStatMods().get(Stat.TRUE_DAMAGE) != null) {
            trueDmg = effect.getStatMods().get(Stat.TRUE_DAMAGE);
        }
        int oldHP = targetCharacter.getHp();
        if (!isDamagable) {
            targetCharacter.setHp(oldHP - trueDmg);
        } else {
            targetCharacter.setHp(oldHP - (finalDamage + trueDmg));
        }
        // set dmg and kill flags
        if (oldHP > targetCharacter.getHp()) {
            dmgFlag = true;
            if (targetCharacter.getHp() == 0) {
                kilFlag = true;
            }
        }
    }

    public void addTargetEffectToCombatant(BattleEffect battleEffect) {
        battleEffect.setCombatant(targetCharacter);
        currentTargetEffects.add(battleEffect);
    }

    private void addNewEffect() {
        BattleEffect newEffect = new BattleEffect(effect);
        newEffect.setInstanceId(randomInt());
        newEffect.setGroupId(randomInt);
        newEffect.setOriginCharacter(fromCharacter.getPosition());
        newEffect.setTargetCharacter(targetCharacter.getPosition());

        int dur = newEffect.getDuration() - 1;
        // all abilities, (dmg has 1 more duration)
        if (dur > 0) {
            if (effect.isVisible()) {
                newEffect.setDuration(dur);
            }
            addTargetEffectToCombatant(newEffect);
        }
        // 1 turn buffs and debuffs
        if (dur == 0) {
            if ((hasQuality && nonDmg) || (hasStatMods && nonDmg && !isShieldGain)) {
                // 999 is gonna be code for (technically 0, but ends this turn) ?? we'll try it.
                newEffect.setDuration(999);
                addTargetEffectToCombatant(newEffect);
            } else if (!isVisible) {
                addTargetEffectToCombatant(newEffect);
            }
        }
        // infinite effects
        if (dur == -2) {
            newEffect.setDuration(-1);
            String newQ = newEffect.getQuality();
            if (!isNullOrEmpty(newQ)) {
                if (newEffect.isStacks()) {
                    boolean set = false;
                    for (BattleEffect c : currentTargetEffects) {
                        String curQ = c.getQuality();
                        if (!isNullOrEmpty(curQ) && !isNullOrEmpty(newQ)) {
                            if (newQ.length() < curQ.length()) {
                                String cutCurQ = curQ.substring(0, newQ.length());
                                // TODO: this is limiting this to 9 stacks
                                String curStacks = curQ.substring(curQ.length() - 1);
                                if (cutCurQ.equals(newQ) && newEffect.isStacks()) {
                                    c.setQuality(newQ + "_" + (Integer.parseInt(curStacks) + 1));
                                    set = true;
                                }
                            }
                        }
                    }
                    if (!set) {
                        newEffect.setQuality(newQ + "_" + 1);
                        addTargetEffectToCombatant(newEffect);
                    }
                } else {
                    addTargetEffectToCombatant(newEffect);
                }
            }
        }
    }

    private void progressOldEffect() {
        int index = -1;
        int counter = 0;
        for (BattleEffect oldEffect : currentTargetEffects) {
            if (effect.getInstanceId() == oldEffect.getInstanceId()) {
                index = counter;
                break;
            }
            counter++;
        }
        if (index == -1) {
            // fucking i dont know... uh oh.
        } else {
            // its pre-existing

            int dur = effect.getDuration() - 1;

            if (hiddenPass && !effect.isVisible()) {
                if (dur > 0 && dur < 900) {
                    effect.setDuration(dur);
                    effect.setCombatant(targetCharacter);
                    currentTargetEffects.set(index, effect);
                }
                if (dur == 0) {
                    // their hidden effect is ending, remove it as normal but replace it with a 1
                    // turn blank effect that's not hidden
                    BattleEffect effectEnded = currentTargetEffects.get(index);
                    effectEnded.setQuality(null);
                    effectEnded.setStatMods(new HashMap<>());
                    effectEnded.setCondition(null);
                    effectEnded.setConditional(false);
                    effectEnded.setDescription(effectEnded.getName() + " has ended.");
                    // 995 code for hidden skill ending, revealed
                    effectEnded.setDuration(995);
                    effectEnded.setVisible(true);
                    effectEnded.setInterruptable(false);
                    effectEnded.setStacks(false);
                    effect.setCombatant(targetCharacter);
                    currentTargetEffects.set(index, effectEnded);
                }
            } else if (!hiddenPass && effect.isVisible()) {
                if (dur > 0 && dur < 900) {
                    effect.setDuration(dur);
                    effect.setCombatant(targetCharacter);
                    currentTargetEffects.set(index, effect);
                }
                if (dur == 994) {
                    effect.setCombatant(null);
                    currentTargetEffects.remove(index);
                }
                if (dur == 0 || dur == 998) {
                    if (((hasQuality && nonDmg) || (hasStatMods && nonDmg && !isShieldGain)) && dur == 0) {
                        // 999 is gonna be code for (technically 0, but ends this turn) ?? we'll try it.
                        effect.setDuration(999);
                        effect.setCombatant(targetCharacter);
                        currentTargetEffects.set(index, effect);
                    } else {
                        effect.setCombatant(null);
                        currentTargetEffects.remove(index);
                    }
                } else {
                    // infinite effects, dur = -1 right lol.
                }
            }
        }
    }

}
