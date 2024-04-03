package domain.animation;

import java.util.HashMap;

public class SpellDepot {
	private HashMap<Spell, Integer> spellMap;
	
	public SpellDepot() {
		this.spellMap = new HashMap<Spell,Integer>();
	}
	
	public SpellDepot(HashMap<Spell, Integer> spellMap) {
		this.spellMap = spellMap;
	}

	public HashMap<Spell, Integer> getSpellMap() {
		return spellMap;
	}

	public void setSpellMap(HashMap<Spell, Integer> spellMap) {
		this.spellMap = spellMap;
	}
	
	public void addSpell(Spell s) {
		if(!spellMap.containsKey(s)) {
			spellMap.put(s, 1);
		}
		else {
			spellMap.replace(s, spellMap.get(s) + 1);
		}
	}
	
	public void popSpell(Spell s) {
		if (spellMap.get(s)==1) {
			spellMap.remove(s);
		}
		else if (spellMap.get(s)>1) {
			spellMap.replace(s, spellMap.get(s)-1);
		}
	}
	
	public boolean checkSpellExists(Spell s) {
		return spellMap.containsKey(s);
	}
}
