package domain.animation.spells;

import domain.animation.Vector;

public class DoubleAccel extends Spell {
	private static final long serialVersionUID = -1052801607880125401L;

	public DoubleAccel(Vector position) {
		super(position);
	}

	@Override
	public int getType() {
		return DOUBLE_ACCEL;
	}
}
