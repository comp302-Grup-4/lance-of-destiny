package domain.animation.collision;

import java.security.SecureRandom;

import domain.animation.SpellDepot;
import domain.animation.Vector;
import domain.animation.barriers.Barrier;
import domain.animation.spells.DoubleAccel;
import domain.animation.spells.FelixFelicis;
import domain.animation.spells.Hex;
import domain.animation.spells.HollowPurple;
import domain.animation.spells.InfiniteVoid;
import domain.animation.spells.MagicalStaffExpansion;
import domain.animation.spells.OverwhelmingFireball;
import domain.animation.spells.Spell;

public class SpellFactory {
	
	private static SpellFactory instance = null; 
	private static SecureRandom rand = new SecureRandom();
	
	public Spell createSpell(int spellType, Vector position) {
		Spell newSpell;
		switch (spellType) {
		case Spell.FELIX_FELICIS:
			newSpell = new FelixFelicis(position);
			break;
		case Spell.HEX:
			newSpell = new Hex(position);
			break;
		case Spell.OVERWHELMING_FIREBALL:
			newSpell = new OverwhelmingFireball(position);
			break;
		case Spell.MAGICAL_STAFF_EXPANSION:
			newSpell = new MagicalStaffExpansion(position);
			break;
		case Spell.INFINITE_VOID:
			newSpell = new InfiniteVoid(position);
			break;
		case Spell.DOUBLE_ACCEL:
			newSpell = new DoubleAccel(position);
			break;
		case Spell.HOLLOW_PURPLE:
			newSpell = new HollowPurple(position);
			break;
		default:
			System.err.println("Spell type could not be found.");
			newSpell = new FelixFelicis(position);
			break;
		}
		return newSpell;
	}
	
	public Spell createSpell(int spellType) {
		return createSpell(spellType, Vector.of(0, 0));
	}
	
	public Spell createRandomSpellForBarriers(Barrier barrier) {
		int spellType = rand.nextInt(0, 4);
		return createSpell(spellType, barrier.getCenterPoint());
	}
	
	public static SpellFactory getInstance() {
		if (instance == null) {
			return new SpellFactory();
		} else {
			return instance;
		}
	}
}
