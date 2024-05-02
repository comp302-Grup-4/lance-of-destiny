package domain.animation.spells;

import domain.animation.Vector;

public class Hex extends Spell {
	private static final long serialVersionUID = 6889631466234664321L;

	public Hex(Vector position) {
		super(position);
	}
	
	@Override
	public int getType() {
		return HEX;
	}

}

