package slogan.motion.outsidersapi.util;

import slogan.motion.outsidersapi.domain.jpa.Combatant;

import java.util.Comparator;

public class SortCharactersById implements Comparator<Combatant> {
	
	public int compare(Combatant a, Combatant b) {
		return a.getCharacterId() - b.getCharacterId();
	}
}
