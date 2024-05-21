package domain.animation.spells;

import domain.animation.Vector;

public class HollowPurple extends Spell {
	private static final long serialVersionUID = 938044675247665148L;

	public HollowPurple(Vector position) {
		super(position);
	}

	@Override
	public int getType() {
		return HOLLOW_PURPLE;
	}

	public void startSpell() {};
	public void stopSpell() {};
}
