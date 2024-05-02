package domain.animation.spells;

import domain.animation.Vector;

public class InfiniteVoid extends Spell {
	private static final long serialVersionUID = -4349142759511666670L;

	public InfiniteVoid(Vector position) {
		super(position);
	}

	@Override
	public int getType() {
		return INFINITE_VOID;
	}
}
