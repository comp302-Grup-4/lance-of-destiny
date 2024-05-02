package domain.animation.spells;

import domain.animation.Vector;

public class MagicalStaffExpansion extends Spell {
	private static final long serialVersionUID = 193880197544713420L;

	public MagicalStaffExpansion(Vector position) {
		super(position);
	}

	@Override
	public int getType() {
		return MAGICAL_STAFF_EXPANSION;
	}

}

