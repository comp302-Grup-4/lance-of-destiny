package domain.animation.spells;

import domain.Vector;
import domain.animation.AnimationObject;

public class Spell extends AnimationObject {
	protected String spellType;

	@Override
	public float getRotation() {
		return 0;
	}

	@Override
	public boolean isCollidable() {
		return true;
	}
	
	
}
