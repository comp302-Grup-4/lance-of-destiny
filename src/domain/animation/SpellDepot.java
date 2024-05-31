package domain.animation;

import java.io.Serializable;
import java.util.HashMap;

import domain.animation.spells.Spell;

public class SpellDepot implements Serializable {
	private static final long serialVersionUID = -112337982175135601L;
	private HashMap<Spell, Integer> spellMap;
	private boolean HexExists;
	private boolean MSEExists;
	
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
		// set hexexists and mseexists to true if the spell is hex or mse
		if (s.getType() == 1) {
			HexExists = true;
		}
		else if (s.getType() == 2) {
			MSEExists = true;
		}
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

	public boolean checkHexExists() {
		return HexExists;
	}

	public void setHexExists(boolean hexExists) {
		HexExists = hexExists;
	}

	public boolean checkMSEExists() {
		return MSEExists;
	}

	public void setMSEExists(boolean mseExists) {
		MSEExists = mseExists;
	}
}