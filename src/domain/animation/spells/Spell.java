package domain.animation.spells;

import domain.animation.AnimationObject;
import domain.animation.Vector;

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

	@Override
	public Vector[] getBoundaryPoints() {
		return boundaryPoints;
	}
	
	@Override
	public Vector getCenterPoint() {
		// TODO Auto-generated method stub
		return center;
	}

	@Override
	public void initializeBoundaryPoints() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initializeCenterPoint() {
		// TODO Auto-generated method stub
		
	}
}
