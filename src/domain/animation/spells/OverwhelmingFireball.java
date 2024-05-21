package domain.animation.spells;

import domain.animation.Vector;

public class OverwhelmingFireball extends Spell {
	private static final long serialVersionUID = 1637097591031136024L;

	public OverwhelmingFireball(Vector position) {
		super(position);
	}
	
	@Override
	public int getType() {
		return OVERWHELMING_FIREBALL;
	}

	public void startSpell() {};
	public void stopSpell() {};
}

